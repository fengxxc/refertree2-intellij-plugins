package com.github.fengxxc.handler;

import com.github.fengxxc.model.PlaceHolderTreeNode;
import com.github.fengxxc.model.RtTreeNode;
import com.github.fengxxc.model.SpringMvcTreeNode;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fengxxc
 * @date 2022-09-25
 */
public class SpringMvcHandler {
    public static RtTreeNode[] parse(PsiDirectory rootDirectory) {
        final List<SpringMvcTreeNode> res = new ArrayList<>();
        // rootDirectory.findSubdirectory("java").getFiles()
        final GlobalSearchScope scope = GlobalSearchScope.projectScope(rootDirectory.getProject());
        final Collection<VirtualFile> javaFiles = FilenameIndex.getAllFilesByExt(rootDirectory.getProject(), "java", scope);
        int indexInLevel = 0;
        for (VirtualFile javaFile : javaFiles) {
            String text;
            try {
                text = VfsUtilCore.loadText(javaFile);
                // "\r\n" 应该当成一个字符，但实际会被当成两个字符，字符索引（光标定位）会出偏差，故去掉所有 \r
                text = text.replace("\r", "");
            } catch (IOException e) {
                // e.printStackTrace();
                continue;
            }
            Matcher controllerMatcher = Pattern.compile(".*@[Rest]*Controller").matcher(text);
            if (!controllerMatcher.find()) {
                continue;
            }

            SpringMvcTreeNode controllerNode = new SpringMvcTreeNode(javaFile);
            controllerNode.setLevel(1);
            final int classIdx = text.indexOf("class");
            final Matcher reqMapMatcher = Pattern.compile("(@RequestMapping\\(\\s*)(.*)(\\s*\\))").matcher(text);
            // final Matcher reqMapMatcher = Pattern.compile("(@RequestMapping\\(\\s*)(.*)(\\s*\\))",Pattern.CASE_INSENSITIVE | Pattern.DOTALL).controllerMatcher(text);
            int matchIdx = 0;
            while (reqMapMatcher.find(matchIdx)) {
                System.out.println("    "+reqMapMatcher.group(2).toString());
                matchIdx = reqMapMatcher.end();
                // System.out.println("matchIdx = " + matchIdx);
                if (classIdx > matchIdx) {
                    /** 类注解 @RequestMapping */
                    String pref = parseValueFromRequestMapping(reqMapMatcher.group(2));
                    controllerNode.setUri(pref);
                } else {
                    /** 方法注解 @RequestMapping */
                    if (controllerNode.getChildCount() > 0 && controllerNode.getVirtualFile() == javaFile) {
                        controllerNode.getLastChild().setSignCodeEnd(reqMapMatcher.start());
                    }
                    Request request = parseRequestFromRequestMapping(reqMapMatcher.group(2));
                    String uri = /*controllerNode.getUri() +*/ request.Path;
                    SpringMvcTreeNode rtTreeNode = new SpringMvcTreeNode(uri, request.Method, reqMapMatcher.start(2));
                    rtTreeNode.setSignCodeStart((controllerNode.getChildCount() == 0 || controllerNode.getVirtualFile() != javaFile) ? controllerMatcher.start() : reqMapMatcher.start());
                    rtTreeNode.setVirtualFile(javaFile);
                    rtTreeNode.setIndexInLevel(indexInLevel++);
                    rtTreeNode.setLevel(2);
                    rtTreeNode.add(new PlaceHolderTreeNode());
                    controllerNode.add( rtTreeNode );
                }
            }
            if (controllerNode.getChildCount() > 0) {
                controllerNode.getLastChild().setSignCodeEnd(text.length());
            }
            res.add(controllerNode);
        }
        return res.toArray(new SpringMvcTreeNode[]{});
    }

    public static String parseValueFromRequestMapping(String obj) {
        final Pattern compile = Pattern.compile("(\")(.*)(\")");
        final Matcher matcher = compile.matcher(obj);
        if (!matcher.find()) {
            return "";
        }
        return matcher.group(2);
    }

    public static Request parseRequestFromRequestMapping(String obj) {
        Request request = new Request();
        Pattern valueCompile = Pattern.compile("(value\\s*=\\s*\")(.*)(\")");
        Matcher valueMatcher = valueCompile.matcher(obj);
        if (valueMatcher.find()) {
            request.Path = valueMatcher.group(2);
        }
        Pattern methodCompile = Pattern.compile("(method\\s*=\\s*RequestMethod\\.)([A-Za-z]*)(\\s*)");
        Matcher methodMatcher = methodCompile.matcher(obj);
        if (methodMatcher.find()) {
            request.Method = methodMatcher.group(2);
        }
        return request;
    }

    static class Request {
        public String Path = "";
        public String Method = "";
    }


}
