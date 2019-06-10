# Tree Service app

## Task description
We in *Amazing Co* need to model how our company is structured so we can do awesome stuff.

We have a root node (only one) and several children nodes,each one with its own children as well. It's a tree-based structure. 

Something like:
```
         root

        /    \

       a      b

       |

       c
```
We need two HTTP APIs that will serve the two basic operations:

1) Get all children nodes of a given node (the given node can be anyone in the tree structure).

2) Change the parent node of a given node (the given node can be anyone in the tree structure).

They need to answer quickly, even with tons of nodes. Also,we can't afford to lose this information, so some sort of persistence is required.

Each node should have the following info:

1) node identification

2) who is the parent node

3) who is the root node

4) the height of the node. In the above example,height(root) = 0 and height(a) == 1.

We can only have docker and docker-compose on our machines, so your server needs to be ran using them.

Please push your solution to GITHUB and share it with us so we can review it.


## API 


### Read tree
```
GET http://localhost:8080/subtree/
Accept: application/json
```
Will return JSON with whole tree or empty body if there is no tree.


### Read subtree
```
GET http://localhost:8080/subtree/{id}
Accept: application/json
```
Will return JSON with subtree starting from node with given `id`.
Will return 404 if node with given `id` not found.


### Save new tree
```
POST http://localhost:8080/newtree/
Content-Type: application/json
Accept: application/json

{
  "data": "root",
  "children": [
    {
      "data": "a",
      "children": [
        {
          "data": "c"
        }
      ]
    },
    {
      "data": "b",
      "children": [
        {
          "data": "d"
        },
        {
          "data": "e"
        }
      ]
    }
  ]
}
```
Will create new tree. If tree already exist old one would be deleted.


### Add new subtree
```
POST http://localhost:8080/addsubtree/{id}
Content-Type: application/json
Accept: application/json

{
  "data": "foo",
  "children": [
    {
      "data": "bar",
      "children": []
    }
  ]
}
```
Add new subtree to existing node with given `id`.
Will return added subtree.
Will return 404 if node with given `id` does not exist.


### Move subtree to other parent
```
GET http://localhost:8080/move/{nodeId}/to/{newParentId}
Accept: application/json
```
Will move node with given `nodeId` to new parent with given `newParentId`.
Will return subtree for given `nodeId` after it moved to new parent.
Will return 404 if `nodeId` or `newParentId` not found.
Will return 400 if `nodeId` is root node.


## How to run
```bash
./gradlew bootRun
```