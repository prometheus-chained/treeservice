package com.tradeshift.treeservice.domain;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@Builder
@NodeEntity
public class Node {
    @Id
    @GeneratedValue
    private long id;
    private long parent;
    private String data;
}
