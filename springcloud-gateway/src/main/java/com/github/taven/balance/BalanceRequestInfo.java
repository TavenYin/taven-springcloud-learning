package com.github.taven.balance;

public class BalanceRequestInfo {
    private String serviceId;
    private String nacosServiceCluster;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getNacosServiceCluster() {
        return nacosServiceCluster;
    }

    public void setNacosServiceCluster(String nacosServiceCluster) {
        this.nacosServiceCluster = nacosServiceCluster;
    }
}
