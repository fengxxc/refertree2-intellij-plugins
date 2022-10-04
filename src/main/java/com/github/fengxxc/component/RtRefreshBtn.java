package com.github.fengxxc.component;

import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author fengxxc
 * @date 2022-10-04
 */
public class RtRefreshBtn extends AnAction {
    public RtRefreshBtn(Icon icon) {
        super("Refresh Tree View", "刷新，快捷键: Ctrl + F5", icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        IntellijUtil.refresh(project);
    }
}
