import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    // https://www.runoob.com/w3cnote/java-capture-group.html
    public static void main(String[] args) {
        //test1();
        test2();
    }

    // 普通捕获组
    public static void test1() {
        String dateStr = "2017-04-25";
        String regex = "(\\d{4})-((\\d{2})-(\\d{2}))";
        //有 4 个左括号，所以有 4 个分组:
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateStr);
        System.out.printf("\nmatcher.group(0) value:%s", matcher.group(0)); // 2017-04-25
        System.out.printf("\nmatcher.group(1) value:%s", matcher.group(1)); // 2017
        System.out.printf("\nmatcher.group(2) value:%s", matcher.group(2)); // 04-25
        System.out.printf("\nmatcher.group(3) value:%s", matcher.group(3)); // 04
        System.out.printf("\nmatcher.group(4) value:%s", matcher.group(4)); // 25
        matcher.find();
    }

    //命名捕获组
    //每个以左括号开始的捕获组，都紧跟着 ?，而后才是正则表达式
    //(?<year>\\d{4})-(?<md>(?<month>\\d{2})-(?<date>\\d{2}))
    public static void test2() {
        String dateStr = "2017-04-25";
        String regex = "(?<year>\\d{4})-(?<md>(?<month>\\d{2})-(?<date>\\d{2}))"; // "(?<year>\\d{4})-(?<md>(?<month>\\d{2})-(?<date>\\d{2}))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateStr);
        matcher.find();
        System.out.printf("\n===========使用名称获取=============");
        System.out.printf("\nmatcher.group(0) value:%s", matcher.group(0));
        System.out.printf("\n matcher.group('year') value:%s", matcher.group("year"));
        System.out.printf("\nmatcher.group('md') value:%s", matcher.group("md"));
        System.out.printf("\nmatcher.group('month') value:%s", matcher.group("month"));
        System.out.printf("\nmatcher.group('date') value:%s", matcher.group("date"));
        matcher.reset();
        System.out.printf("\n===========使用编号获取=============");
        matcher.find();
        System.out.printf("\nmatcher.group(0) value:%s", matcher.group(0));
        System.out.printf("\nmatcher.group(1) value:%s", matcher.group(1));
        System.out.printf("\nmatcher.group(2) value:%s", matcher.group(2));
        System.out.printf("\nmatcher.group(3) value:%s", matcher.group(3));
        System.out.printf("\nmatcher.group(4) value:%s", matcher.group(4));
    }
}
