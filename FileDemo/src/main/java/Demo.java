import com.yintech.util.FileUtil;
import com.yintech.util.MD5Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        demo12();
    }

    public static void demo12() {
        FileUtil.logAndReturnAllFilesInFirstLevel("/Users/daliu/Desktop/job/Modules");
    }

    // Find Podfile.lock with YYKit
    public static void demo11() {
        File file = new File("/Users/daliu/Desktop/delete/1.txt");
        if (file == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tmp = null;
            boolean flag = false;
            String start = null;
            while ((tmp = reader.readLine()) != null) {
                if (tmp.startsWith("  - ")) {
                    start = tmp;
                }
                if (start != null) {
                    if (tmp.startsWith("    - YYKit")) {
                        System.out.println(start);
                        System.out.println("  " + tmp);
                        start = null;
                    }
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void demo10() {
        //NWBOptionalBusinessModule
        List<File> files = FileUtil.logAndReturnFilesContainStr("/Users/daliu/Desktop/job/Refactor_2/NWBOptionalBusinessModule", "<YTXBusinessUI/");
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }
        } else {
            System.out.println("not found");
        }
    }

    public static void demo9() {
        String path = "/Users/daliu/Desktop/job/Modules/tcyapp";
        List<File> files = FileUtil.logAndReturnFilesContainStr(path, "comparePrice:");
        System.out.println("使用了 comparePrice: 的文件：");
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
        System.out.println("---------------------------");
        for (File file : files) {
            if (FileUtil.isContentStr(file, "<YTXBusinessUI/YTXBusinessUI.h>") == false) {
                System.out.println("Find file: " + file.getAbsolutePath());
            }
        }
    }

    // <YTXUtil/YTXUtil.h>
    public static void demo8() {
        String src = "YTXUtil/YTXUtil.h";
        //<YTXBusinessUI/YTXBusinessUI.h>
        //String path = "/Users/daliu/Desktop/job/Refactor_2/NGTInitBusinessModule";
        String path = "/Users/daliu/Desktop/job/Refactor_2/NGTNotificationManagerModule";
        List<File> files = FileUtil.logAndReturnFilesContainStr(path, src);
        System.out.println("---------------------------------");
        if (files != null) {
            for (File file : files) {
                //(File file, String src, String dest) {
                FileUtil.appendAfterString(file, src, "#import <YTXBusinessUI/YTXBusinessUI.h>\n");
            }
        }
    }

    public static void demo7() {
        FileUtil.logLinesContainStr("/Users/daliu/Desktop/job/Refactor_2/tcyapp/Podfile", ":path =>");
//        String dirPath = "/Users/daliu/Desktop/job/Refactor_2/tcyapp";
//        System.out.println("包含YTXUtil.h的文件列表：");
//        FileUtil.logFilesContainStr(dirPath, "YTXUtil.h");
    }

    public static void demo6() {
        String dirPath = "/Users/daliu/Desktop/job/Refactor_2/tcyapp";
        System.out.println("包含 <ZJSUtil/ZJSUtil.h> 的文件列表：");
        FileUtil.logFilesContainStr(dirPath, "<ZJSUtil/ZJSUtil.h>");
    }

    public static void demo5() {
        String dirPath = "/Users/daliu/Desktop/job/Refactor_2/YTXEmotion";

        System.out.println("包含<YTXUtil/的文件列表：");
        FileUtil.logFilesContainStr(dirPath, "<YTXUtil/");

//        System.out.println("开始清理...");
//        processDelete(dirPath);
//        System.out.println("----------------------------------");
//        System.out.println("清理后，包含<YTXUtil/的文件列表：");
//        FileUtil.logFilesContainStr(dirPath, "<YTXUtil/");
    }

    public static void processDelete(String dirPath) {
        List<File> files = FileUtil.getAllFiles(dirPath);
        for (File file : files) {
            if (!file.getName().endsWith("-InteriorHeader.h") && !file.getName().contains("NWBPhotoViewController.h")) {
                FileUtil.deleteLineHasString(file, "<YTXUtil/");
            }
        }
    }

    public static void demo4() {
        String dirPath = "/Users/daliu/Desktop/compare/NGTMineBusinessModule";
        FileUtil.logFilesContainStr(dirPath, "<YTXUtil/");
    }

    public static void demo3() {
        // tag: 2.5.6
        String path1 = "/Users/daliu/Desktop/compare/2.5.6";
        List<File> fileList1 = FileUtil.getAllFiles(path1);
        System.out.println("共有" + fileList1.size() + "个文件: " + path1);

        // TCY
        String path2 = "/Users/daliu/Desktop/compare/TCY";
        List<File> fileList2 = FileUtil.getAllFiles(path2);
        System.out.println("共有" + fileList2.size() + "个文件：" + path2);

        for (File file1 : fileList1) {
            boolean didFind = false;
            for (File file2 : fileList2) {
                if (file1.getName().equals(file2.getName())) { // 文件名相同
                    didFind = true;
                    String md51 = MD5Util.getMD5(file1);
                    String md52 = MD5Util.getMD5(file2);
                    if (!md51.equals(md52)) {
                        System.out.println("文件内容不相同：" + file1.getAbsolutePath() + " 《--》 " + file2.getAbsolutePath());
                    } else {
                        System.out.println("相同：" + file1.getName() + " == " + file2.getName());
                    }
                    break;
                }
            }
            if (!didFind) {
                System.out.println("未找到匹配文件：" + file1.getName());
            }
//            else {
//                System.out.println("文件内容相同: " + file1.getName());
//            }
        }

//        System.out.println(path1 + " 共有" + fileList1.size() + "个文件：");
//        for (File file : fileList1) {
//            System.out.println(file.getName());
//        }
//        System.out.println("-------------------------------");
//        System.out.println(path2 + " 共有" + fileList2.size() + "个文件：");
//        for (File file : fileList2) {
//            System.out.println(file.getName());
//        }
    }

    public static void demo1() {
        String basePath = "/Users/daliu/Desktop/job/Refactor_2/YTXUtil/YTXUtil/Classes/Base";
        String commonPath = "/Users/daliu/Desktop/job/Refactor_2/YTXUtil/YTXUtil/Classes/Common";
        List<File> baseFileList = FileUtil.getAllFiles(basePath);
        List<File> commonFileList = FileUtil.getAllFiles(commonPath);
        // 查看是否base引用了common的东西
        List<String> commonHeaderNames = new ArrayList<>();
        for (File commonFile : commonFileList) {
            commonHeaderNames.add(commonFile.getName());
        }
        for (File baseFile : baseFileList) {
            String baseFileStr = FileUtil.convertFileToString(baseFile);
            boolean didFind = false;
            for (String commonName : commonHeaderNames) {
                if (baseFileStr.contains(commonName)) {
                    didFind = true;
                    System.out.println("！！！ base中引用了common: " + baseFile.getAbsolutePath() + " --> " + commonName);
                }
            }
            if (!didFind) {
                System.out.println("OK: " + baseFile);
            }
        }
    }

    public static void demo2() {
        String path = "/Users/daliu/Desktop/job/Refactor_2/ytxutil/YTXUtil/Classes/Base";
//        FileUtil.logFilesWithSuffix(path, ".h");
    }
}

//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBHomePageBusinessModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBOptionalBusinessModule.git
//        git clone http://gitlab.yintech.net/ytx/futures/iOS/FTRSTradeBusinessModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/nugget/ios/NGTChooseStockBusinessModule.git
//        git clone http://gitlab.yintech.net/ytx/paprika/nugget/ios/NGTMineBusinessModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBQuotationBusinessModule.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXChat.git
//        git clone http://gitlab.yintech.net/rjhy/app/nugget/ios/NGTCopyWeChatBusinessModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBUserOptionalListManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/nugget/ios/NGTJumpManagerModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBBasicProviderModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBUserManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBPreloadConfig.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBQuotationModel.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBFDZQAPI.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXChart2.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBTransitionQuotationModel.git
//        git clone http://gitlab.yintech.net/rjhy/app/nugget/ios/NGTWebViewController.git
//        git clone http://gitlab.yintech.net/rjhy/app/nugget/ios/NGTEnvironmentProvider.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXMQTTClientManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBQuotationChartManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/NewStar/iOS/YTXRobotModule.git
//        git clone http://gitlab.yintech.net/ytx/futures/iOS/FTRSChartDataManager.git
//        git clone http://gitlab.yintech.net/ytx/futures/iOS/FTRSQuotationSocketManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/base/ios/YTXRouterModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBLiveBusinessModule.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXSocketRocket.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXVideoAVPlayer.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBShareManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/nugget/ios/NGTNotificationManagerModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/Util/NWBQuotationGCDAsyncSocketManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/Util/NWBChartManagerModule.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/Util/NWBQuotationWebSocketManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/Util/NWBQuotify.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBScreenShotManager.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/Util/NWBSinaAPI.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/NewBee/NWBSQLite.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXChart2KLineItem.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXIntroGuideManger.git
//        git clone http://gitlab.yintech.net/ytx-ios/YTXQuotationModel.git
//        git clone http://gitlab.yintech.net/rjhy/app/newbee/iOS/Util/NWBQuotationProtoBufModels.git
//        git clone http://gitlab.yintech.net/rjhy/app/nugget/ios/NGTSpecialTabbarItemBusinessModule.git
