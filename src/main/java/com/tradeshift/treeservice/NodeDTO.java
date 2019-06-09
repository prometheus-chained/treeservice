package com.tradeshift.treeservice;

import com.tradeshift.treeservice.domain.Node;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
public class NodeDTO {
    private final long id;
    @EqualsAndHashCode.Exclude
    private final int height;
    @EqualsAndHashCode.Exclude
    private final String data;
    @EqualsAndHashCode.Exclude
    @Singular
    private final List<NodeDTO> chilrens;

    public Node toNode() {
        return Node.builder()
                   .id(id)
                   .height(height)
                   .data(data)
                   .chilrens(chilrens.stream()
                                     .map(NodeDTO::toNode)
                                     .collect(Collectors.toList()))
                   .build();
    }

    public static NodeDTO fromNode(Node node) {
        return NodeDTO.builder()
                      .id(node.getId())
                      .height(node.getHeight())
                      .data(node.getData())
                      .chilrens(node.getChilrens()
                                    .stream()
                                    .map(NodeDTO::fromNode)
                                    .collect(Collectors.toSet()))
                      .build();
    }
}
