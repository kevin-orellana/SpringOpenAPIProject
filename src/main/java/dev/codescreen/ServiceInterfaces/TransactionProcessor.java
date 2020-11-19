package dev.codescreen.ServiceInterfaces;

import dev.codescreen.BusinessRules.AuthorizationRequestRule;
import dev.codescreen.BusinessRules.LoadRequestRule;
import dev.codescreen.BusinessRules.ValidAccountRule;
import dev.codescreen.EventDriven.AuthorizeRequestEvent;
import dev.codescreen.EventDriven.Event;
import dev.codescreen.EventDriven.LoadRequestEvent;
import dev.codescreen.EventDriven.RequestResult;
import dev.codescreen.Models.*;
import dev.codescreen.Requests.AuthorizationRequest;
import dev.codescreen.Requests.LoadRequest;
import dev.codescreen.Responses.AuthorizationResponse;
import dev.codescreen.Responses.LoadResponse;
import dev.codescreen.Responses.ResponseCode;
import dev.codescreen.Services.AccountManager;
import dev.codescreen.Services.RuleEngineService;
import dev.codescreen.Utils.RuleEngine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionProcessor {

    /*
    * TransactionProcessors are the "workers" that have received events from the QueueEventService.
    * These "workers" may be computing nodes that are capable of handling heavy workloads and can be
    * increased in quantity if needed.
    * */
    public static <T extends Event> RequestResult processSynchronously(T event){
        RequestResult result;
        try {
            if (LoadRequestEvent.TYPE.equals(event.getType())){
                result = processLoadRequestEvent((LoadRequestEvent)event);
            } else if (AuthorizeRequestEvent.TYPE.equals(event.getType())){
                result = processAuthorizeEvent((AuthorizeRequestEvent) event);
            } else {
                throw new IllegalArgumentException("Event of type " + event.getClass().getCanonicalName() + " not supported by TransactionProcessor");
            }
        } catch (Exception e){
            e.printStackTrace();
            result = new RequestResult() {
                @Override
                public boolean successfullyExecuted() {
                    return false;
                }

                @Override
                public String getMessage() {
                    return "Failed processing - re-queuing this event" + e.getMessage();
                }
            };

            // requeue the event
            event.increaseAttemptCount();
            QueueEventService.fireEvent(event, QueueEventService.FORCE_ASYNC);
            return result;
        }

        postEventProcessing(event);
        return result;
    }

    private static void postEventProcessing(Event event){
        event.setEndProcessingTime(LocalDateTime.now());
        event.calculateProcessingWallThread();
        event.setMachineName(Thread.currentThread().getName());
    }

    private static RequestResult processLoadRequestEvent(LoadRequestEvent event){
        RequestResult requestResult;

        try {
            LoadRequest loadRequest = event.getLoadRequest();

            // fetch top-level entities we will request a Write lock with
            User user = UserService.getUserById(loadRequest.getUserId());
            Account account = AccountManager.createAccountIfMissingForUser(user);

            // fetch distributed lock by account+userId path

            // execute the load request (add funds to account)
            TransactionAmount transactionAmount = loadRequest.getTransactionAmount();
            AccountBalance currentBalance = account.getAccountBalance();

            // validate a load request
            RuleEngine ruleEngine = RuleEngineService.createRuleEngine();
            ruleEngine.addPattern(Account.class.getCanonicalName(), account);
            ruleEngine.addPattern(LoadRequest.class.getCanonicalName(), loadRequest);
            ruleEngine.addRule(new ValidAccountRule(ruleEngine));
            ruleEngine.addRule(new LoadRequestRule(ruleEngine));
            ruleEngine.run();

            if (ruleEngine.hasErrors()){
                // do not process this request
                account.addTransactionRequestToHistory(loadRequest);
                throw new RuntimeException("Authorization Request Failed Validation!");
            } else {
                account.addTransactionRequestToHistory(loadRequest);

                // final execution
                BigDecimal currentAmount = currentBalance.getAmount();
                BigDecimal additiveAmount = transactionAmount.getAmount();
                BigDecimal newBalance = currentAmount.add(additiveAmount);

                currentBalance.setAmount(newBalance);
                account.setAccountBalance(currentBalance);

                // persist to datastore
                // notify any other event-driven services (e.g. extract-transform-load, user subscriptions)

                // return successful result
                LoadResponse loadResponse = new LoadResponse(user.getUserId(), loadRequest.getMessageId(), currentBalance);

                LoadRequestResult result = new LoadRequestResult();
                result.setLoadResponse(loadResponse);
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            // fatal error - we need to distinguish between a bad request and a failed request processing
            // if it made it here - this is likely a bad request.
            e.printStackTrace();

            requestResult = new LoadRequestResult();
            requestResult.setSuccess(false);
            requestResult.setMessage(e.getMessage());
        }

        return requestResult;
    }

    private static RequestResult processAuthorizeEvent(AuthorizeRequestEvent event){
        RequestResult requestResult;

        try {
            AuthorizationRequest authorizationRequest = event.getAuthorizationRequest();

            // fetch top-level entities we will request a Write lock with
            User user = UserService.getUserById(authorizationRequest.getUserId());
            Account account = AccountManager.createAccountIfMissingForUser(user);

            // fetch distributed lock by account+userId path

            // execute the load request (reduce funds amount in account)
            TransactionAmount transactionAmount = authorizationRequest.getTransactionAmount();
            AccountBalance currentBalance = account.getAccountBalance();

            // validate a AuthorizeRequest
            RuleEngine ruleEngine = RuleEngineService.createRuleEngine();
            ruleEngine.addPattern(Account.class.getCanonicalName(), account);
            ruleEngine.addPattern(AuthorizationRequest.class.getCanonicalName(), authorizationRequest);
            ruleEngine.addRule(new ValidAccountRule(ruleEngine));
            ruleEngine.addRule(new AuthorizationRequestRule(ruleEngine));
            ruleEngine.run();

            // decide if we want to process this request
            if (ruleEngine.hasErrors()){
                account.addTransactionRequestToHistory(authorizationRequest);
                // do not process this request
                throw new RuntimeException("Authorization Request Failed Validation!");
            } else {
                account.addTransactionRequestToHistory(authorizationRequest);
                // validation complete - process the request
                BigDecimal currentAmount = currentBalance.getAmount();
                BigDecimal subtractiveAmount = transactionAmount.getAmount();
                BigDecimal newBalance = currentAmount.subtract(subtractiveAmount);

                currentBalance.setAmount(newBalance);
                // persist to datastore
                // notify any other event-driven services (e.g. extract-transform-load, user subscriptions)

                AuthorizationResponse authorizationResponse = new AuthorizationResponse(user.getUserId(), authorizationRequest.getMessageId(), ResponseCode.APPROVED, currentBalance);
                AuthorizationRequestResult result = new AuthorizationRequestResult();
                result.setAuthorizationResponse(authorizationResponse);
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            // this is likely a bad request - do not add it to the account's transaction history
            requestResult = new LoadRequestResult();
            requestResult.setSuccess(false);
            requestResult.setMessage(e.getMessage());
        }

        return requestResult;
    }
}
