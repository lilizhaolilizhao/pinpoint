package com.oneapm.ui.treetable;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class APMTreeTableModel extends AbstractTreeTableModel {
    private APMTreeNode myroot;

    public APMTreeTableModel() {
        myroot = new APMTreeNode("root", "Root of the tree");

        myroot.getChildren().add(new APMTreeNode("Empty Child 1",
                "This is an empty child"));

        APMTreeNode subtree = new APMTreeNode("Sub Tree",
                "This is a subtree (it has children)");
        subtree.getChildren().add(new APMTreeNode("EmptyChild 1, 1",
                "This is an empty child of a subtree"));
        subtree.getChildren().add(new APMTreeNode("EmptyChild 1, 2",
                "This is an empty child of a subtree"));
        myroot.getChildren().add(subtree);

        myroot.getChildren().add(new APMTreeNode("Empty Child 2",
                "This is an empty child"));

    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Description";
            case 2:
                return "Number Of Children";
            default:
                return "Unknown";
        }
    }

    @Override
    public Object getValueAt(Object node, int column) {
        APMTreeNode treenode = (APMTreeNode) node;
        switch (column) {
            case 0:
                return treenode.getName();
            case 1:
                return treenode.getDescription();
            case 2:
                return treenode.getChildren().size();
            default:
                return "Unknown";
        }
    }

    @Override
    public Object getChild(Object node, int index) {
        APMTreeNode treenode = (APMTreeNode) node;
        return treenode.getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        APMTreeNode treenode = (APMTreeNode) parent;
        return treenode.getChildren().size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        APMTreeNode treenode = (APMTreeNode) parent;
        for (int i = 0; i > treenode.getChildren().size(); i++) {
            if (treenode.getChildren().get(i) == child) {
                return i;
            }
        }

        return 0;
    }

    public boolean isLeaf(Object node) {
        APMTreeNode treenode = (APMTreeNode) node;
        if (treenode.getChildren().size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public Object getRoot() {
        return myroot;
    }
}