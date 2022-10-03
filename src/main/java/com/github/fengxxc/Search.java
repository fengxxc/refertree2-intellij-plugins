package com.github.fengxxc;

import java.util.BitSet;

/**
 * @author fengxxc
 * @date 2022-09-28
 */
public class Search {
    private static String content;
    private static BitSet matchIndexes = new BitSet();

    public static String content() {
        return isNull() ? "" : Search.content;
    }

    public static void input(char text) {
        if (isNull()) {
            Search.content = text + "";
        } else {
            Search.content += text;
        }
    }

    public static void backspace() {
        if (isNull() || len() == 0) {
            return;
        }
        Search.content = Search.content.substring(0, Search.content().length()-1);
    }

    public static void clean() {
        Search.content = null;
    }

    public static boolean isNull() {
        return Search.content == null;
    }

    public static int len() {
        return Search.content().length();
    }

    public static String getMatchIndexesString() {
        return Search.matchIndexes.toString();
    }

    public static int getFirstMatchIndex() {
        return Search.matchIndexes.nextSetBit(0);
    }

    public static void addMatchIndex(int index) {
        Search.matchIndexes.set(index);
    }

    public static void removeMatchIndex(int index) {
        Search.matchIndexes.set(index, false);
    }

    public static void cleanMatchIndex() {
        Search.matchIndexes.clear();
    }
}
