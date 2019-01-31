package com.oneapm.ui.panel;


import com.oneapm.action.kafka.KafkaTcpProducer;
import com.oneapm.ui.customer.FileOrDirectoryChooser;
import com.oneapm.ui.customer.JAdvancedComboBox;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class FileChoosePanel extends JPanel {

    private JPanel fileChoosePanel;
    private JPanel fileAdvanceConPanel;
    private JPanel sendMsgPanel;

    //文件选择面板组件
    private JLabel localMsgFileLabel;
    private JTextField localMsgFileField;
    private JButton fileChooseBtn;

    //文件高级配置面板组件
    private JLabel brokerLable;
    private JAdvancedComboBox brokerComboBox;
    private JLabel vlanIDLabel;
    private JTextField vlanIDField;
    private JLabel customProtocalLabel;
    private JTextField customProtocalField;
    private JLabel probeHostnameLabel;
    private JTextField probeHostnameField;

    //消息发送面板组件
    private JCheckBox timeStampBox;
    private JCheckBox loopSendBox;
    private JLabel sendRateLable;
    private JTextField sendRateField;
    private JLabel topicLabel;
    private JTextField topicField;
    private JToggleButton msgBtn;

    //kakfa生产者
    private KafkaTcpProducer kafkaTcpProducer;

    private Future<?> submitTask;

    public FileChoosePanel() {
        initViewPanel();
        initViewComponent();
        initlisteners();
    }

    /**
     * 添加监听事件
     */
    private void initlisteners() {
        final ExecutorService kafKaTaskPool = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "KafKaTaskPool");
            }
        });

        msgBtn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    msgBtn.setText("停止发送");

                    String brokerList = brokerComboBox.getSelectedItem().toString();
                    kafkaTcpProducer = new KafkaTcpProducer(brokerList);

                    final boolean timeStampResetFlag = timeStampBox.isSelected();
                    final boolean loopSendFlag = loopSendBox.isSelected();
                    final Integer sendRateInt = Integer.valueOf(sendRateField.getText().trim());
                    final String topic = topicField.getText().trim();
                    final String localMsgFile = localMsgFileField.getText().trim();
                    final String valnID = vlanIDField.getText().trim();
                    final String customProtocal = customProtocalField.getText().trim();
                    final String probeHostname = probeHostnameField.getText().trim();
                    submitTask = kafKaTaskPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            kafkaTcpProducer.produce(timeStampResetFlag, loopSendFlag, sendRateInt, topic, localMsgFile, valnID, customProtocal, probeHostname);
                        }
                    });
                } else {
                    submitTask.cancel(true);
                    msgBtn.setText("发送消息");
                }
            }
        });
        fileChooseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileOrDirectoryChooser folderChooser = new FileOrDirectoryChooser(localMsgFileField.getText(),
                        JFileChooser.FILES_ONLY, localMsgFileField);
                folderChooser.setDialogTitle("选择消息文件");
                folderChooser.showDialog(fileChooseBtn.getRootPane(), "选择消息文件");
            }
        });
    }

    /**
     * 初始化 panel
     */
    private void initViewPanel() {
        fileChoosePanel = new JPanel();
        fileChoosePanel.setLayout(new GridBagLayout());
        fileChoosePanel.setBackground(new Color(255, 255, 255));

        TitledBorder fileChooseBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "文件选择");
        fileChoosePanel.setBorder(fileChooseBorder);

        localMsgFileLabel = new JLabel("本地消息文件:");
        localMsgFileField = new JTextField("/Users/lilizhao/data/all.dump");
        fileChooseBtn = new JButton("...");

        fileChoosePanel.add(localMsgFileLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(2, 30, 5, 5), 0, 0));
        fileChoosePanel.add(localMsgFileField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 7, 5, 30), 0, 0));
        fileChoosePanel.add(fileChooseBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 7, 5, 30), 0, 0));

        fileAdvanceConPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fileAdvanceConPanel.setBackground(new Color(255, 255, 255));
        TitledBorder configBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "配置选择");
        fileAdvanceConPanel.setBorder(configBorder);

        probeHostnameLabel = new JLabel("探针主机名称");
        probeHostnameField = new JTextField("ni-local20", 10);
        brokerLable = new JLabel("broker配置:");

        Vector<String> brokers = new Vector<String>();
        brokers.add("127.0.0.1:9092");
        brokers.add("10.128.9.201:9092");

        brokerComboBox = new JAdvancedComboBox(brokers);
        brokerComboBox.setSelectedIndex(1);
        customProtocalLabel = new JLabel("自定义协议:");
        customProtocalField = new JTextField("lilizhao", 10);
        vlanIDLabel = new JLabel("vlanID:");
        vlanIDField = new JTextField("111", 13);

        fileAdvanceConPanel.add(probeHostnameLabel);
        fileAdvanceConPanel.add(probeHostnameField);
        fileAdvanceConPanel.add(brokerLable);
        fileAdvanceConPanel.add(brokerComboBox);
        fileAdvanceConPanel.add(customProtocalLabel);
        fileAdvanceConPanel.add(customProtocalField);
        fileAdvanceConPanel.add(vlanIDLabel);
        fileAdvanceConPanel.add(vlanIDField);

        sendMsgPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sendMsgPanel.setBackground(new Color(255, 255, 255));

        timeStampBox = new JCheckBox("重置时间戳", true);
        loopSendBox = new JCheckBox("循环发送", true);
        sendRateLable = new JLabel("发送频率(ms):");
        sendRateField = new JTextField("100", 5);
        topicLabel = new JLabel("Topic:");
        topicField = new JTextField("all", 5);
        msgBtn = new JToggleButton("发送消息");

        sendMsgPanel.add(timeStampBox);
        sendMsgPanel.add(loopSendBox);
        sendMsgPanel.add(sendRateLable);
        sendMsgPanel.add(sendRateField);
        sendMsgPanel.add(topicLabel);
        sendMsgPanel.add(topicField);
        sendMsgPanel.add(msgBtn);
    }

    /**
     * 功能绘制
     */
    private void initViewComponent() {
        setLayout(new BorderLayout());

        add(fileChoosePanel, BorderLayout.NORTH);
        add(fileAdvanceConPanel, BorderLayout.CENTER);
        add(sendMsgPanel, BorderLayout.SOUTH);
    }
}