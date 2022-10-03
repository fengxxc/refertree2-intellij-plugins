package com.github.fengxxc.model;


/**
 * @author fengxxc
 * @date 2022-09-25
 */
public class SpringMvcTreeNode extends RtTreeNode {
    private String uri;

    public SpringMvcTreeNode(String uri, int textOffset) {
        this.uri = uri;
        super.setTextOffset(textOffset);
    }

    public String getUri() {
        return uri;
    }

    public SpringMvcTreeNode setUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public String toString() {
        return uri;
    }
}
