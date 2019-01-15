package com.tvestergaard.start.rest;

import com.google.gson.Gson;
import com.tvestergaard.start.data.repositories.JpaUserRepository;
import com.tvestergaard.start.logic.AuthenticationFacade;
import com.tvestergaard.start.logic.SpecializedGson;
import com.tvestergaard.start.logic.authentication.AuthenticationContext;
import com.tvestergaard.start.logic.authentication.jwt.BasicJwtSecret;
import com.tvestergaard.start.rest.dto.AuthenticationDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("authentication")
public class AuthenticationResource
{

    private static Gson                 gson                 = SpecializedGson.create();
    private static byte[]               secret               = new byte[]{1, 2, 3, 4,};
    private static AuthenticationFacade authenticationFacade = new AuthenticationFacade(
            new BasicJwtSecret(secret),
            () -> new JpaUserRepository(JpaConnection.create())
    );


    @POST
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(String content) throws Exception
    {
        ReceivedAuthenticateUser receivedUser          = gson.fromJson(content, ReceivedAuthenticateUser.class);
        AuthenticationContext    authenticationContext = authenticationFacade.authenticate(receivedUser.email, receivedUser.password);
        String                   token                 = authenticationFacade.generateAuthenticationToken(authenticationContext);
        return Response.ok(gson.toJson(AuthenticationDTO.basic(token, authenticationContext.getUser()))).build();
    }

    private class ReceivedAuthenticateUser
    {
        public String email;
        public String password;
    }
}
