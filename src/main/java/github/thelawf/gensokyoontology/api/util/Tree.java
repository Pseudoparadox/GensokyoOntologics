package github.thelawf.gensokyoontology.api.util;

import github.thelawf.gensokyoontology.api.CanNullObject;

import java.util.*;
import java.util.stream.Collectors;

public class Tree<T> {

    public final Node<T> root;

    public Tree() {
        this.root = new Node<>("", null, null);
    }

    public Tree(String rootName) {
        this.root = new Node<>(rootName, null, null);
    }

    public Tree(Node<T> root) {
        this.root = root;
    }

    public void addNode(String path, T data) {
        List<String> parts = parsePath(path);
        Node<T> currentNode = root;

        for (String part : parts) {
            Node<T> finalCurrentNode = currentNode;
            currentNode = currentNode.getChildren().computeIfAbsent(part, p -> new Node<>(p, null, finalCurrentNode));
        }

        currentNode.setData(data);
    }

    public boolean deleteNode(String path) {
        List<String> parts = parsePath(path);
        if (parts.isEmpty()) {
            return false; // Cannot delete root
        }

        Node<T> parentNode = findNode(parts.subList(0, parts.size() - 1));
        if (parentNode == null) {
            return false;
        }

        String targetNodeName = parts.get(parts.size() - 1);
        return parentNode.getChildren().remove(targetNodeName) != null;
    }

    public T getData(String path) {
        Node<T> node = findNode(parsePath(path));
        return (node != null) ? node.getData() : null;
    }

    public boolean setData(String path, T data) {
        Node<T> node = findNode(parsePath(path));
        if (node == null) {
            return false;
        }

        node.setData(data);
        return true;
    }

    public Node<T> findNode(List<String> pathParts) {
        Node<T> currentNode = root;
        for (String part : pathParts) {
            currentNode = currentNode.getChildren().get(part);
            if (currentNode == null) {
                return null;
            }
        }
        return currentNode;
    }


    public CanNullObject<Node<T>> find(String path) {
        if (path == null || path.isEmpty()) {
            return CanNullObject.empty();
        }
        else  {
            return CanNullObject.of(findNode(parsePath(path)));
        }
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

    public static class Node<T> {
        private final String name;
        private final Node<T> parent;
        private final Map<String, Node<T>> children;
        private T data;

        public Node(String name, T data, Node<T> parent) {
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

        public Node<T> getParent() {
            return parent;
        }

        public Map<String, Node<T>> getChildren() {
            return children;
        }
    }
}
