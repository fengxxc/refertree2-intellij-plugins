package com.github.fengxxc.component;

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionButtonLook;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.IJSwingUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

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

    @Override
    protected @NotNull ActionButton createToolbarButton(@NotNull AnAction action, ActionButtonLook look, @NotNull String place, @NotNull Presentation presentation, @NotNull Dimension minimumSize) {
        ActionButton toolbarButton = super.createToolbarButton(action, look, place, presentation, minimumSize);
        toolbarButton.setFocusable(true);
        return toolbarButton;
    }

    @Override
    protected @NotNull DataContext getDataContext() {
        final String SUPPRESS_TARGET_COMPONENT_WARNING = "ActionToolbarImpl.suppressTargetComponentWarning";
        if (super.getTargetComponent() == null && getClientProperty(SUPPRESS_TARGET_COMPONENT_WARNING) == null &&
                !ApplicationManager.getApplication().isUnitTestMode()) {
            putClientProperty(SUPPRESS_TARGET_COMPONENT_WARNING, true);
            /* Override 毙掉警告
            LOG.warn("'" + myPlace + "' toolbar by default uses any focused component to update its actions. " +
                    "Toolbar actions that need local UI context would be incorrectly disabled. " +
                    "Please call toolbar.setTargetComponent() explicitly.", myCreationTrace);
            */
        }
        Component target = super.getTargetComponent() != null ? super.getTargetComponent() : IJSwingUtilities.getFocusedComponentInWindowOrSelf(this);
        return DataManager.getInstance().getDataContext(target);
    }
}
