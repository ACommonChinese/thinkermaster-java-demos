package com.daliu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindDupImage {
    public static int dupCount = 0;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("输入不合法，请键入：java -jar FindDuplicateImages.jar 根目录");
            System.out.println("退出");
            System.exit(1);
        }
        String docPath = args[0];
        System.out.println("Searching in path: " + docPath);
        System.out.println("Please wait...");
        List<File> allFiles = FileUtil.getAllFiles(docPath);
        if (allFiles.size() == 0) {
            System.out.println("未找到文件");
            System.out.println("退出");
            System.exit(1);
        }
        List<File> targetFiles = new ArrayList<File>();
        for (File item : allFiles) {
            if (item != null) {
                if (item.getName().endsWith("png") || item.getName().endsWith("jpg") || item.getName().endsWith("jpeg")) {
                    targetFiles.add(item);
                }
            }
        }

        logSame(targetFiles);
        System.out.println("共有 " + dupCount + " 张图片可删除");
        System.exit(1);
    }

    public static void logSame(List<File> files) {
        List<File> currentFiles = files;
        if (currentFiles.size() <= 1) {
            return;
        }
        List<File> removeFiles = new ArrayList<File>();
        File firstFile = currentFiles.get(0);
        removeFiles.add(firstFile);

        String md5_1 = MD5Util.getMD5(firstFile);
        for (int i = 1; i < currentFiles.size(); i++) {
            File compareFile = currentFiles.get(i);
            String md5_2 = MD5Util.getMD5(compareFile);
            if (md5_1.contentEquals(md5_2)) {
                dupCount++;
                removeFiles.add(compareFile);
            }
        }
        if (removeFiles.size() > 1) {
            System.out.println("--------------------------------------");
            for (File removeFile : removeFiles) {
                System.out.println(removeFile.getAbsolutePath());
            }
            System.out.println("--------------------------------------");
        }
        currentFiles.removeAll(removeFiles);

        // recursive
        logSame(currentFiles);
    }
}
