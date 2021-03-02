import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 生成html和添加li标签
 * 文件结构：
    ios_doc (serverRoot)
        - index.html
        - branch_list.html (用于拷贝到其他目录)
        - YTXBusinessUI (moduleName)
            - dxg_dev (branchName)
                - _book (gitbook生成)
            - hxg_dev
                - _book (gitbook生成)
            ...
        - YTXChart (moduleName)
            - dxg_dev (branchName)
                - _book (gitbook生成)
        ... ...
 */
public class CreateDoc {
    public String moduleName;
    public String branchName;
    public String serverRoot = "/Users/daliu/Desktop/ios_doc/";

    //java -jar docutil.jar 组件名 分支名
    //java -jar docutil.jar YTXBusinessUI dxg_dev
    //                         args[0]    args[1]
    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            System.out.println(i + " " + args[i]);
        }
        CreateDoc doc = new CreateDoc();
        doc.moduleName = args[0];
        doc.branchName = args[1];
        doc.process();
    }

//    public static void main(String[] args) {
//        CreateDoc doc = new CreateDoc();
//        doc.moduleName = "YTXBusinessUI";
//        doc.branchName = "dxg_dev";
//        doc.process();
//    }

    public void process() {
        if (!makeSureBranchListHtmlExist()) {
            return;
        }
        appendIndexHtml();
        appendBranchListHtml();
    }

    public boolean makeSureBranchListHtmlExist() {
        //创建branch_list.html(如果不存在)
        File source = new File(serverRoot + "branch_list.html");
        File dest = new File(serverRoot + "/" + moduleName + "/branch_list.html");
        try {
            copyIfNotExist(source, dest);
        } catch (IOException e) {
            System.out.println("初始化文件branch_list失败");
            return false;
        }
        return true;
    }

    //往index.html中添加li标签
    public boolean appendIndexHtml() {
        File htmlFile = new File(serverRoot + "index.html");
        return appendLi(htmlFile, "<a href=\"./"+ this.moduleName +"/branch_list.html\">"+this.moduleName+"</a>");
    }

    //往branch_list.html中添加li
    public boolean appendBranchListHtml() {
        File htmlFile = new File(serverRoot + this.moduleName + "/branch_list.html");
        return appendLi(htmlFile, "<a href=\"./" + this.branchName + "/_book/index.html\">" + this.branchName + "</a>");
    }

    public boolean appendLi(File htmlFile, String text) {
        Document document = null;
        try {
            document = Jsoup.parse(htmlFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (document == null) {
            return false;
        }
        Element ol = document.selectFirst("body").selectFirst("ol");
        Elements elements = ol.getElementsByTag("li"); //all li element
        for (Element li : elements) {
            Element a = li.selectFirst("a");
            if (a != null && a.outerHtml() != null && a.outerHtml().equalsIgnoreCase(text)) {
                return true; //already exist
            }
        }

        Element li = ol.appendElement("li");
        //li.append("<a href=\"./YTXBusinessUI/branch_list.html\">" + "YTXUtil</a>");
        //li.append("<a href=\"./"+ this.moduleName +"/branch_list.html\">"+this.moduleName+"</a>");
        li.append(text);

        //System.out.println("append text: " + text);
        String resultHtml = document.outerHtml();
        try {
            //System.out.println("resultHtml: " + resultHtml);
            FileUtils.writeStringToFile(htmlFile, resultHtml, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean createFileIfNotExist(String path, String tempFilePath) {
        File file = new File(path);

        if (file.exists()) return true;
        try {
            file.createNewFile();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void copy(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public void copyIfNotExist(File source, File dest) throws IOException {
        if (!dest.exists()) {
            copy(source, dest);
        }
    }
}
