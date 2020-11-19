package dev.codescreen.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleEngine {
    private Map<String, Object> patterns;
    private List<Rule> unrunRules;
    protected List<String> errorMessages;

    public RuleEngine() {
        patterns = new HashMap<String, Object>();
        unrunRules = new ArrayList<Rule>();
        errorMessages = new ArrayList<String>();
    }

    public boolean hasErrors(){
        return !errorMessages.isEmpty();
    }

    public void run(){
        for (Rule rule : unrunRules){
            if (rule.when()){
                try {
                    rule.then();
                } catch (Exception e){
                    errorMessages.add(e.getMessage());
                }
            }
        }
    }

    public void addPattern(String pattern, Object object){
        patterns.put(pattern, object);
    }

    public Object getPattern(String pattern){
        return patterns.get(pattern);
    }

    public void addRule(Rule rule){
        unrunRules.add(rule);
    }

    public void addErrorMessage(String errorMessage){
        errorMessages.add(errorMessage);
    }

    public void printRuleEngineErrors(){
        for (String error : errorMessages){
            System.out.println("error: " + error);
        }
    }
}