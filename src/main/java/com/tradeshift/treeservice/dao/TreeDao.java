package com.tradeshift.treeservice.dao;

import com.tradeshift.treeservice.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Service
@Transactional
public class TreeDao {
    private static final String INSERT_NODE = "INSERT INTO nodes (height, data, parent) VALUES (?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TreeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Node save(Node node) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> createInsertStatement(con, node), keyHolder);

//        node.setId(keyHolder.getKey().longValue());
        return node;
    }

    public Node save(List<Node> nodes) {
        throw new RuntimeException("not implemented");
    }

    private PreparedStatement createInsertStatement(Connection con, Node node) throws SQLException {
        PreparedStatement ps = con.prepareStatement(INSERT_NODE);
        ps.setInt(1, node.getHeight());
        ps.setString(2, node.getData());
        Node parent = node.getParent();
        if (parent == null) {
            ps.setNull(3, Types.BIGINT);
        } else {
            ps.setLong(3, parent.getId());
        }
        return ps;
    }
}
