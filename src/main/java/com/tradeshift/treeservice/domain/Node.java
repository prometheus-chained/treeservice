package com.tradeshift.treeservice.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import static java.util.OptionalLong.empty;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Node {
    @EqualsAndHashCode.Include
    private long id;
    private int height;
    private String data;
    @Builder.Default
    private OptionalLong parent = empty();
    @ToString.Exclude
    @Builder.Default
    private List<Node> children = new ArrayList<>();

    public void setNewHeight(int newHeight) {
        height = newHeight;
        children.forEach(node -> node.setNewHeight(newHeight + 1));
    }

    public void removeChild(Node node) {
        children.remove(node);
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public void setParent(long parentId) {
        parent = OptionalLong.of(parentId);
    }

    public void setNoParent() {
        parent = OptionalLong.empty();
    }
}
