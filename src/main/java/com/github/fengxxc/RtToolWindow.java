package com.github.fengxxc;

import com.github.fengxxc.component.RtToolbar;
import com.github.fengxxc.listener.RtKeyListener;
import com.github.fengxxc.listener.RtMouseListener;
import com.github.fengxxc.listener.RtTreeWillExpandListener;
import com.github.fengxxc.model.RootTreeNode;
import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RtToolWindow implements ToolWindowFactory {
    private RootTreeNode rootNode = Singleton.getRootTreeNode();
    private Tree tree = Singleton.getTree();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        rootNode.setRootName(project.getName())
                .setPluginInfo("v1.0.0")
                .setVirtualFile(project.getProjectFile());

        IntellijUtil.loadTree(project);
        tree.addKeyListener(new RtKeyListener());
        tree.setCellRenderer(new RtTreeCellRenderer(project));
        tree.addMouseListener(new RtMouseListener(project));
        tree.addTreeWillExpandListener(new RtTreeWillExpandListener(project));
        JComponent jbScrollPane = new JBScrollPane(tree);
        jbScrollPane.setAutoscrolls(true);
        jbScrollPane.setBackground(Color.magenta);
        JComponent jPanel = new JBPanel();
        jPanel.setLayout(new BorderLayout());
        // jPanel.add(new JButton("btn"));
        final SimpleToolWindowPanel simpleToolWindowPanel = new SimpleToolWindowPanel(false, true);
        RtToolbar rtToolbar = new RtToolbar();
        // rtToolbar.setTargetComponent(rtToolbar);
        simpleToolWindowPanel.setToolbar(rtToolbar);
        simpleToolWindowPanel.setContent(jbScrollPane);
        simpleToolWindowPanel.setComponentPopupMenu(new JBPopupMenu("JBPopupMenu"));
        jPanel.add(simpleToolWindowPanel);
        jPanel.registerKeyboardAction(e -> {
            IntellijUtil.refresh(project);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // 获取Service
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content1 = contentFactory.createContent(jPanel, "REST API", false);
        toolWindow.getContentManager().addContent(content1);
    }

}
