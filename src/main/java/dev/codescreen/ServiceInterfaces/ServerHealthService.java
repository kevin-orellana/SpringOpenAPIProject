package dev.codescreen.ServiceInterfaces;

import dev.codescreen.Models.ServerStatus;

/*
* Service that pulls data from application resources to indicate the application's health.
* */
public class ServerHealthService {
    private static ServerStatus serverStatus;

    public static ServerStatus getServerStatus(){
        return serverStatus;
    }

    public static void setServerStatus(ServerStatus status) {
        serverStatus = status;
    }

    public static ServerStatus createHealthyStatus(){
        return new ServerStatus(ServerStatus.HEALTH_STATUS.HEALTHY.name(), "All is well.");
    }

    public static ServerStatus createUnhealthyStatus(){
        return new ServerStatus(ServerStatus.HEALTH_STATUS.UNHEALTHY.name(), "Things could be better.");
    }

    public static void setHealthy(boolean healthy){
        setServerStatus(healthy ? createHealthyStatus() : createUnhealthyStatus());
    }

}
