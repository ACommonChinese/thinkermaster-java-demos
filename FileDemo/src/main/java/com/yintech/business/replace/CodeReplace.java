package com.yintech.business.replace;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

public class CodeReplace {
    public static void main(String[] args) {
        //http://tool.chinaz.com/regex/
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
//        test7();
//        test8();
//        test9();
//        test10();
//        test11();
//        test12();
//        test13();
//        test14();
//        test15();
    }

    public static String replaceInLine(String line, String oldClsName, String newClsName) {
        if (line == null ||  oldClsName == null ||  newClsName == null) {
            return null;
        }

        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        newLine = replaceInLine1(newLine, oldClsName, newClsName);
        newLine = replaceInLine2(newLine, oldClsName, newClsName);
        newLine = replaceInLine3(newLine, oldClsName, newClsName);
        newLine = replaceInLine4(newLine, oldClsName, newClsName);
        newLine = replaceInLine5(newLine, oldClsName, newClsName);
        newLine = replaceInLine6(newLine, oldClsName, newClsName);
        newLine = replaceInLine7(newLine, oldClsName, newClsName);
        newLine = replaceInLine8(newLine, oldClsName, newClsName);
        newLine = replaceInLine9(newLine, oldClsName, newClsName);
        newLine = replaceInLine10(newLine, oldClsName, newClsName);
        newLine = replaceInLine11(newLine, oldClsName, newClsName);
        newLine = replaceInLine12(newLine, oldClsName, newClsName);
        newLine = replaceInLine13(newLine, oldClsName, newClsName);
        newLine = replaceInLine14(newLine, oldClsName, newClsName);
        newLine = replaceInLine15(newLine, oldClsName, newClsName);
        newLine = replaceInLine16(newLine, oldClsName, newClsName);

        return newLine;
    }

    // NSString *s = AAA.class; BAAA.class "
    public static String replaceInLine1(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "\\s" + oldClsName + "\\.class";
        newLine = newLine.replaceAll(regex, " " + newClsName + ".class");
        regex = "[^\\w]" + oldClsName + "\\.class"; // [^\w]NAME\.class
        newLine = newLine.replaceAll(regex, newClsName + ".class");
        return newLine;
    }
    public static String replaceInLine2(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "\\[" + oldClsName + " class\\]";
        newLine = newLine.replaceAll(regex, "[" + newClsName + " class]");
        return newLine;
    }
    public static String replaceInLine3(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "NSStringFromClass" + "\\(" + oldClsName + ".class\\)";
        newLine = newLine.replaceAll(regex, "NSStringFromClass(" + newClsName + ".class)");
        return newLine;
    }
    public static String replaceInLine4(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        // NSStringFromClass\(\[AAA\s+class\]\)
        String regex = "NSStringFromClass" + "\\(\\[" + oldClsName + "\\s+class\\]\\)";
        newLine = newLine.replaceAll(regex, "NSStringFromClass([" + newClsName + " class])");
        return newLine;
    }
    public static String replaceInLine5(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "@interface\\s+" + oldClsName + "([^A-Za-z_])";
        //分组替换 https://blog.csdn.net/jiaobuchong/article/details/81257570
        //@interface AAA{  @interface AAAB { @interface AAA<UITableViewDelegate, NSObject>
        newLine = newLine.replaceAll(regex, "@interface " + newClsName + "$1");
        return newLine;
    }
    public static String replaceInLine6(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "@implementation\\s+" + oldClsName + "([^A-Za-z_])";
        newLine = newLine.replaceAll(regex, "@implementation " + newClsName + "$1");
        return newLine;
    }
    //@interface AAA    (xxx) @interface AAA (xxx)<UITableViewDelegate> @interface AAA(xxx)  @interface BAAA(xxx)  @interface AAAB(xxx)
    public static String replaceInLine7(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "@interface\\s+" + oldClsName + "\\s*\\(";
        newLine = newLine.replaceAll(regex, "@interface " + newClsName + " (");
        return newLine;
    }
    //@implementation A (xxx)  -> @implementation B (xxx)
    public static String replaceInLine8(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "@implementation\\s+" + oldClsName + "\\s*\\(";
        newLine = newLine.replaceAll(regex, "@implementation " + newClsName + " (");
        return newLine;
    }
    //#import "A.h" -> #import "B.h"
    public static String replaceInLine9(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "#import\\s+\"" + oldClsName + "\\.h\"";
        newLine = newLine.replaceAll(regex, "#import \"" + newClsName + ".h\"");
        return newLine;
    }
    //#import <xxx/A.h> -> #import <xxx/B.h>
    //需要分组替换
    public static String replaceInLine10(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "#import\\s+<(\\w+)/" + oldClsName + "\\.h>";
        newLine = newLine.replaceAll(regex, "#import <" + "$1/" + newClsName + ".h>");
        return newLine;
    }

    //BAAA.name = AAA.name= BAAA.name = AAAB.block =  // TO:
    //BAAA.name = BBB.name= BAAA.name = AAAB.block =
    public static String replaceInLine11(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "([^A-Za-z_])" + oldClsName + "\\.";
        newLine = newLine.replaceAll(regex, "$1" + newClsName + ".");
        return newLine;
    }
    //[BAAA setName [AAA name [BAAA name [AAAB block
    public static String replaceInLine12(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "\\[" + oldClsName + "\\s+";
        newLine = newLine.replaceAll(regex, "[" + newClsName + " ");
        return newLine;
    }
    //AAA* a = AAA.new AAA * AAA  * BAAA * AAAB* CC
    public static String replaceInLine13(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        //[^A-Za-z_](AAA\s*\*)
        String regex = "([^A-Za-z_])*" + oldClsName + "\\s*\\*";
        newLine = newLine.replaceAll(regex,  "$1" + newClsName + " *");
        return newLine;
    }
    //replaceInLine14
    // Hello:AAA Hello:AAA  Pe Hello:AAAAAA Person : AAA Cat:BAAA Dog:AAAB
    public static String replaceInLine14(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = ":\\s*" + oldClsName + "([^A-Aa-z_0123456789])*";
        newLine = newLine.replaceAll(regex,  ": " + newClsName + "$1");
        return newLine;
    }
    //#import "Dog+util.h"
    public static String replaceInLine15(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "#import\\s+\"" + oldClsName + "\\+(\\w+)\\.h\"";
        newLine = newLine.replaceAll(regex,  "#import \"" + newClsName + "\\+$1.h\"");
        return newLine;
    }
    // #import <xxx/A+yyy.h> -> #import <xxx/A+yyy.h>
    // Note: similar with replaceInLine10
    public static String replaceInLine16(String line, String oldClsName, String newClsName) {
        if (!line.contains(oldClsName)) return line;

        String newLine = line;
        String regex = "#import\\s+<(\\w+)/" + oldClsName + "\\+(\\w+)\\.h>";
        newLine = newLine.replaceAll(regex, "#import <" + "$1/" + newClsName + "\\+$2.h>");
        System.out.println(line);
        System.out.println(newLine);
        return newLine;

        // String str = CodeReplace.replaceInLine16("#import <xxx/Dog+util.h>", "Dog", "Lion");
    }
}