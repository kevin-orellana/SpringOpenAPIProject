package dev.codescreen.Models;

/*
* Hold the possible values for a server's health.
* */
public class ServerStatus {
    public enum HEALTH_STATUS {
        HEALTHY,
        UNHEALTHY
    }
    String description;
    String health;

    public String getHealth() {
        return health;
    }

    public ServerStatus(String health, String description) {
        this.health = health;
        this.description = description;
    }
}
