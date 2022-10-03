package com.github.fengxxc;

import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;

public class ReferTreeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ToolWindowManager.getInstance(e.getProject()).getToolWindow("ReferTree2").show();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        IntellijUtil.scrollTreeFromPsiFile(psiFile, editor, Singleton.getTree());
    }

}
