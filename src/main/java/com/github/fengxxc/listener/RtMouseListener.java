package com.github.fengxxc.listener;

import com.github.fengxxc.Singleton;
import com.github.fengxxc.model.RtTreeNode;
import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author fengxxc
 * @date 2022-09-29
 */
public class RtMouseListener extends MouseAdapter {
    private Project project;

    public RtMouseListener(Project project) {
        this.project = project;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // super.mouseClicked(e);
        // if (e.getSource() == tree && e.getClickCount() == 2) {
        Tree tree = Singleton.getTree();
        if (e.getSource() == tree && e.isAltDown()) {
            TreePath pathForLocation = tree.getPathForLocation(e.getX(), e.getY());
            if (pathForLocation == null) {
                return;
            }
            RtTreeNode node = (RtTreeNode) pathForLocation.getLastPathComponent();
            IntellijUtil.openAndFixedInFile(project, node);
        }
    }
}
