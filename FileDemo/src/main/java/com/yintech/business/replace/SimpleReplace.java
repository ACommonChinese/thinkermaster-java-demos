package com.yintech.business.replace;

import com.yintech.util.FileUtil;
import com.yintech.util.StringUtil;

import java.io.File;
import java.util.List;

public class SimpleReplace {
    public static void main(String[] args) {
        // java -jar FileDemo.jar YourDirPath
        // java -jar FileDemo.jar -src YTX -dest TTT
        String src = "YTX";
        String dest = "TTT";
        List<File> srcFiles = FileUtil.getAllFilesWithPrefix("dirPath", src);
        System.out.println("Searching files: ");
        for (File srcFile : srcFiles) {
            System.out.println(srcFile.getName() + StringUtil.rPad(" --> ", 40) + srcFile.getAbsolutePath());
        }

//        FileUtil.getFileWithChangedSuffix()
    }
}
