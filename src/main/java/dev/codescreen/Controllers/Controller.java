package dev.codescreen.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dev.codescreen.EventDriven.*;
import dev.codescreen.Models.*;
import dev.codescreen.Requests.AuthorizationRequest;
import dev.codescreen.Requests.LoadRequest;
import dev.codescreen.Responses.*;
import dev.codescreen.ServiceInterfaces.QueueEventService;
import dev.codescreen.ServiceInterfaces.ServerHealthService;
import dev.codescreen.Utils.DebitOrCredit;
import dev.codescreen.Utils.ServerErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
* Spring Controller that holds the business logic for redirecting REST requests.
* */
@RestController
public class Controller {
    /*
    * Current OpenAPI project first deliverable: GET /ping
    * Dev Notes:
    * - can be called with `curl -i localhost:8080/ping`
    * - @GetMapping.produces indicates the response's media type
    * - @GetMapping.value indicates the request path
    * - Spring will automatically convert the <T implements IPingResponse> class to APPLICATION_JSON_VALUE
    *   - each class must have the setter/getters
    * */
    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ping() {
        ServerStatus serverStatus = ServerHealthService.getServerStatus();
        if (ServerStatus.HEALTH_STATUS.HEALTHY.name().equals(serverStatus.getHealth())){
            Ping ping = new Ping(LocalDateTime.now());
            return new ResponseEntity<>(ping, HttpStatus.OK);
        } else {
            ServerError error = new ServerError("Unhealthy.", "Server slow/unresponsive because X");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Current OpenAPI project second deliverable: PUT /load/{messageId}
     * Dev Notes:
     *  - ideally, we'd serialize the HTTP request body to a LoadRequestMessage but for now we directly manipulate the JSON
     *  - can be called with
               curl -H "Accept: application/json" -H "Content-type: application/json" \
                    -X PUT -d '{
                "messageId": "55210c62-e480-asdf-bc1b-e991ac67FSAC",
                "userId": "2226e2f9-ih09-46a8-958f-d659880asdfD",
                "transactionAmount": {
                    "amount": "100.23",
                    "currency": "USD",
                    "debitOrCredit": "CREDIT"
                    }
                 }' http://localhost:8080/load/1234
    * */
    @RequestMapping(value = "/load/{messageId}", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity<?> load_messageId(@PathVariable("messageId") String messageId,
                          @RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        try {
            String userId = payload.get("userId").asText();
            JsonNode transactionAmountNode = payload.get("transactionAmount");
            String amount = transactionAmountNode.get("amount").asText();
            String currency = transactionAmountNode.get("amount").asText();
            String debitOrCredit = transactionAmountNode.get("debitOrCredit").asText();

            TransactionAmount transactionAmount = new TransactionAmount(new BigDecimal(amount), currency, DebitOrCredit.valueOf(debitOrCredit));
            LoadRequest loadRequest = new LoadRequest(messageId, userId, transactionAmount);

            LoadRequestEvent event = new LoadRequestEvent(loadRequest, EventStatus.RECEIVED);
            RequestResult result = QueueEventService.fireEvent(event);

            if (result.isSuccess()){
                LoadResponse loadResponse = ((LoadRequestResult) result).getLoadResponse();
                return new ResponseEntity<>(loadResponse, HttpStatus.CREATED);
            } else {
                // for now, we are vague with the reason as to why this load request was declined
                ServerError error = new ServerError("Something about a bad load request!",  ResponseCode.DECLINED.name());;
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            e.printStackTrace();
            ServerError error = new ServerError("Something about a bad load request!", ServerErrorCodes.BAD_LOAD_REQUEST.name());;
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Current OpenAPI project third deliverable: PUT /load/{messageId}
     * Dev Notes:
    * */
    @RequestMapping(value = "/authorization/{messageId}", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity<?> authorization_messageId(@PathVariable("messageId") String messageId,
                             @RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        try {
            String userId = payload.get("userId").asText();
            JsonNode transactionAmountNode = payload.get("transactionAmount");
            String amount = transactionAmountNode.get("amount").asText();
            String currency = transactionAmountNode.get("amount").asText();
            String debitOrCredit = transactionAmountNode.get("debitOrCredit").asText();

            TransactionAmount transactionAmount = new TransactionAmount(new BigDecimal(amount), currency, DebitOrCredit.valueOf(debitOrCredit));
            AuthorizationRequest authorizationRequest = new AuthorizationRequest(messageId, userId, transactionAmount);

            AuthorizeRequestEvent event = new AuthorizeRequestEvent(authorizationRequest, EventStatus.RECEIVED);
            RequestResult requestResult = QueueEventService.fireEvent(event);

            if (requestResult.isSuccess()){
                AuthorizationResponse authorizationResponse = ((AuthorizationRequestResult)requestResult).getAuthorizationResponse();
                return new ResponseEntity<>(authorizationResponse, HttpStatus.CREATED);
            } else {
                // for now, we are vague with the reason as to why this authorization request was declined
                ServerError error = new ServerError("Something about a bad authorization request!", ResponseCode.DECLINED.name());
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            ServerError error = new ServerError("Something about a bad authorization request!", ServerErrorCodes.BAD_AUTH_REQUEST.name());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Development debugging paths
     * */
    @RequestMapping("/") // localhost:8080
    public String index() {
        return "Response from app";
    }
}
