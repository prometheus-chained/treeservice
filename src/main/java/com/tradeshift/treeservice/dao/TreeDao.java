package com.tradeshift.treeservice.dao;

import com.tradeshift.treeservice.domain.Node;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;

@Service
public interface TreeDao extends Neo4jRepository<Node, Long> {
}
