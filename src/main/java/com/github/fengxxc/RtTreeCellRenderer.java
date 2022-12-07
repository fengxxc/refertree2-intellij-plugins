package com.github.fengxxc;

import com.github.fengxxc.model.*;
import com.github.fengxxc.util.IconUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * @author fengxxc
 * @date 2020-10-30
 */
public class RtTreeCellRenderer extends ColoredTreeCellRenderer {
    private Project project;

    public RtTreeCellRenderer(Project project) {
        super();
        this.project = project;
    }

    @Override
    public void customizeCellRenderer(@NotNull JTree jTree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.setToolTipText(((RtTreeNode) value).getAbsFilePath());
        if (value instanceof StrutsTagTreeNode) {
            StrutsTagTreeNode node = (StrutsTagTreeNode) value;
            String text = node.toString() == null ? "null" : node.toString();
            String relatePath = node.getAbsFilePath().replace(project.getBasePath(), "");

            switch (node.NodeType) {
                case StrutsTagTreeNode.NODETYPE_PACKAGE:
                case StrutsTagTreeNode.NODETYPE_ACTION:
                    this.setIcon(IconUtil.STRUTS);
                case StrutsTagTreeNode.NODETYPE_RESULT:
                    this.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    this.append("   " + relatePath, SimpleTextAttributes.GRAYED_ATTRIBUTES);
                    this.setForeground(new Color(53, 88, 167));
                    break;
                case StrutsTagTreeNode.NODETYPE_METHOD:
                    String methodSignText = node.getUserObjValueToString(StrutsTagTreeNode.METHOD_SIGN_TEXT);
                    String[] methodSignSplit = methodSignText.split(text);
                    if (methodSignSplit.length == 2) {
                        this.append(methodSignSplit[0], SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        this.append(text, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                        this.append(methodSignSplit[1], SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    } else {
                        this.append(methodSignText, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    }
                    this.append("   " + relatePath, SimpleTextAttributes.GRAYED_ATTRIBUTES);
                    this.setForeground(JBColor.DARK_GRAY);
                    break;
                default:
                    this.setForeground(JBColor.BLACK);
            }

        } else if (value instanceof SpringMvcTreeNode) {
            SpringMvcTreeNode node = (SpringMvcTreeNode) value;
            final String uri = node.getUri();
            // debugger
            // this.append(node.getIndexInLevel() + ": " + node.getSignCodeStart() + "-" + node.getSignCodeEnd(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            this.append(node.getMethod() + " ", SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES);
            if (uri != null && !"".equals(uri)) {
                if (!Search.isNull() && !"".equals(Search.content())) {
                    int index = uri.indexOf(Search.content());
                    if (index != -1) {
                        final String first = uri.substring(0, index);
                        final String second = uri.substring(index, index + Search.len());
                        final String third = uri.substring(index + Search.len());
                        this.append(first, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        this.append(second, new SimpleTextAttributes(new Color(255, 223, 128), new Color(255, 223, 128), new Color(255, 223, 128), SimpleTextAttributes.STYLE_SEARCH_MATCH));
                        this.append(third, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        Search.addMatchIndex(row);
                    } else {
                        this.append(uri, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        Search.removeMatchIndex(row);
                    }
                } else {
                    this.append(uri, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    Search.cleanMatchIndex();
                }
            }
            this.append("  " + node.getVirtualFile().getName(), SimpleTextAttributes.GRAYED_ATTRIBUTES);
        } else if (value instanceof RootTreeNode) {
            RootTreeNode rootNode = (RootTreeNode) value;
            // debugger
            // this.append(Search.getMatchIndexesString(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
            if (!Search.isNull()) {
                this.append(" [Search for: ", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                this.append(Search.content(), SimpleTextAttributes.REGULAR_ATTRIBUTES, SimpleTextAttributes.STYLE_OPAQUE);
                this.append("]   ", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
            }
            this.append(rootNode.getRootName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES, true);
            this.append("   " + project.getBasePath(), SimpleTextAttributes.GRAYED_ATTRIBUTES);
            this.append("  (" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rootNode.getUpdateTime()) + ")", SimpleTextAttributes.GRAYED_ATTRIBUTES );
            this.append("    " + rootNode.getPluginInfo(), SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES);
        } else if (value instanceof ServiceMethodRefTreeNode) {
            ServiceMethodRefTreeNode signNode = (ServiceMethodRefTreeNode) value;
            this.append(signNode.getServiceName(), SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES);
            String signatureName = signNode.getMethodName();
            if (signatureName != null) {
                this.append("." + signatureName, SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
        }
    }
}
