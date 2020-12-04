import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MDGenerator {
    public static void generate(Command cmd) {
        String lineFeed = "\r\n";
        String lineFeed2 = "\r\n\r\n";
        String name = cmd.getName();
        if (name == null) {
            name = "TODO";
        }
        String dest = cmd.getDest();
        if (dest.startsWith(".")) {
            dest = new File("").getAbsolutePath() + "/" + name + ".md";
            System.out.println("文件地址：" + dest);
        }
        try (
                FileWriter writer = new FileWriter(dest);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                ) {
            // name
            bufferedWriter.write("#" + name + lineFeed2);
            bufferedWriter.write("###" + "1. 简介" + lineFeed2);
            bufferedWriter.write("###" + "2. SDK Name" + lineFeed2);
            bufferedWriter.write("###" + "3. 依赖库" + lineFeed2);
            bufferedWriter.write("###" + "4. Pod install" + lineFeed2);
            bufferedWriter.write("```ruby" + lineFeed2 + "```" + lineFeed2);
            bufferedWriter.write("###" + "6. 示例" + lineFeed2 + "```Objective-C" + lineFeed2 + "```" + lineFeed2);
        } catch (Exception ex) {
            System.out.println("写入文件失败");
            ex.printStackTrace();
        }
    }
}
