import javax.swing.*;

public class TextValidator {
    public boolean validate(JTextArea textArea, String text, String anno) {
        String[] pinyinList = anno.split(" ");
        int length = Integer.min(text.length(), pinyinList.length);
        boolean checkSuccess = false;
        if (pinyinList.length == text.length()) {
            checkSuccess = true;
        }
        else {
            textArea.setText("汉字和拼音个数不匹配!!\n");
        }

        for (int i = 0; i < length; i++) {
            textArea.append(String.valueOf(text.charAt(i)) + " ----> " + pinyinList[i] + "\n");
        }

        return checkSuccess;
    }
}
