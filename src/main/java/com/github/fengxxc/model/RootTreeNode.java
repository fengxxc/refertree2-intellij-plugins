package com.github.fengxxc.model;

import java.util.Date;

/**
 * @author fengxxc
 * @date 2022-09-26
 */
public class RootTreeNode extends RtTreeNode {
    private String rootName;
    private Date updateTime;
    private String pluginInfo;

    public String getRootName() {
        return rootName;
    }

    public RootTreeNode setRootName(String rootName) {
        this.rootName = rootName;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public RootTreeNode setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getPluginInfo() {
        return pluginInfo;
    }

    public RootTreeNode setPluginInfo(String pluginInfo) {
        this.pluginInfo = pluginInfo;
        return this;
    }
}
