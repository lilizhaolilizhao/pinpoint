package com.oneapm.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 应用选择面板
 */
public class AppChoosePanel extends JPanel {

    private JPanel appChoosePanel;

    //文件选择面板组件
    private JLabel appChooseLabel;
    private JTextField localMsgFileField;
    private JButton fileChooseBtn;

    public AppChoosePanel() {
        initViewPanel();
        initViewComponent();
        initlisteners();
    }

    /**
     * 添加监听事件
     */
    private void initlisteners() {
    }

    /**
     * 初始化 panel
     */
    private void initViewPanel() {
        appChoosePanel = new JPanel();
        appChoosePanel.setLayout(new GridBagLayout());
        appChoosePanel.setBackground(new Color(255, 255, 255));

        TitledBorder fileChooseBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "APP选择");
        appChoosePanel.setBorder(fileChooseBorder);

        appChooseLabel = new JLabel("应用选择:");
        localMsgFileField = new JTextField("/Users/lilizhao/data/all.dump");
        fileChooseBtn = new JButton("...");

        appChoosePanel.add(appChooseLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 30, 5, 5), 0, 0));
        appChoosePanel.add(localMsgFileField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 7, 5, 30), 0, 0));
        appChoosePanel.add(fileChooseBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 7, 5, 30), 0, 0));
    }

    /**
     * 功能绘制
     */
    private void initViewComponent() {
        setLayout(new BorderLayout());

        add(appChoosePanel, BorderLayout.CENTER);
    }
}