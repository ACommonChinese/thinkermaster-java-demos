package com.yintech.business.replace;

import com.sun.istack.internal.NotNull;
import com.yintech.util.FileUtil;
import com.yintech.util.NameSuffix;
import com.yintech.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Replace;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.*;

class ReplaceItemParseResult {
    private List<ReplaceItem> items;
    private List<File> files;
    public List<ReplaceItem> getItems() {
        return items;
    }
    public void setItems(List<ReplaceItem> items) {
        this.items = items;
    }
    public List<File> getFiles() {
        return files;
    }
    public void setFiles(List<File> files) {
        this.files = files;
    }
}

public class ReplaceItem {
    private String src;
    private String dest;

    protected ReplaceItem copy() {
        ReplaceItem replaceItem = new ReplaceItem();
        replaceItem.setSrc(src);
        replaceItem.setDest(dest);
        return replaceItem;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    /**
     * 寻找文列列表中以固定前缀开头的文件名的文件，并返回对应的原文件名和目标文件名，假如：
     * 输入：YTX->TTT
     * YTXUtil.h
     * YTXText.m
     * YTXPerson.h
     *
     * 则items: [
     *      YTXUtil, TTTUtil,
     *      YTXText, TTTText,
     *      YTXPerson, TTTPerson
     * ]
     * @param text 输入的文本
     * @param files
     * @return
     */
    public static ReplaceItemParseResult parseFromInputForFileNameChange(String text, List<File> files) {
        // YTX->YTT
        // src: YTX | dest: TTT
        /**
         * [
         * YTXUtil TTTUtil
         * YTXPerson TTTPerson
         * ...
         * ]
         */
        List<ReplaceItem> items = parseNameFromText(text);

        List<ReplaceItem> targetItems = new ArrayList<>();
        List<File> targetFiles = new ArrayList<>();

        for (File file : files) {
            ReplaceItem item = matchedItemForNameChange(file, items); // YTXUtil.h

            if (item != null) {
                ReplaceItem newItem = item.copy();
                NameSuffix nameSuffix = NameSuffix.parse(file.getName());
                String prefix = item.src;
                newItem.src = nameSuffix.getName(); // YTXUtil
                newItem.dest = item.dest + StringUtil.subStringFromNotIncludeStr(newItem.src, prefix); // TTTUtil
                targetFiles.add(file);
                // YTXUtil --> TTTUtil
                if (!isContainsItem(targetItems, newItem)) {
                    targetItems.add(newItem);
                }
            } else {
                //Console.logLine("不修改: " + file.getName());
            }
        }

        ReplaceItemParseResult result = new ReplaceItemParseResult();
        result.setItems(targetItems);
        result.setFiles(targetFiles);
        return result;
    }

    /**
     * 根据输入解析出要替换的内容，比如：YTXUtil -> TTTUtil
     * @param text 输入内容
     * @param files 要搜索的文件列表
     * @return ReplaceItemParseResult对象
     */
    public static ReplaceItemParseResult parseFromInputForCodeChange(String text, List<File> files) {
        List<ReplaceItem> inputItems = parseNameFromText(text); // [YTX->TTT | AAA->BBB | ...]
        Set<String> targetNameSet = new HashSet<>();
        for (File file : files) {
            Set<String> nameSet = FileUtil.getOCClassAndProtocolNamesInFile(file); // OC文件中类名和OC协议名
            if (nameSet != null) {
                targetNameSet.addAll(nameSet); // targetNameSet: 所有OC类名+协议名
            }
        }

        List<ReplaceItem> targetItems = new ArrayList<>();
        for (String targetName : targetNameSet) { // YTXUtil
            ReplaceItem newItem = getNewItem(targetName, inputItems);
            if (newItem != null) {
                targetItems.add(newItem);
            }
        }

        ReplaceItemParseResult result = new ReplaceItemParseResult();
        result.setItems(targetItems);
        result.setFiles(files);
        return result;
    }

    @Test
    public void test_parseFromInputForCodeChange() {
        String text = "YTX->TTT";
        String[] suffix = {".h", ".m"};
        List<File> files = FileUtil.getAllFilesWithSuffixInList("/Users/daliu/Desktop/YTXEmotion", suffix);
        ReplaceItemParseResult result = parseFromInputForCodeChange(text, files);
        for (ReplaceItem item : result.getItems()) {
            System.out.println(item);
        }
    }

    /**
     * Convert ReplaceItem
     * @param ocName such as YTXUtil
     * @param inputItems such as [YTX->TTT]
     * @return new ReplaceItem, YTXUtil->TTTUtil
     */
    public static ReplaceItem getNewItem(String ocName, List<ReplaceItem> inputItems) {
        if (!StringUtil.isValidString(ocName) || inputItems == null || inputItems.size() == 0) return null;
        for (ReplaceItem item : inputItems) {
            if (ocName.trim().startsWith(item.src)) {
                ReplaceItem newItem = new ReplaceItem();
                newItem.src = ocName.trim();
                newItem.dest = StringUtil.getChangePrefixStr(newItem.src, item.src, item.dest);
                return newItem;
            }
        }
        return null;
    }

    // YTXUtil, YTX -> TTT  match
    public static boolean isOCClassNameMatchInputItems(String ocName, List<ReplaceItem> inputItemList) {
        if (!StringUtil.isValidString(ocName) || inputItemList == null || inputItemList.size() == 0) {
            return false;
        }
        for (ReplaceItem inputItem : inputItemList) {
            if (inputItem.src != null && ocName.startsWith(inputItem.src)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContainsItem(List<ReplaceItem> itemList, ReplaceItem item) {
        for (ReplaceItem replaceItem : itemList) {
            if (replaceItem.src.equals(item.getSrc())) {
                return true;
            }
        }
        return false;
    }

    public static ReplaceItem matchedItemForNameChange(File file, List<ReplaceItem> items) {
        String fileName = file.getName(); // YTXUtil.h
        for (ReplaceItem item : items) {
            if (fileName.startsWith(item.src)) { // YTX
                return item;
            }
        }
        if (fileName.startsWith("YTX")) {
            Console.logLine("出问题：fileName: " + fileName);
        }
        return null;
    }

    public static List<ReplaceItem> parseNameFromText(String text) {
        if (text == null || text.length() <= 0) {
            return null;
        }
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());
        //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //https://stackoverflow.com/questions/1096621/read-string-line-by-line
        BufferedReader reader = null;
        List<ReplaceItem> items = new ArrayList<>();
        try {
            reader = new BufferedReader(new StringReader(text));
            String newline = null;
            while ((newline = reader.readLine()) != null) {
                String line = newline.trim();
                if (line.startsWith("请输入要替换的前缀内容")) {
                    continue;
                }
                String[] arr = line.split("->");
                if (arr.length != 2) {
                    Console.error("! 输入不合法，请检查：" + line);
                    continue;
                }
                ReplaceItem item = new ReplaceItem();
                item.setSrc(arr[0]);
                item.setDest(arr[1]);
                items.add(item);
            }
        } catch (Exception e) {
            System.out.println("Convert string to BufferedReader fail");
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public String toString() {
        return StringUtil.rPad(src, 40) + "=> " + dest;
    }
}
