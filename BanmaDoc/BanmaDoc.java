public class BanmaDoc {
    // java BanmaDoc -name FrameworkName -version 1.0 -headers /Users/banma-1118/IdeaProjects/BanmaDoc/src/BanmaDoc.java -dest .
    public static void main(String[] args) {
        Command cmd = new Command(args);
        System.out.println("==== 获取输入 ====");
        System.out.println(cmd.toString());
        System.out.println("==== 生成文件 ====");

        MDGenerator.generate(cmd);
    }
}