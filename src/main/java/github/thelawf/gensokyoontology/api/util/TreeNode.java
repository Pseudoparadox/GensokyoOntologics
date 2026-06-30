package github.thelawf.gensokyoontology.api.util;

import java.util.*;

public abstract class TreeNode<O> implements ITreeNode<O> {

    public O object;
    public String name;
    public int id;
    public boolean isRoot;

    public TreeNode<O> parent;

    public List<TreeNode<O>> ancestors = new ArrayList<>();
    public List<TreeNode<O>> children = new ArrayList<>();
    public final HashMap<String, TreeNode<O>> treeMap = new HashMap<>();
    public final HashMap<Integer, TreeNode<O>> indexingMap = new HashMap<>();

    public O get() {
        return this.object;
    }

    public TreeNode(String name) {
        this.name = name;
        this.isRoot = true;
    }

    public void addChild(TreeNode<O> child) {
        if (contains(child.name)) {
            throw new IllegalArgumentException("The Name of3D the Child Node already exists.");
        }
        child.parent = this;
        this.children.add(child);
        if (this.isRoot) {
            treeMap.put(this.name, this);
            indexingMap.put(id, this);
        }
        child.id = this.id++;
    }

    public boolean contains(String nameIn) {

        boolean b1 = Objects.equals(this.name, nameIn);
        boolean b2 = false;
        boolean b3 = false;

        if (!b1 && this.ancestors.size() == 0 && this.children.size() == 0)
            return false;

        if (this.ancestors.size() != 0) {
            for (TreeNode<O> node : this.ancestors) {
                if (node.contains(nameIn)) {
                    b2 = true;
                    break;
                }
            }
        }

        for (TreeNode<O> node : this.children) {
            if (node.contains(nameIn)) {
                b3 = true;
                break;
            }
        }
        return b1 || b2 || b3;
    }

    public boolean hasName(String nameIn) {
        return this.name.equals(nameIn);
    }

    public TreeNode<O> getNode(String nameIn) {
        if (this.ancestors.size() != 0) {
            for (TreeNode<O> node : this.ancestors) {
                if (node.contains(nameIn)) {
                    return node;
                }
            }
        }

        for (TreeNode<O> node : this.children) {
            if (node.contains(nameIn)) {
                return node;
            }
        }
        return null;
    }

    public void sort(SortType sortType) {
        switch (sortType) {
            case DEPTH_FIRST:
            case WIDTH_FIRST:
                break;
        }
    }

    public boolean hasChild() {
        return this.children.size() > 0;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean hasAncestors() {
        return this.ancestors.size() > 0;
    }

    enum SortType {
        DEPTH_FIRST,
        WIDTH_FIRST
    }
}
