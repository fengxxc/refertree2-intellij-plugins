package com.github.fengxxc.model;


import com.intellij.openapi.vfs.VirtualFile;

import java.util.Map;

/**
 * @author fengxxc
 * @date 2022-09-25
 */
public class SpringMvcTreeNode extends RtTreeNode {
    private String uri;
    private String method = "";
    private Map<String, VirtualFile> serviceMap;

    public SpringMvcTreeNode(VirtualFile virtualFile) {
        super();
        super.setVirtualFile(virtualFile);
    }

    public SpringMvcTreeNode(String uri, String method, int textOffset) {
        this.uri = uri;
        this.method = method;
        super.setTextOffset(textOffset);
    }

    public String getUri() {
        return uri;
    }

    public SpringMvcTreeNode setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, VirtualFile> getServiceMap() {
        return serviceMap;
    }

    public SpringMvcTreeNode setServiceMap(Map<String, VirtualFile> serviceMap) {
        this.serviceMap = serviceMap;
        return this;
    }

    @Override
    public String toString() {
        return "SpringMvcTreeNode{" +
                "uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
