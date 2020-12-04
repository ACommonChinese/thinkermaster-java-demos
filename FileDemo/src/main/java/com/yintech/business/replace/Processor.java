package com.yintech.business.replace;

import java.io.IOException;
import java.lang.*;
import com.yintech.util.FileUtil;
import com.yintech.util.NameSuffix;
import com.yintech.util.StringUtil;

import java.io.File;
import java.util.*;

interface IProcessor {
    /// 即将开始处理
    void onStart();
    /// 处理出现错误
    void onError(String message);
    /// 处理中，多次回调
    void onProcessing(String info);
    /// 处理完成
    void onFinish();
}

class Item {
    private String src;
    private String dest;
}

public class Processor {
    private IProcessor delegate;
    private EnumSet<ChangeType> changeTypes;
    private String inputText;
    File rootDir;
    private int changeFileNameSuccessCount = 0;
    private int changeFileNameFailCount = 0;
    private List<ReplaceItem> items;

    public int getChangeFileNameSuccessCount() {
        return changeFileNameSuccessCount;
    }
    public void setChangeFileNameSuccessCount(int changeFileNameSuccessCount) {
        this.changeFileNameSuccessCount = changeFileNameSuccessCount;
    }
    public int getChangeFileNameFailCount() {
        return changeFileNameFailCount;
    }
    public void setChangeFileNameFailCount(int changeFileNameFailCount) {
        this.changeFileNameFailCount = changeFileNameFailCount;
    }
    public IProcessor getDelegate() {
        return delegate;
    }
    public EnumSet<ChangeType> getChangeTypes() {
        return changeTypes;
    }
    public void setChangeTypes(EnumSet<ChangeType> changeTypes) {
        this.changeTypes = changeTypes;
    }
    public String getInputText() {
        return inputText;
    }
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
    public void setDelegate(IProcessor delegate) {
        this.delegate = delegate;
    }
    public File getRootDir() {
        return rootDir;
    }
    public void setRootDir(File rootDir) {
        this.rootDir = rootDir;
    }

    private void reset() {
        changeFileNameSuccessCount = 0;
        changeFileNameFailCount = 0;
    }

    public void start() {
        // 检错
        // -------------------------------------------------------------
        assert delegate != null : "delegate can't be null!";

        if (inputText == null || inputText.length() == 0) {
            Console.errorLine("替换的文本输入不合法，请重新输入！");
            return;
        }
        if (changeTypes.size() == 0) {
            Console.errorLine("请勾选checkbox");
            return;
        }
        if (rootDir == null) {
            delegate.onError("未选择要处理的目录");
            return;
        }

        delegate.onStart();

        // 只考虑 .h.m 文件
        // -------------------------------------------------------------
        String[] suffixArr = {".h", ".m"};
        Console.logLine("Start Searching...");
        //List<File> files = FileUtil.getAllFiles(rootDir.getAbsolutePath());
        List<File> files = FileUtil.getAllFilesWithSuffixInList(rootDir.getAbsolutePath(), suffixArr, false);

        // 文件名修改
        Console.logLine(".h .m文件:");
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            Console.logLine((i+1) + ". " + file.getName()); // + " full path: " + file.getAbsolutePath()
        }

        // 根据前缀输入寻找对应要修改的文件
        // inputText: YTX->TTT
        ReplaceItemParseResult parseResult = ReplaceItem.parseFromInputForFileNameChange(inputText, files); // 前缀
        this.items = parseResult.getItems();
        files = parseResult.getFiles();

        Console.logLine("================= 解析输入 =================");
        Console.logLine("目录：" + rootDir);

        Console.logLine("\n准备修改名字的文件清单：");
        for (ReplaceItem item : this.items) {
            Console.logLine(item.toString());
        }
        Console.logLine("\n");

        if (changeTypes.contains(ChangeType.FILE)) {
            Console.logLine("\n================= 文件名修改 =================");
            replaceFileName(files);
        }

        if (changeTypes.contains(ChangeType.CODE)) {
            Console.logLine("\n================= 代码修改 =================");
            // Re-get files, since file's name did changed
            files = FileUtil.getAllFilesWithSuffixInList(rootDir.getAbsolutePath(), suffixArr, false);
//            ReplaceItemParseResult result = ReplaceItem.parseFromInputForCodeChange(inputText, files);
//            List<ReplaceItem> items = result.getItems();
//            this.items = items;
//            for (int i = 0; i < items.size(); i++) {
//                for (ReplaceItem item : items) {
//                    Console.logLine(item.toString());
//                }
//            }
//
//            replaceCode(files);
            simpleReplaceCode(files);
        }

