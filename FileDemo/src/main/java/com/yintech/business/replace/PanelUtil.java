package com.yintech.business.replace;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EnumSet;

/**
 * 路径
 */

@FunctionalInterface
interface IPathPanel {
    void didChoose(File file);
}

class PathPanel extends JPanel {
    public JLabel label = new JLabel();
    public JButton button = new JButton();
    private IPathPanel iPathPanel;

    public void setDidChoose(IPathPanel handler) {
        iPathPanel = handler;
    }

    public PathPanel() {
        //Swing中的组件位置和大小取决于布局管理器(如果设置了布局管理器)
        // Component verticalStrut = Box.createVerticalStrut(60);
        //this.setBackground(Color.BLUE);
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        this.add(Box.createRigidArea(new Dimension(10, 10)));
        JPanel contentPanel = new JPanel();
        //contentPanel.setBackground(Color.yellow);
        add(contentPanel);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.add(Box.createHorizontalStrut(20));
        label.setText("");
        //label.setBackground(Color.red);
        contentPanel.add(label);
        contentPanel.add(Box.createHorizontalGlue());
        button.setText("选择目录");
        contentPanel.add(button);
        this.setAlignmentY(Component.TOP_ALIGNMENT);
        // http://www.voidcn.com/article/p-teyvvngj-bub.html
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, contentPanel.getMinimumSize().height));
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                JFileChooser fileChooser = new JFileChooser();
                FileSystemView systemView = FileSystemView.getFileSystemView();
                fileChooser.setCurrentDirectory(systemView.getHomeDirectory());
                fileChooser.setDialogTitle("选择要更改名字的根目录文件夹");
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setApproveButtonText("确定");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                // https://www.infoworld.com/article/2077597/java-tip-93--add-a-file-finder-accessory-to-jfilechooser.html
                JButton previous = new JButton("上一级");
                previous.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File current = fileChooser.getCurrentDirectory();
                        fileChooser.setCurrentDirectory(current.getParentFile());
                        fileChooser.setSelectedFile(current.getParentFile());
                    }
                });
                fileChooser.setAccessory(previous);
                int result = fileChooser.showOpenDialog(null);
                //fileChooser.setSelectedFile(new File("."));
                if (result == JFileChooser.CANCEL_OPTION) {
                    return;
                } else if (result == JFileChooser.APPROVE_OPTION) {
                    label.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    if (iPathPanel != null) {
                        iPathPanel.didChoose(fileChooser.getSelectedFile());
                    }
                }
                fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());
            }
        });
    }
}

enum ChangeType {
    FILE, CODE
}

/**
 * 复选框：文件名 | 代码
 */
class CheckBoxPanel extends JPanel {
    private JCheckBox check1;
    private JCheckBox check2;

    public CheckBoxPanel() {
        //this.setBackground(Color.CYAN);
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);
        this.add(Box.createHorizontalStrut(20));
        check1 = new JCheckBox("文件名", true);
        check2 = new JCheckBox("代码", true);
        this.add(check1);
        this.add(Box.createHorizontalStrut(5));
        this.add(check2);
        this.add(Box.createHorizontalGlue());
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    public EnumSet<ChangeType> getCheckTypes() {
        EnumSet<ChangeType> enumSet = EnumSet.noneOf(ChangeType.class);
        if (check1.isSelected()) {
            enumSet.add(ChangeType.FILE);
        }
        if (check2.isSelected()) {
            enumSet.add(ChangeType.CODE);
        }
        return enumSet;
    }
}

// 进度
class InfoPanel extends JPanel {
    public JLabel label = new JLabel("current process: ");

    public InfoPanel() {
        //this.setBackground(Color.red);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createHorizontalStrut(10));
        this.add(label);
        label.setPreferredSize(new Dimension(100, 50));
        //label.setBackground(Color.yellow);
        this.add(Box.createHorizontalStrut(10));
        this.setMinimumSize(new Dimension(50, 50));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
    }

    public void setInfo(String text) {
        if (null != text) {
            this.label.setText(text);
        }
    }
}

// 输入替换文件
class UserInputPanel extends JPanel {
    public JTextArea textArea = new JTextArea("请输入要替换的前缀内容，格式：\nAAA->YTX\nBBB->TCY\n");
    public UserInputPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));
        textArea.setForeground(Color.black);
        textArea.setFont(new Font("Courier", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        Dimension size = scrollPane.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(300, 400));
        add(scrollPane);
        setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        add(Box.createVerticalStrut(5));
    }
}

class ReplaceInfoPanel extends JPanel implements IConsole {
    @Override
    public void consoleLog(String string) {
        appendText(string);
    }

    @Override
    public void consoleError(String string) {
        appendText(string);
    }

    public JTextArea textArea = new JTextArea();
    public ReplaceInfoPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));
        //textArea.setForeground(Color.black);
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(textArea, 20, 30);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
        add(Box.createVerticalStrut(5));
        Console.setDelegate(this);
    }

    public void setText(String text) {
        if (text == null || text.length() == 0) return;
        textArea.setText(text);
    }
    public void appendText(String text) {
        if (text == null || text.length() == 0) return;
        textArea.append(text);
    }
    public void appendLine(String text) {
        if (text == null || text.length() == 0) return;
        appendText(text + "\n");
    }
    public void clear() {
        textArea.setText("");
    }
}

class BottomPanel extends JPanel {
    private UserInputPanel inputPanel = new UserInputPanel();
    private ReplaceInfoPanel infoPanel = new ReplaceInfoPanel();

    public ReplaceInfoPanel getInfoPanel() {
        return infoPanel;
    }
    public void setInfoPanel(ReplaceInfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }
    public UserInputPanel getInputPanel() {
        return inputPanel;
    }
    public void setInputPanel(UserInputPanel inputPanel) {
        this.inputPanel = inputPanel;
    }
    public String getInput() {
        return getInputPanel().textArea.getText();
    }

    public BottomPanel() {
        //this.setBackground(Color.PINK);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createHorizontalStrut(10));
        this.add(inputPanel);
        this.add(Box.createHorizontalStrut(10));
        this.add(infoPanel);
        this.add(Box.createHorizontalStrut(10));
    }
}

public class PanelUtil {
    public PathPanel getPathPanel() {
        return new PathPanel();
    }

    public InfoPanel getInfoPanel() {
        return new InfoPanel();
    }

    public CheckBoxPanel getCheckBoxPanel() {
        return new CheckBoxPanel();
    }

    public BottomPanel getBottomPanel() {
        return new BottomPanel();
    }
}
