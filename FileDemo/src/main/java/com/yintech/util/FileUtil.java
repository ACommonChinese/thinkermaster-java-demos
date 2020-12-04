package com.yintech.util;

import com.yintech.business.replace.Console;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

public class FileUtil {
    // file, <YTXUtil/YTXUtil.h>, <YTXBusinessUI/YTXBusinessUI.h>
    public static void appendAfterString(File file, String src, String newLine) {
        if (!isValidFile(file)) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
                if (str.contains(src)) {
                    builder.append(newLine + "\n");
                    System.out.println("append in: " + file.getAbsolutePath());
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        String fileText = builder.toString();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileText);
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void deleteLineHasString(File file, String content) {
        if (!isValidFile(file)) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                if (!str.contains(content)) {
                    builder.append(str + "\n");
                } else {
                    System.out.println("delete in: " + file.getAbsolutePath());
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        String fileText = builder.toString();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileText);
            writer.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static List<File> getAllFiles(String path) {
        File file = new File(path);
        if (null == file) {
            return null;
        }
        return getAllFiles(file);
    }

    public static List<File> getAllFiles(File file) {
        ArrayList<File> fileList = new ArrayList<File>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File itemFile = files[i];
                if (itemFile.getAbsolutePath().contains("Pods/Headers")) continue; // TODO://
                if (itemFile == null || itemFile.getName().contains(".DS_Store")) continue;
                if (itemFile.isDirectory()) {
                    List<File> items = getAllFiles(itemFile);
                    if (items != null) {
                        fileList.addAll(getAllFiles(itemFile));
                    }
                } else {
                    fileList.add(itemFile);
                }
            }
        } else {
            fileList.add(file);
        }
        return fileList;
    }

