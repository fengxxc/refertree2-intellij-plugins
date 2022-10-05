package com.github.fengxxc.listener;

import com.github.fengxxc.Search;
import com.github.fengxxc.Singleton;
import com.intellij.ui.treeStructure.Tree;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 键盘事件
 * @author fengxxc
 * @date 2022-09-29
 */
public class RtKeyListener implements KeyListener {

    public RtKeyListener() {
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        Tree tree = Singleton.getTree();
        final int keyCode = e.getKeyCode();
        if (keyCode == 27) {
            // Esc
            Search.clean();
        } else if (keyCode == 8) {
            // Backspace
            Search.backspace();
        } else if (isLetterOrNumeral(keyCode))
        {
            Search.input(e.getKeyChar());
        }
        tree.updateUI();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!Search.isNull() && isLetterOrNumeral(e.getKeyCode())) {
            Tree tree = Singleton.getTree();
            tree.clearSelection();
            tree.addSelectionRow(Search.getFirstMatchIndex());
        }
    }

    private static boolean isLetterOrNumeral(int keyCode) {
        return (65 <= keyCode && keyCode <= 90)
                || keyCode == 45
                || (47 <= keyCode && keyCode <= 57)
                || (96 <= keyCode && keyCode <= 107)
                || (109 <= keyCode && keyCode <= 111);
    }
}
