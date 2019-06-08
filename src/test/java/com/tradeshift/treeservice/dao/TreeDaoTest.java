package com.tradeshift.treeservice.dao;

import com.tradeshift.treeservice.domain.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TreeDaoTest {
    @Autowired
    public TreeDao treeDao;

    @Test
    public void insertTest() {
        Node root = treeDao.save(Node.builder()
                                     .height(0)
                                     .data("root")
                                     .build());
        System.out.println(root);
    }
}