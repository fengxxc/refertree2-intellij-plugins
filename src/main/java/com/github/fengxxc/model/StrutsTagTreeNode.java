package com.github.fengxxc.model;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.HashMap;
import java.util.Map;

public class StrutsTagTreeNode extends RtTreeNode {
    public static final String NODETYPE_PACKAGE = "PACKAGE";
    public static final String NODETYPE_ACTION = "ACTION";
    public static final String NODETYPE_RESULT = "RESULT";
    public static final String NODETYPE_METHOD = "METHOD";

    public static final String PACKAGE_NAME = "package_name";
    public static final String PACKAGE_EXTENDS = "package_extends";
    public static final String PACKAGE_NAMESPACE = "package_namespace";

    public static final String ACTION_NAME = "action_name";
    public static final String ACTION_METHOD = "action_method";
    public static final String ACTION_CLASS = "action_class";

    public static final String RESULT_NAME = "result_name";
    public static final String RESULT_TEXT = "result_text";

    public static final String METHOD_NAME = "method_name";
    public static final String METHOD_SIGN_TEXT = "method_sign_text";

    public String NodeType = "";

    public StrutsTagTreeNode(String nodeType) {
        this.NodeType = nodeType;
        Map<String, Object> userObj = new HashMap<String, Object>();
        setUserObject(userObj);
    }

    public StrutsTagTreeNode putUserObj(String key, Object value) {
        Map<String, Object> userObj = (Map<String, Object>) super.getUserObject();
        userObj.put(key, value);
        super.setUserObject(userObject);
        return this;
    }

    public String getUserObjValueToString(String key) {
        return (String) getUserObjValue(key);
    }

    public Object getUserObjValue(String key) {
        return ((Map<String, Object>) super.getUserObject()).get(key);
    }



    public String toJsonTreeString() {
        StringBuffer sb = new StringBuffer();
        Map<String, Object> userObj = (Map<String, Object>) super.getUserObject();
        int tabCount = 0;
        if (NODETYPE_PACKAGE.equals(NodeType)) tabCount = 0;
        if (NODETYPE_ACTION.equals(NodeType))  tabCount = 4;
        if (NODETYPE_RESULT.equals(NodeType))  tabCount = 8;
        for (int i = 0; i < tabCount; i++) sb.append(" ");
        sb.append("" + NodeType + ": { ");
        userObj.forEach((k, v) -> {
            sb.append(k + ":" + v + ", ");
        });
        sb.append("\n");
        for (int i = 0; i < super.getChildCount(); i++) {
            StrutsTagTreeNode c = (StrutsTagTreeNode) super.getChildAt(i);
            sb.append(c.toString());
        }
        for (int i = 0; i < tabCount; i++) sb.append(" ");
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        // return toJsonTreeString();
        String res = "";
        switch (NodeType) {
            case NODETYPE_PACKAGE:
                res = this.getUserObjValueToString(PACKAGE_NAMESPACE);
                break;
            case NODETYPE_ACTION:
                res = this.getUserObjValueToString(ACTION_NAME);
                break;
            case NODETYPE_RESULT:
                String path = this.getUserObjValueToString(RESULT_TEXT);
                res = path == null ? "[null]" : path.substring(path.lastIndexOf('/') + 1);
                break;
            case NODETYPE_METHOD:
                res = this.getUserObjValueToString(METHOD_NAME);
                break;
            default:
                res = "[null]";
        }
        return res;
    }
}
