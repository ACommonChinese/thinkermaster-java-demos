import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TextValidatorUI {
    private int width = 800;
    private int height = 600;
    private JFrame frame = new JFrame("测试文本匹配");
    private JTextField textField = new JTextField(30);
    private JTextField annoField = new JTextField(30);
    private JTextArea textArea = new JTextArea("", 10, 10);
    private JLabel label = new JLabel();

    public void init() {
        frame.setLayout(new BorderLayout(30, 5));

        textField.addFocusListener(new TextFieldHintListener(textField, "请输入汉字"));
        annoField.addFocusListener(new TextFieldHintListener(annoField, "请输入拼音"));

        Panel panel = new Panel();
        panel.setLayout(new BorderLayout(20, 5));
        panel.add(textField, BorderLayout.NORTH);
        panel.add(annoField, BorderLayout.CENTER);
        JButton button = new JButton("开始校验");
        panel.add(button, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.NORTH);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                String pinyin = annoField.getText();
                TextValidator validator = new TextValidator();
                boolean success = validator.validate(textArea, text, pinyin);
                if (success) {
                    label.setForeground(Color.black);
                    label.setText("      正确 √");
                }
                else {
                    label.setBackground(Color.green);
                    label.setForeground(Color.red);
                    label.setText("      错误 ×");
                }
            }
        });

        frame.add(new Label(), BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(textArea, 20, 30);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(label, BorderLayout.WEST);
        label.setBackground(Color.red);
        label.setHorizontalTextPosition(JLabel.HORIZONTAL);
        frame.add(new Label(), BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 旅途中看着来来往往的人群，觉得很温暖，曾经想做个梦想家，如今梦没了，只剩下想家了。
        // lǚ tú zhōng kàn zhe lái lái wǎng wǎng de rén qún $ júe  dé hěn wēn nuǎn $ céng jīng xiǎng zuò gè mèng xiǎng jiā $ rú jīn mèng méi le $ zhī shèng xià xiǎng jiā le $
        // javac *.java
        // jar cvfe validator.jar TextValidatorUI *
        new TextValidatorUI().init();
    }
}
