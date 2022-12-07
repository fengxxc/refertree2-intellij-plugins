package com.github.fengxxc.model;

import com.intellij.openapi.vfs.VirtualFile;

public class ServiceMethodRefTreeNode extends RtTreeNode {
    private String serviceName = "";
    private String serviceVarName = "";
    private String methodName;
    private VirtualFile ServiceVirtualFile;

    public ServiceMethodRefTreeNode(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceMethodRefTreeNode(String serviceName, String serviceVarName) {
        this.serviceName = serviceName;
        this.serviceVarName = serviceVarName;
    }

    public ServiceMethodRefTreeNode(String serviceName, String serviceVarName, String methodName) {
        this.serviceName = serviceName;
        this.serviceVarName = serviceVarName;
        this.methodName = methodName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceMethodRefTreeNode setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getServiceVarName() {
        return serviceVarName;
    }

    public ServiceMethodRefTreeNode setServiceVarName(String serviceVarName) {
        this.serviceVarName = serviceVarName;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public ServiceMethodRefTreeNode setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public VirtualFile getServiceVirtualFile() {
        return ServiceVirtualFile;
    }

    public ServiceMethodRefTreeNode setServiceVirtualFile(VirtualFile serviceVirtualFile) {
        ServiceVirtualFile = serviceVirtualFile;
        return this;
    }
}
