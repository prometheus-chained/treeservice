package com.tradeshift.treeservice.dao;

import com.tradeshift.treeservice.domain.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TreeDaoTest {
    @Autowired
    public TreeDao treeDao;

    @Test
    public void insertTest() {
        Node root = Node.builder()
                        .height(0)
                        .data("root")
                        .build();
        treeDao.save(root);
        assertNotEquals(-1, root.getId());
        Optional<Node> optionalNode = treeDao.findById(root.getId());
        assertTrue(optionalNode.isPresent());
        assertEquals(root.getId(), optionalNode.get().getId());
        assertEquals(root.getData(), optionalNode.get().getData());
    }
}