package com.oneapm.action.menuaction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 功能描述：为所有的菜单子项添加响应动作
 */
public class MenuActionListener implements ActionListener {

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source instanceof JMenuItem) {
            JMenuItem jMenuItem = (JMenuItem) source;
            String actionCommand = jMenuItem.getActionCommand();

            if ("frame.menu.file.exit".equals(actionCommand)) {
                System.exit(0);
            }
        }
    }

}