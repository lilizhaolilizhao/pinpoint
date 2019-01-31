package com.oneapm.ui.panel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by lilizhao on 16-3-12.
 */
public class MessagePanel extends JPanel {

    private JTextArea messageTextArea;
    private JScrollPane msgLogPane;

    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }

    public MessagePanel() {
        initTextArea();
        initViewComponent();
    }

    private void initTextArea() {
        messageTextArea = new JTextArea("kafka消费日志:");
        messageTextArea.setEditable(false);

        msgLogPane = new JScrollPane(messageTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        TitledBorder msgLogBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "消息日志");
        msgLogPane.setBorder(msgLogBorder);
    }

    /**
     * 功能绘制
     */
    private void initViewComponent() {
        setLayout(new BorderLayout());

        add(msgLogPane, BorderLayout.CENTER);
    }
}
