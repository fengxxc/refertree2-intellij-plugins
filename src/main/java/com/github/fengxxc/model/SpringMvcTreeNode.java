package com.github.fengxxc.model;


import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author fengxxc
 * @date 2022-09-25
 */
public class SpringMvcTreeNode extends RtTreeNode {
    private String uri;
    private String method = "";

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

    @Override
    public String toString() {
        return "SpringMvcTreeNode{" +
                "uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
