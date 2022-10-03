package com.github.fengxxc;

import com.github.fengxxc.model.RootTreeNode;
import com.intellij.ui.treeStructure.Tree;

public class Singleton {

    public static RootTreeNode getRootTreeNode() {
        return Holder.RootTreeNode;
    }

    public static Tree getTree() {
        return Holder.Tree;
    }

    private static class Holder {
        private static final RootTreeNode RootTreeNode = new RootTreeNode();
        private static final Tree Tree = new Tree(RootTreeNode);
    }

}
