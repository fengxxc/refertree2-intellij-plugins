package com.github.fengxxc.util;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ReflectionUtil;

import javax.swing.*;

/**
 * @author fengxxc
 * @date 2020-10-30
 */
public class IconUtil {
//    public static final Icon STRUTS = IconLoader.getIcon("/icons/struts-logo.svg");
    public static final Icon STRUTS = IconLoader.getIcon("/icons/struts-logo.svg", ReflectionUtil.getGrandCallerClass());

}
