package com.yintech.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class StringUtil {
    public static String getLastPathComponent(String originPath) {
        String path = originPath;
        if (path == null || path.length() == 0) {
            return null;
        }
        String newPath = null;
        if (path.endsWith("/")) {
            newPath = path.substring(0, path.length()-1);
        } else {
            newPath = path;
        }
        int lastIndex = newPath.lastIndexOf("/");
        String lastComponent = newPath.substring(lastIndex+1, newPath.length());
        return lastComponent;
    }
    @Test
    public void testGetLastPathComponent() {
        Assertions.assertEquals("YTXUtil", StringUtil.getLastPathComponent("/Users/daliu/Desktop/job/Modules/ytxutil/YTXUtil"));
    }

    public static String removeLastPathComponent(String originPath) {
        String path = originPath;
        if (path == null || path.length() == 0) {
            return null;
        }
        if (!path.contains("/") || path.equals("/")) {
            return path;
        }

        String newPath = null;
        if (path.endsWith("/")) {
            newPath = path.substring(0, path.length()-1);
        } else {
            newPath = path;
        }
        int lastIndex = newPath.lastIndexOf("/");
        newPath = newPath.substring(0, lastIndex) + "/";
        return newPath;
    }

    public static String appendPathComponent(String originPath, String appendComponent) {
        String path = originPath;
        if (path == null
                || path.length() == 0
                || appendComponent == null
                || appendComponent.length() == 0) {
            return null;
        }

        String newPath = path;
        if (!newPath.endsWith("/")) {
            newPath = newPath + "/";
        }
        if (appendComponent.startsWith("/")) {
            appendComponent = appendComponent.substring(1);
        }
        return newPath + appendComponent;
    }

    /**
     * 判断字符串是否有效， 如果为null或只有空字符，视为无效
     * @param str 字符串
     * @return 是否有效
     */
    public static boolean isValidString(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static String safeString(String str) {
        if (isValidString(str)) {
            return str;
        }
        return "";
    }

    /**
     * 获取子字符串（注：包括起始字符串）
     * @param src 原字符串
     * @param c 起始字符
     * @return subStringFromIncludeChar("AAA+Xxx.m", "+") ==> AAA+Xxx.m -> +Xxx.m
     */
    public static String subStringFromIncludeChar(String src, String c) {
        if (src == null || src.length() == 0 || !src.contains(c)) {
            return "";
        }
        return src.substring(src.indexOf(c));
    }

    /**
     * 修改字符串前缀
     * @param str 原字符串
     * @param srcPrefix 原前缀
     * @param destPrefix 新前缀
     * @return 修改前缀扣的字符串
     */
    public static String getChangePrefixStr(String str, String srcPrefix, String destPrefix) {
        if (!isValidString(str) ||
                !isValidString(srcPrefix) ||
                !isValidString(destPrefix) ||
                !str.trim().startsWith(srcPrefix)) {
            return null;
        }
        return destPrefix + subStringFromNotIncludeStr(str, srcPrefix);
    }
    @Test
    public void test_getChangePrefixStr() {
        Assertions.assertEquals(getChangePrefixStr("YTXUtil", "YTX", "TTT"), "TTTUtil");
    }

    /**
     * 获取子字符串（注：不包括起始字符串）
     * @param src 原字符串
     * @param c 起始字符
     * @return subStringFromIncludeChar("AAA+Xxx.m", "+") ==> AAA+Xxx.m -> +Xxx.m
     */
    public static String subStringFromNotIncludeChar(String src, String c) {
        if (src == null || src.length() == 0 || !src.contains(c)) {
            return "";
        }
        if (src.indexOf(c) != -1 && src.length() > src.indexOf(c) + 1) {
            return src.substring(src.indexOf(c)+1);
        } else {
            return "";
        }
    }

    // YTXRobotIntroduceViewModel:NSObject -> YTXRobotIntroduceViewModel:
    public static String subStringToIncludeString(String src, String c) {
        if (src == null || src.length() == 0 || !src.contains(c)) {
            return "";
        }
        if (src.indexOf(c) != -1 && src.length() > src.indexOf(c) + 1) {
            return src.substring(src.indexOf(c)+1);
        }
        return null;
    }

    // For formatter log
    // https://stackoverflow.com/questions/6000810/printing-with-t-tabs-does-not-result-in-aligned-columns
    public static String rPad(String str, int finalLength) {
        if (str == null) return null;
        return (str + "                                                                                                                                                                                           " // typically a sufficient length spaces string.
        ).substring(0, finalLength);
    }

    /**
     * 获取子字符串（注：包括起始字符串）
     * @param src 原字符串
     * @param sub 起始字符
     * @return 截取后的字符串
     */
    public static String subStringFromIncludeStr(String src, String sub) {
        if (src == null || src.length() == 0 || !src.contains(sub)) {
            return "";
        }
        int index = src.indexOf(sub);
        return src.substring(index);
    }

    /**
     * 获取子字符串（注：包括起始字符串）
     * @param src 原字符串
     * @param sub 起始字符
     * @return 截取后的字符串
     */
    public static String subStringFromNotIncludeStr(String src, String sub) {
        if (src == null || src.length() == 0 || !src.contains(sub)) {
            return "";
        }
        int index = src.indexOf(sub);
        return src.substring(index+sub.length());
    }

    /**
     * 获取OC类或协议名
     * @param line 一行代码
     * @return 类或协议的名字，比如：@interface Person -> Person   @protocol IPerson -> IPerson
     */
    public static String getOCClassOrProtocolName(String line) {
        if (line == null || line.trim().length() == 0 || isComment(line)) {
            return null;
        }
        String[] strArr = line.trim().split(" ");
        if (strArr.length < 2) {
            return null;
        }
        String first = strArr[0];
        String second = strArr[1].trim();
        if (first.trim().equals("@interface")) {
            if (second.contains(":")) { //YTXEmotionCollectionCell()
                return second.split(":")[0].trim();
            }

            return second.replaceAll("[^A-Za-z_]+", " ").split(" ")[0];

//            return second;
        } else if (first.trim().equals("@protocol")) {
            //@protocol YTXUtil
            //@protocol YTXUtil<NSObject>
            //@protocol Hello//World
            //@protocol Hello/**
            return second.replaceAll("[^A-Za-z_]+", " ").split(" ")[0];
        }
        return null;
    }

    @Test
    public void test_getOCClassOrProtocolName() {
        //@interface
        //String line = "@interface YTXUtil : NSObject";
        //String line = "@interface YTXUtil:NSObject";
        String line = "@interface YTXUtil    :NSObject";
        String result = getOCClassOrProtocolName(line);
        Assertions.assertEquals(result, "YTXUtil");
        line = "//@interface YTXUtil    :NSObject";
        Assertions.assertEquals(getOCClassOrProtocolName(line), null);
        line = "@interface YTXEmotionCollectionCell()";
        Assertions.assertEquals(getOCClassOrProtocolName(line), "YTXEmotionCollectionCell");

        //@protocol
        //@interface YTXUtil(XXX)
        line = "@protocol YTXUtilProtocol <NSObject>";
        Assertions.assertEquals(getOCClassOrProtocolName(line), "YTXUtilProtocol");
        line = "@protocol YTXUtilProtocol<NSObject>";
        Assertions.assertEquals(getOCClassOrProtocolName(line), "YTXUtilProtocol");
        line = "/*@protocol YTXUtilProtocol<NSObject>";
        Assertions.assertEquals(getOCClassOrProtocolName(line), null);
        line = "  @protocol YTXUtilProtocol         ";
        Assertions.assertEquals(getOCClassOrProtocolName(line), "YTXUtilProtocol");

        Set<String> set = new HashSet<>();
        set.add("Hello");
        set.add("Hello");
        for (String s : set) {
            System.out.println(s);
        }
        Assertions.assertEquals(set.size(), 1);
    }

    /**
     * 是否为代码注释行
     */
    public static boolean isComment(String line) {
        if (line == null || line.trim().length() == 0) return false;
        return line.startsWith("/");
    }

    @Test
    public void testGetClassName() {
        //String[] arr = "@interface YTXRobotIntroduceViewModel : YXWBaseViewModel\n".split(" ");
        //String[] arr = "@interface YTXRobotIntroduceViewModel(YXWBaseViewModel)".split(" ");
        String[] arr = "@interface YTXRobotIntroduceViewModel:NSObject".split(" ");
        Assertions.assertEquals(getClassName(arr, 1), "YTXRobotIntroduceViewModel");
    }

    private static String getClassName(String[] arr, int startIndex) {
        if (arr.length <= startIndex) {
            return null;
        }
        for (int i = startIndex; i < arr.length; i++) {
            String str = arr[i].trim();
            if (str.length() == 0 || str.contains("@interface")) continue;
            if (str.contains("(")) return null; // category
            if (str.endsWith(":")) {
                return str.substring(0, str.length()-2);
            }
            return str.trim();
        }
        return null;
    }

    private static String getProtocolName(String[] arr, int startIndex) {
        if (arr.length <= startIndex) {
            return null;
        }

        return null;
    }

    @Test
    public void testSubStringFromIncludeChar() {
        Assertions.assertEquals(subStringFromIncludeChar("AAA+Xxx.m", "+"), "+Xxx.m");
        Assertions.assertEquals(subStringFromIncludeChar("AAA+", "+"), "+");
        Assertions.assertEquals(subStringFromIncludeChar("AAA", "+"), "AAA");
    }

    @Test
    public void testSubStringFromNotIncludeChar() {
        Assertions.assertEquals(subStringFromNotIncludeChar("AAA+Xxx.m", "+"), "Xxx.m");
        System.out.println(subStringFromNotIncludeChar("AAA+", "+"));
        Assertions.assertEquals(subStringFromNotIncludeChar("AAA+", "+"), "");
        Assertions.assertEquals(subStringFromNotIncludeChar("AAA", "+"), "");
    }

    @Test
    public void testSubStringFromIncludeStr() {
        Assertions.assertEquals(subStringFromIncludeStr("YTXUtils.h", "YTX"), "YTXUtils.h");
    }

    @Test
    public void testSubStringFromNotIncludeStr() {
        Assertions.assertEquals(subStringFromNotIncludeStr("YTXUtils.h", "YTX"), "Utils.h");
        Assertions.assertEquals(subStringFromNotIncludeStr("YTX.h", "YTX"), ".h");
        Assertions.assertEquals(subStringFromNotIncludeStr("YTX", "YTX"), "");
    }

    @Test
    public void testStartWith() {
        String str = "YTXUtil";
        Assertions.assertEquals(str.startsWith("YTXUtil"), true);
    }

    public static String replaceOccurrence(String originStr, String src, String dest) {
        if (originStr == null || src == null || dest == null || originStr.contains(src) == false) {
            return originStr;
        }
        return originStr.replaceAll(src, dest);
    }
    @Test
    public void testReplaceOccurrence() {
        Assertions.assertEquals(replaceOccurrence("Person *p = [[Person alloc] init];", "Person", "YTXPerson"), "YTXPerson *p = [[YTXPerson alloc] init];");
    }

//    public static String replaceInLine15(String line, String oldClsName, String newClsName) {
//        if (!line.contains(oldClsName)) return line;
//
//        String newLine = line;
//        String regex = "#import\\s+\"" + oldClsName + "\\+(\\w+)\\.h\"";
//        newLine = newLine.replaceAll(regex,  "#import \"" + newClsName + "\\+$1.h\"");
//        return newLine;
//    }

    public static void main(String[] args) {
        System.out.println(subStringFromIncludeChar("AAA+Xxx.m", "+"));

        String p1 = "/Users/A/B/C/icon_slices";
        String p2 = "Users/A/B/C/icon_slices";
        String p3 = "Users/A/B/C/icon_slices/";
        String p4 = "/Users/A/B/C/icon_slices/";

        System.out.println(removeLastPathComponent(p1));
        System.out.println(removeLastPathComponent(p2));
        System.out.println(removeLastPathComponent(p3));
        System.out.println(removeLastPathComponent(p4));

        System.out.println(appendPathComponent(p1, "/hello"));
        System.out.println(appendPathComponent(p2, "hello"));
        System.out.println(appendPathComponent(p3, "/hello"));
        System.out.println(appendPathComponent(p4, "hello"));
    }
}