package com.github.fengxxc.component;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManagerListener;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.options.SchemeManager;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * @author fengxxc
 * @date 2022-09-29
 */
public class RtToolbar extends ActionToolbarImpl {
    private Tree tree;

    public RtToolbar(Tree tree) {
        super("RtToolbar", new ActionGroup() {
            @NotNull
            @Override
            public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
                return new AnAction[]{
                        new RtScrollFromSourceBtn(tree, AllIcons.General.Locate)
                        , new RtEditSourceBtn(tree, AllIcons.Actions.EditSource)
                        // , new RtEditSourceBtn(AllIcons.Actions.Expandall)
                        // , new RtEditSourceBtn(AllIcons.Actions.ShowAsTree)
                };
            }
        }, true, true);
        this.tree = tree;
        // setTargetComponent(this);
    }
}
