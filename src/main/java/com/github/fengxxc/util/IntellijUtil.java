package com.github.fengxxc.util;

import com.github.fengxxc.model.RtTreeNode;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

public class IntellijUtil {
    public static PsiFile findFile(PsiDirectory rootDirectory, String deepPathName, String separator) throws FileNotFoundException {
        if (deepPathName.startsWith(separator))
            return findFile(rootDirectory, deepPathName.substring(1), separator);

        String[] dirOrFiles = deepPathName.split(separator);
        PsiDirectory tmp = rootDirectory;
        for (int i = 0; i < dirOrFiles.length - 1; i++) {
            PsiDirectory find = tmp.findSubdirectory(dirOrFiles[i]);
            if (find == null) {
                // throw new FileNotFoundException(tmp.getVirtualFile().getPath() + "下未找到目录" + dirOrFiles[i]);
                continue;
            }
            tmp = find;
        }
        PsiFile res = tmp.findFile(dirOrFiles[dirOrFiles.length - 1]);
        if (res == null) {
            // throw new FileNotFoundException(tmp.getVirtualFile().getPath() + "下未找到文件" + dirOrFiles[dirOrFiles.length - 1]);
        }
        return res;
    }

    public static void openAndFixedInFile(Project project, RtTreeNode node) {
        VirtualFile virtualFile = node.getVirtualFile();
        if (virtualFile == null) {
            return;
        }
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile);
        Editor editor = FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true);
        int textOffset = node.getTextOffset();
        // 光标定位
        CaretModel caretModel = editor.getCaretModel();
        caretModel.moveToOffset(textOffset);
        SelectionModel selectionModel = editor.getSelectionModel();
        selectionModel.selectLineAtCaret();
    }

    public static void scrollTreeFromPsiFile(PsiFile psiFile, Editor editor, Tree tree) {
        if (psiFile == null || editor == null) {
            return;
        }
        final CaretModel caretModel = editor.getCaretModel();
        final int curOffset = caretModel.getOffset();
        final VirtualFile virtualFile = psiFile.getVirtualFile();
        System.out.println(virtualFile.getPath());

        tree.clearSelection();
        RtTreeNode rootNode = (RtTreeNode) (tree.getModel()).getRoot();
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            RtTreeNode node = (RtTreeNode) rootNode.getChildAt(i);
            if (node.getAbsFilePath().equals(virtualFile.getPath()) && node.getSignCodeStart() <= curOffset && curOffset < node.getSignCodeEnd()) {
                System.out.println("find: " + node.getAbsFilePath());
                tree.requestFocus();
                int row = node.getIndexInLevel() + 1;
                if (tree.isCollapsed(row)) {
                    tree.expandRow(0);
                }
                tree.scrollRowToVisible(row);
                tree.addSelectionRow(row);
                break;
            }
        }
    }
}
