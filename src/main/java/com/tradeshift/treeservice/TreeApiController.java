package com.tradeshift.treeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@RestController
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TreeApiController {
    private final TreeService treeService;

    @Autowired
    public TreeApiController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GET
    @Path("subtree/{id: (\\\\d+)?}")
    public Response getSubtree(@PathParam("id") Long id) {
        Optional<NodeDTO> nodeDTO = id == null ? treeService.getTree() : treeService.getSubTree(id);
        return nodeDTO.map(n -> Response.ok(n).build())
                      .orElse(Response.ok().build());
    }

    @POST
    @Path("newtree")
    public Response newTree(NodeDTO tree) {
        NodeDTO nodeDTO = treeService.setNewTree(tree);
        return Response.ok(nodeDTO).build();
    }

    @POST
    @Path("addsubtree/{id}")
    public Response newSubTree(@PathParam("id") long id, NodeDTO tree) {
        NodeDTO nodeDTO = treeService.addSubTree(id, tree);
        return Response.ok(nodeDTO).build();
    }

    @GET
    @Path("move/{nodeId}/to/{newParentId}")
    public Response moveSubTree(@PathParam("nodeId") long nodeId, @PathParam("newParentId") long newParentId) {
        NodeDTO nodeDTO = treeService.moveNode(nodeId, newParentId);
        return Response.ok(nodeDTO).build();
    }
}
