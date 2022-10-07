package com.github.fengxxc.model;

public class ServiceSignTreeNode extends RtTreeNode {
    private String serviceName = "";
    private String serviceBeanName = "";
    private String signatureName;

    public ServiceSignTreeNode(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceSignTreeNode(String serviceName, String serviceBeanName) {
        this.serviceName = serviceName;
        this.serviceBeanName = serviceBeanName;
    }

    public ServiceSignTreeNode(String serviceName, String serviceBeanName, String signatureName) {
        this.serviceName = serviceName;
        this.serviceBeanName = serviceBeanName;
        this.signatureName = signatureName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceSignTreeNode setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getServiceBeanName() {
        return serviceBeanName;
    }

    public ServiceSignTreeNode setServiceBeanName(String serviceBeanName) {
        this.serviceBeanName = serviceBeanName;
        return this;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public ServiceSignTreeNode setSignatureName(String signatureName) {
        this.signatureName = signatureName;
        return this;
    }
}
