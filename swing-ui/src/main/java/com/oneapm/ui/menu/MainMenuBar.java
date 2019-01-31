package com.oneapm.ui.menu;

import com.oneapm.action.menuaction.MenuActionListener;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MainMenuBar extends JMenuBar {

    private String xpath;

    public MainMenuBar(String xpath) {
        this.xpath = xpath;
        initMainMenus();
    }

    /**
     * 功能描述：初始化菜单项
     *
     * @param
     */
    private void initMainMenus() {
        try {
            MenuActionListener menuActionListener = new MenuActionListener();
            Map<String, ButtonGroup> buttonGroups = new HashMap<String, ButtonGroup>();

            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(this.getClass().getResource(xpath));
            Element rootElement = document.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> menuEles = XPath.selectNodes(rootElement, "/menus/frame/menu");
            ListIterator<Element> elementListIterator = menuEles.listIterator();
            while (elementListIterator.hasNext()) {
                Element nextMenuEle = elementListIterator.next();
                String currentMenuName = nextMenuEle.getAttributeValue("name");
                String mnemonic_key = nextMenuEle.getAttributeValue("mnemonic_key");

                JMenu jMenu = new JMenu(currentMenuName);

                if (mnemonic_key != null) {
                    jMenu.setMnemonic(mnemonic_key.charAt(0));
                }

                @SuppressWarnings("unchecked")
                List<Element> menuItemEles = nextMenuEle.getChildren();
                ListIterator<Element> elementListIterator1 = menuItemEles.listIterator();
                while (elementListIterator1.hasNext()) {
                    Element menuObjEle = elementListIterator1.next();

                    if ("menu".equals(menuObjEle.getName())) {
                        String subCurrentMenuName = menuObjEle.getAttributeValue("name");
                        String subMnemonic_key = menuObjEle.getAttributeValue("mnemonic_key");

                        JMenu subjMenu = new JMenu(subCurrentMenuName);
                        if (subMnemonic_key != null) {
                            subjMenu.setMnemonic(subMnemonic_key.charAt(0));
                        }

                        @SuppressWarnings("unchecked")
                        List<Element> subMenuItemEles = menuObjEle.getChildren();
                        ListIterator<Element> listIterator = subMenuItemEles.listIterator();
                        while (listIterator.hasNext()) {
                            Element subMenuItemEle = listIterator.next();
                            createMenuItem(menuActionListener, buttonGroups, subjMenu, subMenuItemEle);
                        }

                        jMenu.add(subjMenu);
                    } else {
                        Element menuItemEle = menuObjEle;
                        createMenuItem(menuActionListener, buttonGroups, jMenu, menuItemEle);
                    }
                }

                add(jMenu);
            }

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：创建菜单项
     *
     * @param menuActionListener
     * @param buttonGroups
     * @param jMenu
     * @param menuItemEle
     */
    private void createMenuItem(MenuActionListener menuActionListener,
                                Map<String, ButtonGroup> buttonGroups, JMenu jMenu,
                                Element menuItemEle) {
        URL url = null;
        ImageIcon imageIcon = null;

        String menuItemName = menuItemEle.getAttributeValue("name");
        String actioncommand = menuItemEle.getAttributeValue("actioncommand");
        String menuItemType = menuItemEle.getAttributeValue("type");
        String groupType = menuItemEle.getAttributeValue("group");
        String accelerate_key = menuItemEle.getAttributeValue("accelerate_key");
        String imagepath = menuItemEle.getAttributeValue("imagepath");
        String selected = menuItemEle.getAttributeValue("selected");

        if (imagepath != null) {
            url = this.getClass().getResource(imagepath);

            if (url != null) {
                imageIcon = new ImageIcon(url.getPath());
            }
        }

        if ("common".equals(menuItemType)) {
            JMenuItem jMenuItem = new JMenuItem(menuItemName);

            if (imageIcon != null) {
                jMenuItem.setIcon(imageIcon);
            }

            jMenuItem.setActionCommand(actioncommand);
            jMenuItem.addActionListener(menuActionListener);
            setAccelerateKey(accelerate_key, jMenuItem);
            jMenu.add(jMenuItem);
        } else if ("radio".equals(menuItemType)) {
            JRadioButtonMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem(menuItemName);

            if (jRadioButtonMenuItem != null) {
                jRadioButtonMenuItem.setIcon(imageIcon);
            }

            jRadioButtonMenuItem.setActionCommand(actioncommand);
            jRadioButtonMenuItem.addActionListener(menuActionListener);

            if (buttonGroups.get(groupType) != null) {
                buttonGroups.get(groupType).add(jRadioButtonMenuItem);
            } else {
                ButtonGroup buttonGroup = new ButtonGroup();
                buttonGroup.add(jRadioButtonMenuItem);
                buttonGroups.put(groupType, buttonGroup);
            }

            if ("true".equals(selected)) {
                jRadioButtonMenuItem.setSelected(true);
            }

            setAccelerateKey(accelerate_key, jRadioButtonMenuItem);
            jMenu.add(jRadioButtonMenuItem);
        } else if ("checkbox".equals(menuItemType)) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(menuItemName);

            if (jCheckBoxMenuItem != null) {
                jCheckBoxMenuItem.setIcon(imageIcon);
            }

            jCheckBoxMenuItem.setActionCommand(actioncommand);
            jCheckBoxMenuItem.addActionListener(menuActionListener);

            setAccelerateKey(accelerate_key, jCheckBoxMenuItem);
            jMenu.add(jCheckBoxMenuItem);
        } else {
            jMenu.addSeparator();
        }
    }

    /**
     * 设置快捷键
     *
     * @param accelerate_key
     * @param jMenuItem
     */
    private void setAccelerateKey(String accelerate_key, JMenuItem jMenuItem) {
        if (accelerate_key != null) {
            if (!accelerate_key.contains("+")) {
                jMenuItem.setAccelerator(KeyStroke.getKeyStroke(accelerate_key));
            } else {
                String[] splits = accelerate_key.split("\\+");
                StringBuilder builder = new StringBuilder();

                for (String split : splits) {
                    builder.append(split).append(" ");
                }
                jMenuItem.setAccelerator(KeyStroke.getKeyStroke(builder.toString()));
            }
        }
    }


}