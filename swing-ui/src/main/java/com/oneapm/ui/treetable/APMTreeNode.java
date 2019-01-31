package com.oneapm.ui.treetable;

import java.util.ArrayList;
import java.util.List;

class APMTreeNode {
    private String name;
    private String description;
    private List<APMTreeNode> children = new ArrayList<APMTreeNode>();

    public APMTreeNode() {
    }

    public APMTreeNode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<APMTreeNode> getChildren() {
        return children;
    }

    public String toString() {
        return "APMTreeNode: " + name + ", " + description;
    }
}
