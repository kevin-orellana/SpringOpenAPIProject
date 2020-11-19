package dev.codescreen.Utils;

import dev.codescreen.Services.IRule;

public abstract class Rule implements IRule {
    protected RuleEngine ruleEngine;

    public Rule(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    public boolean when(){
        return false;
    }

    public void then(){
    }

}