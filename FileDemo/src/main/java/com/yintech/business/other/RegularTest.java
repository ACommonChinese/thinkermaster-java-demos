// https://blog.csdn.net/jiaobuchong/article/details/81257570
/**
 * Matcher:
 * goupCount() 返回该正则表达式模式中的分组数目，对应于「左括号」的数目
 * group(int i) 返回对应组的匹配字符，没有匹配到则返回 null
 * start(int group) 返回对应组的匹配字符的起始索引
 * end(int group) 返回对应组的匹配字符的最后一个字符索引加一的值
 */

import com.google.gson.Gson;
import com.yintech.business.other.ViewPointStockInfo;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularTest {
    // newLine = newLine.replaceAll(regex, "#import <" + "$1/" + newClsName + "\\+$2.h>");
    @Test
    public void testDaLiu2() {
        //String input = "↘{\"text\":\"Hello world!\"}↙游戏行业格隆汇11月11日|↘{\"text\":\"路通视信\"}↙(300555.SZ)涨停，持有的发生变更"
                ;
        String input = "↘{\"market\":\"sz\",\"symbol\":\"002624\",\"stockName\":\"完美世界\",\"exchange\":\"SZA\",\"text\":\"完美世界\",\"type\":\"stock\"}↙游戏行业格隆汇11月11日|↘{\"market\":\"sz\",\"symbol\":\"300555\",\"stockName\":\"路通视信\",\"exchange\":\"SZA\",\"text\":\"路通视信\",\"type\":\"stock\"}↙(300555.SZ)涨停，持有的发生变更。"
                ;
        String regex = "↘([^↘↙]+)?↙";
        Pattern pattern = Pattern.compile(regex);
        Matcher stockMatcher = pattern.matcher(input);
        for (int i = 0; i < stockMatcher.groupCount(); i++) {
            
        }
        Gson gson = new Gson();
            //ViewPointStockInfo viewPointStockInfo =
        String newStr = input.replaceAll(regex, gson.fromJson("$1", ViewPointStockInfo.class).getText()); //
        System.out.println(newStr);


//        String newLine = "#import <xxx/Dog.h>";
//        String regex = "#import\\s+<(\\w+)/" + oldClsName + "\\+(\\w+)\\.h>";
//        newLine = newLine.replaceAll(regex, "#import <" + "$1/" + newClsName + "\\+$2.h>");
//        System.out.println(line);
//        System.out.println(newLine);
    }

    @Test
    public void testDaLiu3() {
        String line = "#import <xxx/Dog+util.h>";
        String newLine = line;
        String regex = "#import\\s+<(\\w+)/" + "Dog" + "\\+(\\w+)\\.h>";
        newLine = newLine.replaceAll(regex, "#import <" + "$1/" + "Lion" + "\\+$2.h>");
        System.out.println(line);
        System.out.println(newLine);
    }

    @Test
    public void testDaLiu() {
        String input = "↘{\"text\":\"Hello world!\"}↙游戏行业格隆汇11月11日|↘{\"text\":\"路通视信\"}↙(300555.SZ)涨停，持有的发生变更"
                ;
        String regex = "↘([^↘↙]*)?↙";
        Pattern pattern = Pattern.compile(regex);
        Matcher stockMatcher = pattern.matcher(input);
        int offset = 0;
        StringBuilder spannableStringBuilder = new StringBuilder(input);

        while (stockMatcher.find()) {
            int start = stockMatcher.start();
            int end = stockMatcher.end(); // 最后一个索引+1
            int realStart = start - offset;
            int realEnd = end - offset;
            String stockStr = spannableStringBuilder.toString().substring(realStart + 1, realEnd - 1);
            //String stockStr = stockMatcher.group(0);
            System.out.println(" --> " + stockStr);
            Gson gson = new Gson();
            ViewPointStockInfo viewPointStockInfo = gson.fromJson(stockStr, ViewPointStockInfo.class);
            //String stockText = viewPointStockInfo.getText();
            String stockText = stockMatcher.group(1);
            spannableStringBuilder.replace(realStart, realEnd, stockText);
            offset += stockStr.length() - stockText.length() + 2;
        }

        System.out.println(spannableStringBuilder.toString());
    }

    @Test
    public void testYiXin() {
        // String input = "↘{\"market\":\"sz\",\"symbol\":\"002624\",\"stockName\":\"完美世界\",\"exchange\":\"SZA\",\"text\":\"完美世界\",\"type\":\"stock\"}↙游戏行业格隆汇11月11日|↘{\"market\":\"sz\",\"symbol\":\"300555\",\"stockName\":\"路通视信\",\"exchange\":\"SZA\",\"text\":\"路通视信\",\"type\":\"stock\"}↙(300555.SZ)涨停，持有的发生变更。"
                ;
        String input = "↘{\"text\":\"Hello world!\"}↙游戏行业格隆汇11月11日|↘{\"text\":\"路通视信\"}↙(300555.SZ)涨停，持有的发生变更"
                ;
        //String regex = "↘([^↘↙]+?)↙";
        String regex = "↘[^↘↙]+?↙";
        Pattern pattern = Pattern.compile(regex);
        Matcher stockMatcher = pattern.matcher(input);
//        while (matcher.find()) {
//            System.out.println(matcher.group(1) + ", pos: " + matcher.start() + "-" + (matcher.end()-1));
//        }

        int offset = 0;

        StringBuilder spannableStringBuilder = new StringBuilder(input);

        while (stockMatcher.find()) {
            int start = stockMatcher.start();
            int end = stockMatcher.end(); // 最后一个索引+1

            System.out.println("start: " + start + "  --> end: " + end);

            int realStart = start - offset;
            int realEnd = end - offset;
            String stockStr = spannableStringBuilder.toString().substring(realStart + 1, realEnd - 1);
            System.out.println(" --> " + stockStr);
            Gson gson = new Gson();
            ViewPointStockInfo viewPointStockInfo = gson.fromJson(stockStr, ViewPointStockInfo.class);
            String stockText = viewPointStockInfo.getText();
            spannableStringBuilder.replace(realStart, realEnd, stockText);

//            spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.common_blue)), realStart, realStart + stockText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    stockClick(context, Objects.requireNonNull(viewPointStockInfo));
//                }
//
//                @Override
//                public void updateDrawState(@NonNull TextPaint ds) {
//                    ds.setUnderlineText(false);
//                }
//            }, realStart, realStart + stockText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 替换后产生的偏移量
            offset += stockStr.length() - stockText.length() + 2;
        }

        System.out.println(spannableStringBuilder.toString());
    }

    @Test // 单个分组
    public void test1() {
        // 这个正则表达式有两个组，
        // group(0) 是 \\$\\{([^{}]+?)\\}
        // group(1) 是 ([^{}]+?)
        String regex = "\\$\\{([^{}]+?)\\}";
        Pattern pattern = Pattern.compile(regex);
        String input = "${name}-babalala-${age}-${address}";

        Matcher matcher = pattern.matcher(input);
        System.out.println(matcher.groupCount());
        // find() 像迭代器那样向前遍历输入字符串
        while (matcher.find()) {
            // System.out.println(matcher.group(0) + ", pos: " + matcher.start() + "-" + (matcher.end() - 1));
            System.out.println(matcher.group(1) + ", pos: " + matcher.start(1) + "-" + (matcher.end(1) - 1)); // group(1)表示括号中(一个子表达式分组)匹配到的内容
        }
    }

    @Test // 多个分组
    public void test2() {
        String regex = "(\\$\\{([^{}]+?)\\})"; // (${([^{}]+?)})
        Pattern pattern = Pattern.compile(regex);
        String input = "${name}-babalala-${age}-${address}";
        Matcher matcher = pattern.matcher(input);
        // matcher.find() 方法会对 input 这个字符串多次进行匹配，如果能匹配到，这个匹配结果里就会包含多个分组，我们可以从分组里提取我们想要的结果
        while (matcher.find()) {
            //System.out.println(matcher.group(0) + ", pos: " + matcher.start());
            //System.out.println(matcher.group(1) + ", pos: " + matcher.start(1));
            System.out.println(matcher.group(2) + ", pos: " + matcher.start(2));
        }
    }

    @Test // 通过分组提取想要的数据
    public void test3() {

    }
}
