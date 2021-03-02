package com.daliu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
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
}
