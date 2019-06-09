package com.tradeshift.treeservice;

import com.tradeshift.treeservice.dao.TreeDao;
import com.tradeshift.treeservice.domain.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TreeService {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final TreeDao treeDao;

    private Map<Long, Node> index = new ConcurrentHashMap<>();
    private Node root;

    @PostConstruct
    @Transactional(readOnly = true)
    public void loadTree() {
        lock.writeLock().lock();
        try {
            Optional<Node> optionalRoot = treeDao.findRoot();
            if (optionalRoot.isPresent()) {
                root = optionalRoot.get();
                index.put(root.getId(), root);
                loadChildNodes(optionalRoot.get());
                root.setNewHeight(0);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<NodeDTO> getTree() {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(root)
                           .map(NodeDTO::fromNode);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<NodeDTO> getSubTree(long id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(index.get(id))
                           .map(NodeDTO::fromNode);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Transactional
    public void moveNode(long nodeToMoveId, long newParentId) {
        lock.writeLock().lock();
        try {
            checkNodeExists(nodeToMoveId);
            checkNodeExists(newParentId);

            Node nodeToMove = index.get(nodeToMoveId);
            Node oldParent = index.get(nodeToMove.getParent()
                                                 .orElseThrow(() -> new IllegalArgumentException("Unable to move root node")));
            Node newParent = index.get(newParentId);

            oldParent.removeChild(nodeToMove);
            newParent.addChild(nodeToMove);
            nodeToMove.setParent(newParentId);

            treeDao.update(nodeToMove);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Transactional
    public void addSubTree(long parentId, NodeDTO nodeDTO) {
        Lock writeLock = this.lock.writeLock();
        try {
            checkNodeExists(parentId);

            Node parent = index.get(parentId);
            Node newNode = nodeDTO.toNode();
            newNode.setParent(parentId);
            saveSubTree(newNode);
            parent.addChild(newNode);
            newNode.setNewHeight(parent.getHeight() + 1);
        } finally {
            writeLock.unlock();
        }
    }

    @Transactional
    public void setNewTree(NodeDTO nodeDTO) {
        lock.writeLock().lock();
        try {
            if(root != null) {
                treeDao.deleteAll();
                index.clear();
            }
            Node newNode = nodeDTO.toNode();
            saveSubTree(newNode);
            newNode.setNewHeight(0);
            root = newNode;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void saveSubTree(Node node) {
        treeDao.save(node);
        long parentId = node.getId();
        index.put(parentId, node);
        for (Node chilren : node.getChilrens()) {
            chilren.setParent(parentId);
            saveSubTree(chilren);
        }
    }

    private void checkNodeExists(long id) {
        if (!index.containsKey(id)) {
            throw new IllegalArgumentException("Node #" + id + " not found");
        }
    }

    private void loadChildNodes(Node parent) {
        List<Node> childNodes = treeDao.findChildNodes(parent.getId());
        parent.getChilrens().addAll(childNodes);
        childNodes.forEach(node -> index.put(node.getId(), node));
        childNodes.forEach(this::loadChildNodes);
    }
}