    public static void logLinesContainStr(String filePath, String str) {
        File file = new File(filePath);
        if (file == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tmp = null;
            while ((tmp = reader.readLine()) != null) {
                if (tmp.contains(str)) {
                    String preStr = tmp.substring(0, tmp.indexOf(":path"));
                    //System.out.println(tmp);
                    System.out.println(preStr);
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static List<File> logAndReturnAllFiles(String dirPath) {
        List<File> allFiles = getAllFiles(dirPath);
        for (File file : allFiles) {
            System.out.println(file.getName());
        }
        return allFiles;
    }

    public static File[] logAndReturnAllFilesInFirstLevel(String dirPath) {
        File file = new File(dirPath);
        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println(f.getName());
        }
        return files;
    }

    public static List<File> logAndReturnFilesContainStr(String dirPath, String str) {
        List<File> targetFiles = new ArrayList<File>();
        List<File> files = FileUtil.getAllFiles(dirPath);
        if (files.size() <= 0) {
            System.out.println("不存在：" + dirPath);
        }
        for (File file : files) {
            if (isValidFile(file)) {
                if (convertFileToString(file).contains(str)) {
                    System.out.println(file.getAbsolutePath() + " contains: " + str);
                    targetFiles.add(file);
                }
            }
        }
        return targetFiles.size() > 0 ? targetFiles : null;
    }

    public static List<File> getAllFilesContainStrInList(String dirPath, List<String> strList, boolean isLog) {
        List<File> findFiles = new ArrayList<>();

        List<File> files = FileUtil.getAllFiles(dirPath);
        if (files.size() <= 0) {
            System.out.println("不存在：" + dirPath);
        }
        for (File file : files) {
            if (isValidFile(file)) {
                String fileContent = convertFileToString(file);
                boolean didFind = false;
                for (String s : strList) {
                    if (fileContent.contains(s)) {
                        didFind = true;
                        if (isLog) {
                            System.out.println(file.getAbsolutePath() + " contains: " + s);
                        }
                    }
                }
                if (didFind) {
                    findFiles.add(file);
                }
            }
        }
        return findFiles;
    }

    /**
     * 获取所有包括有字符串str的文件
     * @param dirPath 目录
     * @param str 包含的字符串
     * @param isLog 是否打印
     * @return 符合条件的文件列表
     */
    public static List<File> getAllFilesContainStr(String dirPath, final String str, boolean isLog) {
        return getAllFilesContainStrInList(dirPath, new ArrayList<String>() {
            {
                add(str);
            }
        }, isLog);
    }

    public static void logFilesContainStr(String dirPath, String str) {
        getAllFilesContainStr(dirPath, str, true);
    }

    /**
     * 修改文件后缀
     */
    public static File getFileWithChangedSuffix(File src, String suffix) {
        // /Users/daliu/Desktop/job/Modules/tcyapp/Pods/YTXChat/YTXChat/Classes/EMUI/Helper/3rdParty/MWPhotoBrowser/Classes/MWGridCell.h
        String absolutePath = src.getAbsolutePath();
        int lastIndexOf = absolutePath.lastIndexOf(".");
        if (lastIndexOf != -1) {
            String newPath = absolutePath.substring(0, lastIndexOf) + suffix;
            return new File(newPath);
        } else {
            return null;
        }
    }

    /**
     * 根据原文件获取修改后的文件名
     * @param src 原文件
     * @param newName 新文件名
     * @return 修改后的文件名（只获取名字，不修改文件）
     */
    public static File getFileWithChangedFileName(File src, String newName) {
        if (src == null) {
            return null;
        }
        String parent = StringUtil.removeLastPathComponent(src.getAbsolutePath());
        String newFilePath = StringUtil.appendPathComponent(parent, newName); // /xxx/yyy/BBB.h
        File newFile = new File(newFilePath);
        return newFile;
    }

    public static List<File> getAllFilesWithSuffixInList(String dirPath, String[] suffixArr) {
        return getAllFilesWithSuffixInList(dirPath, suffixArr, false);
    }

    public static List<File> getAllFilesWithSuffixInList(String dirPath, String[] suffixArr, boolean isLog) {
        List<File> files = FileUtil.getAllFiles(dirPath);
        List<File> targetFiles = new ArrayList<>();
        List<String> suffixList = Arrays.asList(suffixArr);
        for (File file : files) {
            String fileName = file.getName();
            for (String suffix : suffixList) {
                if (fileName.endsWith(suffix)) {
                    if (isLog) {
                        System.out.println(file.getName());
                    }
                    targetFiles.add(file);
                    break;
                }
            }
        }
        return targetFiles;
    }

    public static List<File> getAllFilesWithPrefix(String dirPath, String prefix) {
        String[] strings = new String[] { prefix };
        return getAllFilesWithPrefixInList(dirPath, strings);
    }

    public static List<File> getAllFilesWithPrefixInList(String dirPath, String[] prefixArr) {
        List<File> files = FileUtil.getAllFiles(dirPath);
        List<File> targetFiles = new ArrayList<>();
        List<String> prefixList = Arrays.asList(prefixArr);
        for (File file : files) {
            String fileName = file.getName();
            for (String prefix : prefixList) {
                if (fileName.startsWith(prefix)) {
                    targetFiles.add(file);
                }
            }
        }
        return targetFiles;
    }

    public static List<File> getAllFilesWithSuffix(String dirPath, String suffix, boolean isLog) {
        String[] strings = new String[] { suffix };
        return getAllFilesWithSuffixInList(dirPath, strings, isLog);
    }

    public static List<File> sortByFileName(List<File> files) {
        return sortByFileName(files, true);
    }

    public static List<File> sortByFileName(List<File> files, boolean ascending) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (ascending) {
                    return o2.getName().length() - o1.getName().length();
                } else {
                    return o1.getName().length() - o2.getName().length();
                }
            }
        });
        return files;
    }
    @Test
    public void testSortByFileName() {
//        String[] suffix = {".h", ".m"};
//        List<File> files = FileUtil.getAllFilesWithSuffixInList("/Users/daliu/Desktop/job/Modules/ytxutil/YTXUtil", suffix);
        List<File> files = FileUtil.getAllFilesWithPrefix("/Users/daliu/Desktop/job/Modules/", "YTX");
        List<File> sortedFiles = sortByFileName(files);
        for (File sortedFile : sortedFiles) {
            System.out.println(sortedFile.getName());
        }
    }

    // only .h and .m is valid
    public static boolean isValidFile(File file) {
        if (file == null ||
                !file.exists() ||
//                file.getName().endsWith(".xcodeproj") ||
//                file.getName().endsWith(".DS_Store") ||
                !(file.getName().endsWith(".h") || file.getName().endsWith(".m"))) {
            return false;
        }
        return true;
    }

    /**
     * 获取一个文件中的所有OC类或OC协议的名字
     * @param file 文件
     * @return 类名+协议名 集合
     */
    public static Set<String> getOCClassAndProtocolNamesInFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        List<String> stringList = convertFileToStringList(file);
        Set<String> nameSet = new HashSet<>();
        for (String s : stringList) {
            if (s.trim().length() == 0) continue;
            String name = StringUtil.getOCClassOrProtocolName(s);
            if (StringUtil.isValidString(name)) {
                nameSet.add(name);
            }
        }
        return nameSet.size() > 0 ? nameSet : null;
    }

    /**
     * 转换文本文件为字符串
     * @param file 文件
     * @return 转换后的字符串
     */
    public static String convertFileToString(File file) {
        if (!isValidFile(file)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        return builder.toString();
    }

    /**
     * 转换文本文件为字符串List
     * @param file 文件
     * @return 转换后的字符串List
     */
    public static List<String> convertFileToStringList(File file) {
        if (!isValidFile(file)) { // Only consider .h & .m
            return null;
        }
        List<String> stringList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                stringList.add(str + "\n");
                // builder.append(str + "\n");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return stringList;
    }

    public static boolean hasStringContent(File file, String targetStr) {
        if (isValidFile(file) || targetStr == null || targetStr.length() <= 0) {
            return false;
        }
        String content = convertFileToString(file);
        return content.contains(targetStr);
    }

    public static boolean isContentStr(File file, String targetStr) {
        return hasStringContent(file, targetStr);
    }

    public static boolean isEqual(String file1, String file2) {
        File f1 = new File(file1);
        File f2 = new File(file2);
        return isEqual(f1, f2);
    }

    public static boolean isEqual(File file1, File file2) {
        if (file1 == null || file2 == null || file1.isFile() == false || file2.isFile() == false) {
            throw new RuntimeException("file invalud");
        }
        return com.daliu.MD5Util.getMD5(file1).equals(com.daliu.MD5Util.getMD5(file2));
    }

    public static boolean rename(File oldFile, File newFile) {
        if (oldFile.exists() && oldFile.isFile()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }

    /**
     * 文件拷贝
     * @param source 源文件
     * @param dest 目标文件
     */
    public static void copy(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            long size = outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            if (size > 0) {
                System.out.println("From: " + source.getAbsolutePath());
                System.out.println("To: ----> " + dest.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("copy file fail: " + source.getAbsolutePath() + " --> " + dest.getAbsolutePath());
            e.printStackTrace();
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 往文件中写入字符串
     * @param file 文件
     * @param text 字符串
     * @Note 如果文件不存在，则创建
     */
    public static void writeString(File file, String text) throws IOException {
        if (file == null) return;

        File f = file;
        if (!f.exists()) {
            Console.logLine("File not exist, create: " + f.getAbsolutePath());
            f.createNewFile();
        }
        FileWriter writer = new FileWriter(f);
        writer.write(text);
        writer.flush();
        writer.close();
    }

    /**
     * 打印List中所有文件绝对路径
     * @param files 文件List
     */
    public static void logFileList(List<File> files) {
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
