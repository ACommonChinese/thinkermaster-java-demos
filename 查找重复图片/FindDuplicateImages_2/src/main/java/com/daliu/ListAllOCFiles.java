package com.daliu;

import java.io.File;
import java.util.List;

public class ListAllOCFiles {
    public static void main(String[] args) {
        //String path = "/Users/daliu/Desktop/job/九方/YTXUtilCategory";
        // String path = "/Users/daliu/Desktop/job/九方/NWBUtilCategory";
//        // String path = "/Users/daliu/Desktop/job/九方/NWBUtilCategory/NWBUtilCategory/Classes";
        if (args.length == 0) {
            System.out.println("Error, please input path");
        }
        String path = args[0];
        List<File> allFiles = FileUtil.getAllFiles(path);
        for (File file : allFiles) {
            if (file.getName().endsWith(".h") || file.getName().endsWith(".m")) {
                System.out.println(file.getName());
            }
        }
    }
}
