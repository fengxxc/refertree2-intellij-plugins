package com.github.fengxxc.component;

import com.github.fengxxc.model.RtTreeNode;
import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author fengxxc
 * @date 2022-09-30
 */
public class RtScrollFromSourceBtn extends AnAction {
    Tree tree;
    RtTreeNode rtTreeNode;

    public RtScrollFromSourceBtn(Tree tree, Icon icon) {
        super("Scroll from Source", "", icon);
        this.tree = tree;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        IntellijUtil.scrollTreeFromPsiFile(psiFile, editor, tree);
    }


    @Override
    public boolean displayTextInToolbar() {
        return false;
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}
