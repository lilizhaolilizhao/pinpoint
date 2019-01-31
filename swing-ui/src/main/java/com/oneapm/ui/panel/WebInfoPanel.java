package com.oneapm.ui.panel;

import com.oneapm.ui.treetable.APMTreeTableModel;
import org.jdesktop.swingx.JXTreeTable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * 展示WEB事务信息
 */
public class WebInfoPanel extends JPanel {

    private JScrollPane msgLogPane;
    private APMTreeTableModel treeTableModel = new APMTreeTableModel();

    public WebInfoPanel() {
        initTextArea();
        initViewComponent();
    }

    private void initTextArea() {
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);

        msgLogPane = new JScrollPane(treeTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

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
