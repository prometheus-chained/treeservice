package com.tradeshift.treeservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TreeServiceTest {
    private static final NodeDTO root = NodeDTO.builder()
                                               .data("root")
                                               .chilren(NodeDTO.builder()
                                                               .data("a")
                                                               .chilren(NodeDTO.builder()
                                                                               .data("c")
                                                                               .build())
                                                               .build())
                                               .chilren(NodeDTO.builder()
                                                               .data("b")
                                                               .build())
                                               .build();

    @Autowired
    public TreeService treeService;

    @Test
    public void setNewTree() {
        treeService.setNewTree(root);

        Optional<NodeDTO> optional = treeService.getTree();
        assertTrue(optional.isPresent());

        NodeDTO rootDto = optional.get();
        assertEquals("root", rootDto.getData());
        assertEquals(0, rootDto.getHeight());
        List<NodeDTO> chilrens = rootDto.getChilrens();
        assertEquals(asList("a", "b"),
                     chilrens.stream().map(NodeDTO::getData).sorted().collect(toList()));

        Set<String> cNode = chilrens.stream()
                                    .filter(n -> n.getData().equals("a"))
                                    .map(NodeDTO::getChilrens)
                                    .flatMap(Collection::stream)
                                    .map(NodeDTO::getData)
                                    .collect(Collectors.toSet());
        assertEquals(singleton("c"), cNode);
    }

    @Test
    public void moveSubTree() {
        treeService.setNewTree(root);

        Optional<NodeDTO> root = treeService.getTree();
        assertTrue(root.isPresent());

        List<NodeDTO> chilrens = root.get().getChilrens();
        NodeDTO a = getNode(chilrens, "a");
        NodeDTO b = getNode(chilrens, "b");
        NodeDTO c = getNode(a.getChilrens(), "c");

        treeService.moveNode(c.getId(), b.getId());

        root = treeService.getTree();
        assertTrue(root.isPresent());

        chilrens = root.get().getChilrens();
        a = getNode(chilrens, "a");
        b = getNode(chilrens, "b");
        c = getNode(b.getChilrens(), "c");
        assertTrue(a.getChilrens().isEmpty());
    }

    private NodeDTO getNode(List<NodeDTO> chilrens, String data) {
        return chilrens.stream()
                       .filter(node -> node.getData().equals(data))
                       .findFirst()
                       .orElseThrow(() -> new RuntimeException("Node a not found"));
    }
}