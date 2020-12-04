// C中文网：http://c.biancheng.net/view/1206.html
// Box Layout: https://developer.ibm.com/zh/articles/j-lo-boxlayout/

package com.yintech.business.replace;

import javax.swing.*;
import java.awt.*;

public class RenameUI extends JFrame implements IProcessor {
    // 路径
    PathPanel pathPanel;
    // 复选框
    CheckBoxPanel checkBoxPanel;
    // 处理信息
    InfoPanel infoPanel;
    // 底部视频 user-input & information
    BottomPanel bottomPanel;

    public void init() {
        setTitle("Replace tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         | ------------------------------------|
         | 路径                                |
         | 有效文件后缀 ______________________  |
         | 进度显示                             |
         |-------------------------------------|
         |            |                        |
         |   ...      |                        |
         |   ...      |                        |
         |   ...      |                        |
         |            |                        |
         |            |                        |
         | ------------------------------------|
         */
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1000, 600));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // sub panel
        PanelUtil util = new PanelUtil();
        pathPanel      = util.getPathPanel();
        checkBoxPanel  = util.getCheckBoxPanel();
        infoPanel      = util.getInfoPanel();
        bottomPanel    = util.getBottomPanel();

        contentPanel.add(pathPanel); // 路径
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(checkBoxPanel); // 复选框
        contentPanel.add(infoPanel); // 处理信息显示
        contentPanel.add(bottomPanel); // 底部视图
        //contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(Box.createVerticalStrut(5));
        setContentPane(contentPanel);

        pathPanel.setDidChoose(file -> {
            infoPanel.setInfo("Start process ...");
            Processor processor = new Processor();
            processor.setChangeTypes(checkBoxPanel.getCheckTypes());
            processor.setDelegate(this);
            processor.setRootDir(file);
            processor.setInputText(bottomPanel.getInput());
            processor.start();
        });

        setSize(300, 400);
        setContentPane(contentPanel);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new RenameUI().init();
    }
    @Override
    public void onStart() {
        bottomPanel.getInfoPanel().clear();
    }
    @Override
    public void onError(String message) {
        bottomPanel.getInfoPanel().appendLine("ERROR: " + message);
    }
    @Override
    public void onProcessing(String info) {
        infoPanel.setInfo(info);
        bottomPanel.getInfoPanel().appendLine(info);
    }
    @Override
    public void onFinish() {

    }
}
