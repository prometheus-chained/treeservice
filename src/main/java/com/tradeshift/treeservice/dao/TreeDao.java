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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;
import java.util.OptionalLong;

@Service
@Transactional
public class TreeDao {
    private static final String INSERT_NODE = "INSERT INTO nodes (data, parent) VALUES (?, ?)";
    private static final String[] INSERT_NODE_COLUMNS = new String[]{"id"};
    private static final String SELECT_NODE = "SELECT id, data, parent FROM nodes WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TreeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Node save(Node node) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> createInsertStatement(con, node), keyHolder);

        Number key = keyHolder.getKey();
        node.setId(key == null ? -1 : key.longValue());
        return node;
    }

    public Optional<Node> findById(long id) {
        return jdbcTemplate.query(SELECT_NODE, new Object[]{id}, this::mapRow)
                           .stream()
                           .findFirst();
    }

    private Node mapRow(ResultSet rs, int rowNum) throws SQLException {
        long pId = rs.getLong(3);
        OptionalLong parent = rs.wasNull() ? OptionalLong.empty() : OptionalLong.of(pId);
        return Node.builder()
                   .id(rs.getLong(1))
                   .data(rs.getString(2))
                   .parent(parent)
                   .build();
    }

    private PreparedStatement createInsertStatement(Connection con, Node node) throws SQLException {
        PreparedStatement ps = con.prepareStatement(INSERT_NODE, INSERT_NODE_COLUMNS);
        ps.setString(1, node.getData());
        OptionalLong parent = node.getParent();
        if (parent.isPresent()) {
            ps.setLong(2, parent.getAsLong());
        } else {
            ps.setNull(2, Types.BIGINT);
        }
        return ps;
    }
}
