package com.tradeshift.treeservice.dao;

import com.tradeshift.treeservice.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TreeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Node save(Node node) {
        throw new RuntimeException("not implemented");
    }

    public Node save(List<Node> nodes) {
        throw new RuntimeException("not implemented");
    }
}
