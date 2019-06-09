package com.tradeshift.treeservice.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.OptionalLong;
import java.util.Set;

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
    private OptionalLong parent = OptionalLong.empty();
    @ToString.Exclude
    private Set<Node> chilrens;

    private void setNewHeight(int parentHeight) {
        height = parentHeight + 1;
        chilrens.forEach(node -> node.setNewHeight(height));
    }
}
