package com.yintech.business.replace;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A->B
 * ------------------------------------------------------------
 * 1. A.class                      -> B.class
 * 2. [A class]                    -> [B class]
 * 3. NSStringFromClass(A.class)   -> NSStringFromClass(B.class)
 * 4. NSStringFromClass([A class]) -> NSStringFromClass([B class])
 * 5. @interface A                 -> @interface B
 * 6. @implementation A            -> @implementation B
 * 7. @interface A (xxx)           -> @interface B (xxx)
 * 8. @implementation A (xxx)      -> @implementation B (xxx)
 * 9. #import "A.h"                -> #import "B.h"
 * 10. #import <xxx/A.h>           -> #import <xxx/B.h>
 * 11. A.xxx                       -> B.xxx
 * 12. [A xxx]                     -> [B xxx]
 * 13. A *                         -> B *
 * 14. : A                         -> : B
 * 15. #import "A+xxx.h"           -> #import "B+xxx.h"
 * 16. #import <xxx/A+yyy.h>       -> #import <xxx/A+yyy.h>
 *
 * TODO://@"A" ??
 *
 * PersonViewController
 * CustomPersonViewController
 */
public class CodeReplaceTest {
    //1. A.class -> B.class
    @Test
    public void test1() {
        System.out.println("1");
        String str =  CodeReplace.replaceInLine1("NSString *s = AAA.class; BAAA.class ", "AAA", "BBB");
        System.out.println(str);
        Assertions.assertEquals("NSString *s = BBB.class; BAAA.class ", "NSString *s = BBB.class; BAAA.class ");
    }
    // 2. [A class] -> [B class]
    @Test
    public void test2() {
        System.out.println("2");
        String str =  CodeReplace.replaceInLine2("NSString *s = [AAA class]; [AAA class]", "AAA", "BBB");
        System.out.println(str);
    }
    // 3. NSStringFromClass(A.class) -> NSStringFromClass(B.class)
    @Test
    public void test3() {
        System.out.println("3");
        String str =  CodeReplace.replaceInLine3("NSString *s = NSStringFromClass(AAA.class); NSStringFromClass(AAA.class)", "AAA", "BBB");
        System.out.println(str);
    }
    // 4. NSStringFromClass([A class]) -> NSStringFromClass([B class])
    @Test
    public void test4() {
        System.out.println("4");
        String str =  CodeReplace.replaceInLine4("NSString *s = NSStringFromClass([AAA class]); NSStringFromClass([AAA class])", "AAA", "BBB");
        System.out.println(str);
    }
    // 5. @interface A -> @interface B
    @Test
    public void test5() {
        System.out.println("5");
        String str =  CodeReplace.replaceInLine5("  @interface AAA{  @interface AAAB { @interface AAA<UITableViewDelegate, NSObject>", "AAA", "BBB");
        System.out.println(str);
    }
    // 6. @implementation A -> @implementation B
    @Test
    public void test6() {
        System.out.println("6");
        String str =  CodeReplace.replaceInLine6("  @implementation AAA{  @implementation AAAB @implementation BAAA { @implementation AAA<UITableViewDelegate, NSObject>", "AAA", "BBB");
        System.out.println(str);
    }
    // 7. @interface A (xxx) -> @interface B (xxx)
    @Test
    public void test7() {
        System.out.println("7");
        //String str =  CodeReplace.replaceInLine7("  @interface AAA    (xxx) @interface AAA (xxx)<UITableViewDelegate> @interface AAA(xxx)  @interface BAAA(xxx)  @interface AAAB(xxx)", "AAA", "BBB");
        String str =  CodeReplace.replaceInLine7("@interface Dog (util)", "Dog", "Lion");
        System.out.println(str);
    }
    // 8. @implementation A (xxx) -> @implementation B (xxx)
    @Test
    public void test8() {
        System.out.println("8");
        String str =  CodeReplace.replaceInLine8("  @implementation AAA    (xxx) @implementation AAA (xxx)<UITableViewDelegate> @implementation AAA(xxx)  @implementation BAAA(xxx)  @implementation AAAB(xxx)", "AAA", "BBB");
        System.out.println(str);
    }
    // 9. #import "A.h" -> #import "B.h"
    @Test
    public void test9() {
        System.out.println("9");
        String str =  CodeReplace.replaceInLine9("#import \"AAA.h\" #import \"BAAA.h\" #import \"AAAB.h\"", "AAA", "BBB");
        System.out.println(str);
    }
    // 10. #import <XXX/A.h> -> #import <XXX/B.h>
    @Test
    public void test10() {
        System.out.println("10");
        String str =  CodeReplace.replaceInLine10("#import <XXX/AAA.h> #import <XXX/AAAA.h>", "AAA", "BBB");
        System.out.println(str);
    }
    // 11. A.xxx -> B.xxx
    @Test
    public void test11() {
        System.out.println("11");
        String str =  CodeReplace.replaceInLine11("BAAA.name = AAA.name= BAAA.name = AAAB.block = ", "AAA", "BBB");
        System.out.println(str);
    }
    //12. [A xxx] -> [B xxx]
    @Test
    public void test12() {
        System.out.println("12");
        String str =  CodeReplace.replaceInLine12("[BAAA setName [AAA name [AAA name [AAAB block:", "AAA", "BBB");
        System.out.println(str);
    }
    //13. 13. A * -> B *
    @Test
    public void test13() {
        System.out.println("13");
        String str =  CodeReplace.replaceInLine13("AAA* a = AAA.new AAA * AAA  ** BAAA * AAAB* CC", "AAA", "BBB");
        System.out.println(str);
    }

    //14.14. : A -> : B
    @Test
    public void test14() {
        System.out.println("14");
        //String str =  CodeReplace.replaceInLine14(" Hello:AAA Hello:AAA  Pe Hello:AAAAAA Person : AAA Cat:BAAA Dog:AAAB", "AAA", "BBB");
        String str =  CodeReplace.replaceInLine14("@interface Hello:AAA", "AAA", "BBB");
        System.out.println(str);
    }
    @Test
    public void test15() {
        System.out.println("15");
        String str =  CodeReplace.replaceInLine15("#import \"Dog+util.h\"", "Dog", "Lion");
        System.out.println(str);
    }

    @Test
    public void test16() {
        //16. #import <xxx/A+yyy.h>       -> #import <xxx/A+yyy.h>
        System.out.println("16");
        String str = CodeReplace.replaceInLine16("#import <xxx/Dog+util.h>", "Dog", "Lion");
        Assertions.assertEquals("#import <xxx/Lion+util.h>", str);
    }
}
