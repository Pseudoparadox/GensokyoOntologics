package com.github.fictology.gensokyoontology.util.api;

import java.util.*;
import java.util.stream.Collectors;

public class Tree<T> {

    public final TreeNode<T> root;

    public Tree() {
        this.root = new TreeNode<>("", null, null);
    }

    public void addNode(String path, T data) {
        List<String> parts = parsePath(path);
        TreeNode<T> currentNode = root;

        for (String part : parts) {
            TreeNode<T> finalCurrentNode = currentNode;
            currentNode = currentNode.getChildren().computeIfAbsent(part, p -> new TreeNode<>(p, null, finalCurrentNode));
        }

        currentNode.setData(data);
    }

    public boolean deleteNode(String path) {
        List<String> parts = parsePath(path);
        if (parts.isEmpty()) {
            return false; // Cannot delete root
        }

        TreeNode<T> parentNode = findNode(parts.subList(0, parts.size() - 1));
        if (parentNode == null) {
            return false;
        }

        String targetNodeName = parts.get(parts.size() - 1);
        return parentNode.getChildren().remove(targetNodeName) != null;
    }

    public T getData(String path) {
        TreeNode<T> node = findNode(parsePath(path));
        return (node != null) ? node.getData() : null;
    }

    public boolean setData(String path, T data) {
        TreeNode<T> node = findNode(parsePath(path));
        if (node == null) {
            return false;
        }

        node.setData(data);
        return true;
    }

    private TreeNode<T> findNode(List<String> pathParts) {
        TreeNode<T> currentNode = root;
        for (String part : pathParts) {
            currentNode = currentNode.getChildren().get(part);
            if (currentNode == null) {
                return null;
            }
        }
        return currentNode;
    }

    private List<String> parsePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return Collections.emptyList();
        }

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must be absolute: " + path);
        }

        return Arrays.stream(path.split("/+"))
                .filter(part -> !part.isEmpty())
                .collect(Collectors.toList());
    }

    public static class TreeNode<T> {
        private final String name;
        private final TreeNode<T> parent;
        private final Map<String, TreeNode<T>> children;
        private T data;

        public TreeNode(String name, T data, TreeNode<T> parent) {
            this.name = name;
            this.data = data;
            this.parent = parent;
            this.children = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public TreeNode<T> getParent() {
            return parent;
        }

        public Map<String, TreeNode<T>> getChildren() {
            return children;
        }
    }
}
