package com.github.fengxxc.listener;

import com.github.fengxxc.Search;
import com.intellij.ui.treeStructure.Tree;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 键盘事件
 * @author fengxxc
 * @date 2022-09-29
 */
public class RtKeyListener implements KeyListener {
    private Tree tree;

    public RtKeyListener(Tree tree) {
        this.tree = tree;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        if (keyCode == 27) {
            // Esc
            Search.clean();
        } else if (keyCode == 8) {
            // Backspace
            Search.backspace();
        } else if ((65 <= keyCode && keyCode <= 90)
                || keyCode == 45
                || (47 <= keyCode && keyCode <= 57)
                || (96 <= keyCode && keyCode <= 107)
                || (109 <= keyCode && keyCode <=111))
        {
            Search.input(e.getKeyChar());
        }
        tree.updateUI();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        tree.clearSelection();
        tree.addSelectionRow(Search.getFirstMatchIndex());
        tree.scrollRowToVisible(Search.getFirstMatchIndex());
    }
}
