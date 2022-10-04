package com.github.fengxxc.model;

public class ServiceSignNode extends RtTreeNode {
    private String serviceName = "";
    private String signatureName;

    public ServiceSignNode(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceSignNode(String serviceName, String signatureName) {
        this.serviceName = serviceName;
        this.signatureName = signatureName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceSignNode setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public ServiceSignNode setSignatureName(String signatureName) {
        this.signatureName = signatureName;
        return this;
    }
}
