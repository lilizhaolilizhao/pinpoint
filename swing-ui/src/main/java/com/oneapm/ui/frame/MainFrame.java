package com.oneapm.ui.frame;

import com.oneapm.ui.menu.MainMenuBar;
import com.oneapm.ui.panel.AppChoosePanel;
import com.oneapm.ui.panel.MessagePanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static MainMenuBar mainMenuBar;
    public static AppChoosePanel appChoosePanel;
    public static MessagePanel messagePanel;

    private MainFrame() throws HeadlessException {
        setLookAndFeel();
        initViewComponent();
        initFrame();
    }


    /**
     * 设置界面风格
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化界面组件
     */
    private void initViewComponent() {
        mainMenuBar = new MainMenuBar("/menu/menu.xml");

        appChoosePanel = new AppChoosePanel();
        appChoosePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        appChoosePanel.setBackground(new Color(252, 252, 252));

        messagePanel = new MessagePanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        messagePanel.setBackground(new Color(252, 252, 252));
    }

    /**
     * 初始化主Frame
     */
    private void initFrame() {
        setTitle("Java探针UI");
        setSize(970, 680);

        setLayout(new BorderLayout());

        setJMenuBar(mainMenuBar);
        add(appChoosePanel, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 显示功能界面
     */
    private void showUI() {
        // frame初始化到屏幕中央
        int pw = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int w = this.getWidth();
        int ph = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int h = this.getHeight();
        setLocation((pw - w) / 2, (ph - h) / 2);

        setIconImage(Toolkit.getDefaultToolkit().getImage("frame/tool.png"));
        setVisible(true);
    }

    public static void main(String[] args) {
        // 获取开始时间
        long startTime = System.currentTimeMillis();

        new MainFrame().showUI();

        // 获取结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("工具启动用时： " + (endTime - startTime) * 1.0 / 1000 + "s");
    }

}