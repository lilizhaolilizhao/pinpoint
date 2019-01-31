package com.oneapm.ui.customer;

import javax.swing.*;
import java.io.File;

public class FileOrDirectoryChooser extends JFileChooser {

    private JTextField textField;

    public FileOrDirectoryChooser(String filePath, int fileSelectionMode, JTextField textField) {
        super(new File(filePath).isDirectory() ? new File(filePath) : new File(filePath).getParentFile());
        setFileSelectionMode(fileSelectionMode);
        if (!new File(filePath).isDirectory()) {
            setSelectedFile(new File(filePath));
        }
        this.textField = textField;
    }

    public void approveSelection() {
//		super.approveSelection();
        //设置当前打开的目录为默认目录
        this.setCurrentDirectory(this.getSelectedFile());
        selectFolder(this.getSelectedFile().getAbsolutePath());
//        this.getParent().getParent().setVisible(false);
//        this.hide();
        super.approveSelection();
    }

    private void selectFolder(String absolutePath) {
        textField.setText(absolutePath);
    }
}
