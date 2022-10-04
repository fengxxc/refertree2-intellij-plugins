package com.github.fengxxc.component;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author fengxxc
 * @date 2022-09-29
 */
public class RtToolbar extends ActionToolbarImpl {

    public RtToolbar() {
        super("RtToolbar", new ActionGroup() {
            @NotNull
            @Override
            public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
                return new AnAction[]{
                        new RtScrollFromSourceBtn(AllIcons.General.Locate)
                        , new RtEditSourceBtn(AllIcons.Actions.EditSource)
                        // , new RtRefreshBtn(AllIcons.General.InlineRefresh)
                        , new RtRefreshBtn(AllIcons.Actions.Refresh)
                        // , new RtEditSourceBtn(AllIcons.Actions.Expandall)
                        // , new RtEditSourceBtn(AllIcons.Actions.ShowAsTree)
                };
            }
        }, true, true);
        // setTargetComponent(this);
    }
}
