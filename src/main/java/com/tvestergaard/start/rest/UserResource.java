package com.tvestergaard.start.rest;

import com.google.gson.Gson;
import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.data.repositories.JpaUserRepository;
import com.tvestergaard.start.data.repositories.base.transactions.JpaTransaction;
import com.tvestergaard.start.logic.ResourceConflictException;
import com.tvestergaard.start.logic.ResourceNotFoundException;
import com.tvestergaard.start.logic.SpecializedGson;
import com.tvestergaard.start.logic.UserFacade;
import com.tvestergaard.start.rest.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("users")
public class UserResource
{

    private static Gson                       gson       = SpecializedGson.create();
    private static UserFacade<JpaTransaction> userFacade = new UserFacade<>(
            () -> new JpaTransaction(JpaConnection.create()),
            JpaUserRepository::new
    );

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response createUser(String content) throws ResourceConflictException
    {
        ReceivedCreateUser receivedUser = gson.fromJson(content, ReceivedCreateUser.class);

        User createdUser = userFacade.createUser(receivedUser.name,
                                                 receivedUser.email,
                                                 receivedUser.password);

        return Response.status(CREATED).entity(gson.toJson(UserDTO.complete(createdUser))).build();
    }

    private class ReceivedCreateUser
    {
        public String name;
        public String email;
        public String password;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("{id: [0-9]+}")
    public Response getUserById(@PathParam("id") int id) throws ResourceNotFoundException
    {
        User   user    = userFacade.get(id);
        String jsonDTO = gson.toJson(UserDTO.complete(user));
        return Response.ok(jsonDTO).build();
    }
}
