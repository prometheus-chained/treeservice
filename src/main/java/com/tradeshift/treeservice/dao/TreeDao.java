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
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Service
@Transactional
public class TreeDao {
    private static final String INSERT_NODE = "INSERT INTO nodes (data, parent) VALUES (?, ?)";
    private static final String[] INSERT_NODE_COLUMNS = new String[]{"id"};
    private static final String SELECT_NODE = "SELECT id, data, parent FROM nodes WHERE id = ?";
    private static final String SELECT_ROOT_NODE = "SELECT id, data, parent FROM nodes WHERE parent IS NULL";
    private static final String SELECT_CHILD_NODES = "SELECT id, data, parent FROM nodes WHERE parent = ?";
    private static final String DELETE_ALL = "DELETE FROM nodes WHERE id IS NOT NULL";
    private static final String UPDATE_NODE = "UPDATE nodes SET data = ?, parent = ? WHERE id = ?";
    private static final int[] UPDATE_NODE_TYPEs = {Types.VARCHAR, Types.BIGINT, Types.BIGINT};

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TreeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Node node) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> createInsertStatement(con, node), keyHolder);

        Number key = keyHolder.getKey();
        node.setId(key == null ? -1 : key.longValue());
    }

    public void update(Node node) {
        Long parent = node.getParent().isPresent() ? node.getParent().getAsLong() : null;
        jdbcTemplate.update(UPDATE_NODE,
                            new Object[]{node.getData(), parent, node.getId()},
                            UPDATE_NODE_TYPEs);
    }

    public Optional<Node> findById(long id) {
        return jdbcTemplate.query(SELECT_NODE, new Object[]{id}, this::mapRow)
                           .stream()
                           .findFirst();
    }

    public Optional<Node> findRoot() {
        List<Node> result = jdbcTemplate.query(SELECT_ROOT_NODE, this::mapRow);
        if (result.size() > 1) {
            throw new RuntimeException("Internal error, duplicated root nodes");
        }
        return result.isEmpty() ?
               Optional.empty() :
               Optional.of(result.get(0));
    }

    public List<Node> findChildNodes(long parentId) {
        return jdbcTemplate.query(SELECT_CHILD_NODES, new Object[]{parentId}, this::mapRow);
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL);
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
