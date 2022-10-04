package com.github.fengxxc.component;

import com.github.fengxxc.Singleton;
import com.github.fengxxc.model.RtTreeNode;
import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 * @author fengxxc
 * @date 2022-09-29
 */
public class RtEditSourceBtn extends AnAction {

    public RtEditSourceBtn(Icon icon) {
        // 编辑（跳转到）源码，快捷键: Alt+MouseLeft、MouseMiddle
        super("Edit Source Code", "编辑（跳转到）源码，快捷键: Alt+MouseLeft、MouseMiddle", icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Tree tree = Singleton.getTree();
        final TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            return;
        }
        RtTreeNode node = (RtTreeNode) selectionPath.getLastPathComponent();
        IntellijUtil.openAndFixedInFile(project, node);
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
