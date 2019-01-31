package com.oneapm.action.kafka;

import com.oneapm.ui.frame.MainFrame;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * 读取topic调整 tcp2->tcp
 * Created by lilizhao on 16-1-15.
 */
public class KafkaTcpProducer {
    private static Logger log = Logger.getLogger(KafkaTcpProducer.class);

    private final Producer<String, String> producer;

    public KafkaTcpProducer(String brokerList) {
        Properties props = new Properties();
        props.put("metadata.broker.list", brokerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "-1");

        producer = new Producer<String, String>(new ProducerConfig(props));
    }

    /**
     * 消息生产
     *
     * @param timeStampResetFlag
     * @param loopSendFlag
     * @param sendRateInt
     * @param topic
     * @param localMsgFile
     * @param valnID
     * @param customProtocal
     * @param probeHostname
     * @throws Exception
     */
    public void produce(boolean timeStampResetFlag, boolean loopSendFlag, Integer sendRateInt, String topic, String localMsgFile, String valnID,
                        String customProtocal, String probeHostname) {
        JTextArea messageTextArea = MainFrame.messagePanel.getMessageTextArea();
        String oldText = messageTextArea.getText();

        BufferedReader br = null;
        try {
            int i = 0;
            do {
                br = new BufferedReader(new FileReader(localMsgFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (!"".equals(line.trim())) {

                        String[] words = line.split("\t", 26);

                        //重置时间戳
                        if (timeStampResetFlag) {
                            Double d = new Date(System.currentTimeMillis()).getTime() / 1000.0;
                            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                            nf.setGroupingUsed(false);
                            words[1] = String.valueOf(nf.format(d));
                        }

                        if (oldText.length() > 100000) {
                            oldText = oldText.substring(100000);
                        }

                        if (!"".equals(customProtocal)) {
                            words[9] = customProtocal;
                        }

                        if (!"".equals(valnID)) {
                            words[10] = valnID;
                        }

                        String joinWords = StringUtils.join(words, "\t");

                        oldText = oldText + "\n" + probeHostname + ":" + (++i) + "\t" + joinWords;
                        messageTextArea.setText(oldText);
                        scrollToEnd(messageTextArea);

                        producer.send(new KeyedMessage<String, String>(topic, probeHostname, "test_llz", joinWords));
                    }

                    if (sendRateInt > 0) {
                        Thread.sleep(sendRateInt);
                    }
                }
            } while (loopSendFlag);
        } catch (Exception e) {
            oldText = oldText + "\n" + e.getMessage();
            messageTextArea.setText(oldText);

            scrollToEnd(messageTextArea);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.warn("close the read streaming error: ", e);
                }
            }
        }

    }

    //设置面板滚动到低端
    private void scrollToEnd(JTextArea messageTextArea) {
        JScrollBar bar = ((JScrollPane) messageTextArea.getParent().getParent()).getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
    }
}