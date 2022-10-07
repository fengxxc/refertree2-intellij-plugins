package com.github.fengxxc.listener;

import com.github.fengxxc.model.PlaceHolderTreeNode;
import com.github.fengxxc.model.ServiceSignTreeNode;
import com.github.fengxxc.model.SpringMvcTreeNode;
import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RtTreeWillExpandListener implements TreeWillExpandListener {
    private Project project;

    public RtTreeWillExpandListener(Project project) {
        this.project = project;
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        TreePath path = event.getPath();
        Object component = path.getLastPathComponent();
        if (component instanceof SpringMvcTreeNode) {
            SpringMvcTreeNode node = (SpringMvcTreeNode) component;
            if (node.getLevel() == 1) {
                String text = null;
                try {
                    text = VfsUtilCore.loadText(node.getVirtualFile());
                    // "\r\n" 应该当成一个字符，但实际会被当成两个字符，字符索引（光标定位）会出偏差，故去掉所有 \r
                    text = text.replace("\r", "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap<String, VirtualFile> serviceMap = new HashMap<>();
                int autowiredEndIndex = makeServiceMap(text, serviceMap);
                for (String serviceVar : serviceMap.keySet()) {
                    Matcher serviceSignMatcher = Pattern.compile("(" + serviceVar + "\\.)([A-Za-z0-9_\\.]+\\(.*\\))").matcher(text);
                    int findIdx = autowiredEndIndex;
                    while (serviceSignMatcher.find(findIdx)) {
                        boolean directRefer = false;
                        for (int i = 0; i < node.getChildCount(); i++) {
                            SpringMvcTreeNode child = (SpringMvcTreeNode) node.getChildAt(i);
                            if (child.getChildCount() == 1 && child.getChildAt(0) instanceof PlaceHolderTreeNode) {
                                child.removeAllChildren();
                            }
                            if (child.getSignCodeStart() <= serviceSignMatcher.start() && serviceSignMatcher.end() < child.getSignCodeEnd()) {
                                VirtualFile virtualFile = serviceMap.get(serviceVar);
                                ServiceSignTreeNode grandChild = new ServiceSignTreeNode(serviceSignMatcher.start()+":"+serviceSignMatcher.end() + virtualFile.getNameWithoutExtension(), serviceVar, serviceSignMatcher.group(2));
                                grandChild.setVirtualFile(virtualFile);
                                child.add(grandChild);
                                directRefer = true;
                                break;
                            }
                        }
                        if (!directRefer) {
                            // TODO
                        }
                        findIdx = serviceSignMatcher.end();
                    }
                }

                /*
                for (int i = 0; i < node.getChildCount(); i++) {
                    SpringMvcTreeNode child = (SpringMvcTreeNode) node.getChildAt(i);
                    String subText = text.substring(child.getSignCodeStart(), child.getSignCodeEnd());
                    for (String serviceVar : serviceMap.keySet()) {
                        int index = subText.indexOf(serviceVar);
                        if (index < 0) {
                            continue;
                        }
                        subText.contains(serviceVar + ".")
                    }
                }
                */
            }
        }
    }

    private int makeServiceMap(String text, Map<String, VirtualFile> serviceMap) {
        Matcher autowiredMatcher = Pattern.compile("(@Autowired[\\r\\n\\s]+)(\\S+)(\\s+)(.+);").matcher(text);
        // System.out.println("autowiredMatcher.find() = " + autowiredMatcher.find());
        int endIndex = -1;
        while (autowiredMatcher.find()) {
            System.out.println("autowiredMatcher = " + autowiredMatcher.group(2) + " " + autowiredMatcher.group(4));
            //
            PsiDirectory rootDirectory = IntellijUtil.getRootDirectory(project);
            final GlobalSearchScope scope = GlobalSearchScope.projectScope(rootDirectory.getProject());
            Collection<VirtualFile> serviceVirtualFiles = FilenameIndex.getVirtualFilesByName(autowiredMatcher.group(2) + ".java", scope);
            if (serviceVirtualFiles == null || serviceVirtualFiles.size() == 0) {
                continue;
            }
            VirtualFile virtualFile = serviceVirtualFiles.toArray(new VirtualFile[0])[0];
            System.out.println("virtualFile.getPath() = " + virtualFile.getPath());
            serviceMap.put(autowiredMatcher.group(4), virtualFile);
            endIndex = autowiredMatcher.end();
        }
        return endIndex;
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

    }

    public static void main(String[] args) {
        Matcher autowiredMatcher = Pattern.compile("(@Autowired[\\r\\n\\s]+)(.*);").matcher("@Autowired\r\n" +
                "    BaseExpertAccService baseExpertAccService;\n" +
                "    ");
        System.out.println("autowiredMatcher.find() = " + autowiredMatcher.find());
        System.out.println(autowiredMatcher.group());
    }
}
