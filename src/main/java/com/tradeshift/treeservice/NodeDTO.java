package com.tradeshift.treeservice;

import com.tradeshift.treeservice.domain.Node;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NodeDTO {
    private long id;
    @EqualsAndHashCode.Exclude
    private int height;
    @EqualsAndHashCode.Exclude
    private String data;
    @EqualsAndHashCode.Exclude
    @Singular
    private List<NodeDTO> children;

    public Node toNode() {
        return Node.builder()
                   .id(id)
                   .height(height)
                   .data(data)
                   .children(children == null ?
                             new ArrayList<>() :
                             children.stream()
                                     .map(NodeDTO::toNode)
                                     .collect(Collectors.toList()))
                   .build();
    }

    public static NodeDTO fromNode(Node node) {
        return NodeDTO.builder()
                      .id(node.getId())
                      .height(node.getHeight())
                      .data(node.getData())
                      .children(node.getChildren()
                                    .stream()
                                    .map(NodeDTO::fromNode)
                                    .collect(Collectors.toSet()))
                      .build();
    }
}
