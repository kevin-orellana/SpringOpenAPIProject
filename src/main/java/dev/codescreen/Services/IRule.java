package dev.codescreen.Services;

public interface IRule {

    // when to run this rule
    public boolean when();

    // logic in business rule
    public void then();
}
