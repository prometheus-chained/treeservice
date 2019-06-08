package com.tradeshift.treeservice.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Node {
    private long id;
    private int height;
    private String data;
    private Node parent;
    private Set<Node> chilrens;

    private void setNewHeight(int parentHeight) {
        height = parentHeight + 1;
        chilrens.forEach(node -> node.setNewHeight(height));
    }
}