        Console.logLine("================= END =================");
    }

    public void printItems() {
        Console.logLine("begin print items");
        for (ReplaceItem item : items) {
            Console.logLine(item.toString());
        }
        Console.logLine("end print items");
    }

    /**
     * Step 1>：文件名修改
     *   AAA,BBB
     *   - AAA.h -> BBB.h
     *   - AAA.m -> BBB.m
     *   - AAA+XXX.h -> BBB+XXX.h
     *   - AAA+XXX.m -> BBB+XXX.m
     */
    public void replaceFileName(List<File> files) {
        List<ReplaceItem> items = this.items;
        if (files.size() == 0) {
            Console.error("未找到合法文件(.h, .m)");
            return;
        }
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String fileName = file.getName();
            File newFile = null;
            for (ReplaceItem item : items) {
                /// AAA.h --> BBB.h
                NameSuffix nameSuffix = NameSuffix.parse(fileName);
                if (nameSuffix.getName() == null || nameSuffix.getSuffix() == null) {
                    ++changeFileNameFailCount;
                    Console.logLine("解析文件名出错, 可能没有文件后缀：" + file.getAbsolutePath());
                    continue;
                }
                // 文件名：AAA.h/m
                if (nameSuffix.getName().equals(item.getSrc())) {
                    newFile = FileUtil.getFileWithChangedFileName(file, item.getDest() + "." + nameSuffix.getSuffix());
                    break;
                }
                // 文件名：AAA+Xxx.h/m
                if (nameSuffix.getName().startsWith(item.getSrc()+"+")) { // For category
                    newFile = FileUtil.getFileWithChangedFileName(file, item.getDest() + StringUtil.subStringFromIncludeChar(fileName,"+"));
                    break;
                }
            }
            if (newFile != null) { // find it, should rename
                if (FileUtil.rename(file, newFile)) {
                    ++changeFileNameSuccessCount;

                    String str = StringUtil.rPad("√ 重命名 " + file.getName(), 50);
                    Console.logLine(str + "=> " + newFile.getAbsolutePath());
                } else {
                    ++changeFileNameFailCount;
                    Console.logLine("ERROR: 重命名失败：" + file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Step 2> 代码替换
     * A,B
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
     *
     * TODO://@"A" ??
     *
     * PersonViewController
     * CustomPersonViewController
     */
    public void replaceCode(List<File> files) {
        List<ReplaceItem> items = this.items;
        List<File> targetFiles = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String fileStr = FileUtil.convertFileToString(file);
            if (fileStr == null) {
                Console.errorLine("!! null: " + file.getAbsolutePath());
            }
            for (ReplaceItem item : items) {
                if (fileStr.contains(item.getSrc())) {
                    //Console.logLine("检测到需要修改：" + file.getName());
                    targetFiles.add(file);
                    break;
                }
            }
        }
        for (File targetFile : targetFiles) {
            //https://stackoverflow.com/questions/1096621/read-string-line-by-line
            StringBuilder builder = new StringBuilder();
            List<String> strList = FileUtil.convertFileToStringList(targetFile);
            for (String s : strList) { // code of 1 line
                builder.append(getReplacedLine(s));
            }
            try {
                targetFile.setWritable(true);
                FileUtil.writeString(targetFile, builder.toString());
                String s = StringUtil.rPad("√ 修改代码：" + targetFile.getName(), 40);
                Console.logLine(s + " => " + targetFile.getAbsolutePath());
            } catch (IOException e) {
                Console.logLine("!! ERROR: write to file fail: " + targetFile.getAbsolutePath());
                Console.logLine(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void simpleReplaceCode(List<File> files) {
        List<ReplaceItem> items = this.items;
        Collections.sort(items, new Comparator<ReplaceItem>() {
            @Override
            public int compare(ReplaceItem o1, ReplaceItem o2) {
                return o2.getSrc().length() - o1.getSrc().length();
            };
        });
        for (File targetFile : files) {
            //https://stackoverflow.com/questions/1096621/read-string-line-by-line
            String originFileStr = FileUtil.convertFileToString(targetFile);
            String fileString = new String(originFileStr);
            for (ReplaceItem item : items) {
                fileString = StringUtil.replaceOccurrence(fileString, item.getSrc(), item.getDest());
            }
            try {
                if (false == originFileStr.trim().equals(fileString)) {
                    Console.logLine("rewrite code: " + targetFile.getName());
                    targetFile.setWritable(true);
                    FileUtil.writeString(targetFile, fileString);
                }
            } catch (IOException exception) {
                System.out.println("写入文件异常：" + targetFile.getAbsolutePath());
                exception.printStackTrace();
            }
        }
    }

    public String getReplacedLine(String oldLine) {
        String newLine = oldLine;
        for (ReplaceItem item : items) {
            newLine = CodeReplace.replaceInLine(newLine, item.getSrc(), item.getDest());
        }
        if (!newLine.endsWith("\n")) {
            newLine += "\n";
        }
        /**
        if (!oldLine.equals(newLine)) {
            Console.logLine(oldLine + "-->"+newLine);
        } else {
            Console.log("没有替换" + oldLine);
        }
         */

        return newLine;
    }
}
