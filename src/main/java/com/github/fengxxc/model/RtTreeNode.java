package com.github.fengxxc.model;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author fengxxc
 * @date 2022-09-26
 */
public abstract class RtTreeNode extends DefaultMutableTreeNode {

    private int signCodeStart;
    private int signCodeEnd;
    private int textOffset = 0;
    private VirtualFile virtualFile;
    private int indexInLevel;
    private int level;


    public int getSignCodeStart() {
        return signCodeStart;
    }

    public RtTreeNode setSignCodeStart(int signCodeStart) {
        this.signCodeStart = signCodeStart;
        return this;
    }

    public int getSignCodeEnd() {
        return signCodeEnd;
    }

    public RtTreeNode setSignCodeEnd(int signCodeEnd) {
        this.signCodeEnd = signCodeEnd;
        return this;
    }

    public int getTextOffset() {
        return textOffset;
    }

    public RtTreeNode setTextOffset(int textOffset) {
        this.textOffset = textOffset;
        return this;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public RtTreeNode setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
        return this;
    }

    public int getIndexInLevel() {
        return indexInLevel;
    }

    public RtTreeNode setIndexInLevel(int indexInLevel) {
        this.indexInLevel = indexInLevel;
        return this;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public RtTreeNode setLevel(int level) {
        this.level = level;
        return this;
    }

    public String getAbsFilePath() {
        VirtualFile virtualFile = this.getVirtualFile();
        if (virtualFile == null) {
            return "";
        }
        // return virtualFile.getUrl();
        return virtualFile.getPath();
    }
}
