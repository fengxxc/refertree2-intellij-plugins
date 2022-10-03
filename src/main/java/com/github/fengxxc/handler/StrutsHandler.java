package com.github.fengxxc.handler;

import com.github.fengxxc.model.RtTreeNode;
import com.github.fengxxc.model.StrutsTagTreeNode;
import com.github.fengxxc.util.IntellijUtil;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrutsHandler {
    public static RtTreeNode[] parseStrutsCfg(PsiDirectory rootDirectory) throws FileNotFoundException {

        // PsiFile strutsFile = rootDirectory.findSubdirectory("resources").findFile("struts.xml");
        PsiFile strutsFile = IntellijUtil.findFile(rootDirectory, "resources/struts.xml", "/");
        // System.out.println(strutsFile.getName());

        RtTreeNode[] strutsTagTreeNodes = getPackageInfos(rootDirectory, strutsFile);
        return strutsTagTreeNodes;
    }

    private static RtTreeNode[] getPackageInfos(PsiDirectory rootDirectory, PsiFile strutsFile) throws FileNotFoundException {
        if (strutsFile == null) {
            return new StrutsTagTreeNode[0];
        }
        XmlFile xmlFile = (XmlFile) strutsFile;
        XmlTag strutsTag = xmlFile.getDocument().getRootTag();
        // System.out.println(strutsTag);
        XmlTag[] packages = strutsTag.findSubTags("package");
        RtTreeNode[] res = new StrutsTagTreeNode[packages.length];
        Map<String, PsiJavaFile> actionAnnotation2ClassMap = findAllActionAnnotation2ClassMap(rootDirectory);
        for (int i = 0; i < packages.length; i++) {
            res[i] = parsePackage(rootDirectory, packages[i], strutsFile.getVirtualFile(), actionAnnotation2ClassMap);
        }

        XmlTag[] includeTags = strutsTag.findSubTags("include");
        for (int i = 0; i < includeTags.length; i++) {
            String file = "resources/" + includeTags[i].getAttributeValue("file");
            PsiFile subStrutsFile = IntellijUtil.findFile(rootDirectory, file, "/");
            if (subStrutsFile == null) {
                continue;
            }
            RtTreeNode[] subStrutsTagTreeNodes = getPackageInfos(rootDirectory, subStrutsFile);
            StrutsTagTreeNode[] tmp = new StrutsTagTreeNode[res.length + subStrutsTagTreeNodes.length];
            System.arraycopy(res, 0, tmp, 0, res.length);
            System.arraycopy(subStrutsTagTreeNodes, 0, tmp, res.length, subStrutsTagTreeNodes.length);
            res = tmp;
        }
        return res;
    }

    private static RtTreeNode parsePackage(PsiDirectory rootDirectory, XmlTag packagez, VirtualFile virtualFile, Map<String, PsiJavaFile> actionAnnotation2ClassMap) throws FileNotFoundException {
        RtTreeNode packageTreeNode = new StrutsTagTreeNode(StrutsTagTreeNode.NODETYPE_PACKAGE)
                .putUserObj(StrutsTagTreeNode.PACKAGE_NAME, packagez.getAttributeValue("name"))
                .putUserObj(StrutsTagTreeNode.PACKAGE_EXTENDS, packagez.getAttributeValue("extends"))
                .putUserObj(StrutsTagTreeNode.PACKAGE_NAMESPACE, packagez.getAttributeValue("namespace"))
                .setVirtualFile(virtualFile)
                .setTextOffset(packagez.getTextOffset());
        XmlTag[] actions = packagez.findSubTags("action");
        for (int i = 0; i < actions.length; i++) {
            XmlTag action = actions[i];
            RtTreeNode actionTreeNode = parseAction(rootDirectory, action, virtualFile, actionAnnotation2ClassMap);
            packageTreeNode.add(actionTreeNode);
        }
        return packageTreeNode;
    }

    private static RtTreeNode parseAction(PsiDirectory rootDirectory, XmlTag action, VirtualFile virtualFile, Map<String, PsiJavaFile> actionAnnotation2ClassMap) throws FileNotFoundException {
        XmlTag[] results = action.findSubTags("result");
        String actionClass = action.getAttributeValue("class");
        String actionName = action.getAttributeValue("name");
        String actionMethod = action.getAttributeValue("method");
        RtTreeNode actionTreeNode = new StrutsTagTreeNode(StrutsTagTreeNode.NODETYPE_ACTION)
                .putUserObj(StrutsTagTreeNode.ACTION_NAME, actionName)
                .putUserObj(StrutsTagTreeNode.ACTION_CLASS, actionClass)
                .putUserObj(StrutsTagTreeNode.ACTION_METHOD, actionMethod)
                .setVirtualFile(virtualFile)
                .setTextOffset(action.getTextOffset());
        // add results
        for (int j = 0; j < results.length; j++) {
            XmlTag result = results[j];
            PsiFile jspFile = IntellijUtil.findFile(rootDirectory, "webapp" + result.getValue().getText(), "/");
            VirtualFile jspVFile = jspFile == null ? null : jspFile.getVirtualFile();
            actionTreeNode.add(
                    new StrutsTagTreeNode(StrutsTagTreeNode.NODETYPE_RESULT)
                            .putUserObj(StrutsTagTreeNode.RESULT_NAME, result.getAttributeValue("name"))
                            .putUserObj(StrutsTagTreeNode.RESULT_TEXT, result.getValue().getText())
                            .setVirtualFile(jspVFile)
                            .setTextOffset(0)
            );
        }
        // add methods
        if (actionMethod == null || "".equals(actionMethod)) {
            return actionTreeNode;
        }
        if (!actionAnnotation2ClassMap.containsKey(actionClass)) {
            return actionTreeNode;
        }
        PsiJavaFile actionJavaFile = actionAnnotation2ClassMap.get(actionClass);
        System.out.println("actionJavaFile.getVirtualFile().getPath() = " + actionJavaFile.getVirtualFile().getPath());
        String methodRegExp = actionMethod.replaceAll("\\{\\d+\\}", ".*");
        for (PsiClass ac : actionJavaFile.getClasses()) {
            for (PsiMethod acMethod : ac.getMethods()) {
                String acMethodName = acMethod.getName();
                // String acMethodSignText = acMethod.getText().replaceFirst("\\{.*\\}", "");
                String acMethodSignText = acMethodName + "[]";
                Pattern methodPat = Pattern.compile("(.*@.+\\(.*\\)\\n+)*(.+)(\\{[\\s\\S]*\\}[\\s\\S]*)");
                Matcher matcher = methodPat.matcher(acMethod.getText());
                if (matcher.find()) {
                    acMethodSignText = matcher.group(2);
                }

                if (Pattern.compile(methodRegExp).matcher(acMethodName).find()) {
                    System.out.println("    acMethodName = " + acMethodName);
                    actionTreeNode.add(
                            new StrutsTagTreeNode(StrutsTagTreeNode.NODETYPE_METHOD)
                                    .putUserObj(StrutsTagTreeNode.METHOD_NAME, acMethodName)
                                    .putUserObj(StrutsTagTreeNode.METHOD_SIGN_TEXT, acMethodSignText)
                                    .setVirtualFile(actionJavaFile.getVirtualFile())
                                    .setTextOffset(acMethod.getTextOffset())
                    );
                }
            }
        }
        return actionTreeNode;
    }

    private static Map<String, PsiJavaFile> findAllActionAnnotation2ClassMap(PsiDirectory rootDirectory) {
        HashMap<String, PsiJavaFile> res = new HashMap<>();
        GlobalSearchScope scope = GlobalSearchScope.projectScope(rootDirectory.getProject());
        PsiFileSystemItem[] strurs2Dire = FilenameIndex.getFilesByName(rootDirectory.getProject(), "struts2", scope, true);
        for (PsiFileSystemItem sd : strurs2Dire) {
            // System.out.println("strurs2Dire = " + sd.getVirtualFile().getPath());
            if (sd.isDirectory()) {
                PsiDirectory _sd = (PsiDirectory) sd;
                PsiFile[] actionClassFiles = _sd.getFiles();
                for (PsiFile acf : actionClassFiles) {
                    if (!StdFileTypes.JAVA.equals(acf.getFileType())) continue;
                    // System.out.println("acf.getVirtualFile().getPath() = " + acf.getVirtualFile().getPath());
                    String text = acf.getText();
                    Matcher matcher = Pattern.compile("(.*@Controller\\(\\s*\")(.*)(\"\\s*\\).*)").matcher(text);
                    if (!matcher.find()) continue;
                    String controllerVal = matcher.group(2);
                    if (controllerVal == null || "".equals(controllerVal)) continue;
                    PsiJavaFile _acf = (PsiJavaFile) acf;
                    res.put(controllerVal, _acf);
                    // System.out.println("    key: " + controllerVal + ", val: " + _acf.getName());
                }
            }
        }
        return res;
    }
}
