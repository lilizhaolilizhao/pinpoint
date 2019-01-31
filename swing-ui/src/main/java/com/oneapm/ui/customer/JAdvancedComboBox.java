package com.oneapm.ui.customer;


import javax.swing.*;
import java.util.List;
import java.util.Vector;

public class JAdvancedComboBox extends JComboBox {
    public JAdvancedComboBox() {
        addCompleter();
    }

    public JAdvancedComboBox(ComboBoxModel cm) {
        super(cm);
        addCompleter();
    }

    public JAdvancedComboBox(Object[] items) {
        super(items);
        addCompleter();
    }

    @SuppressWarnings("rawtypes")
    public JAdvancedComboBox(List v) {
        super((Vector) v);
        addCompleter();
    }

    private void addCompleter() {
        setEditable(true);
    }

    public String getText() {
        return ((JTextField) getEditor().getEditorComponent()).getText();
    }

    public void setText(String text) {
        ((JTextField) getEditor().getEditorComponent()).setText(text);
    }

    public boolean containsItem(String itemString) {
        for (int i = 0; i < this.getModel().getSize(); i++) {
            String _item = " " + this.getModel().getElementAt(i);
            if (_item.equals(itemString))
                return true;
        }
        return false;
    }
}
