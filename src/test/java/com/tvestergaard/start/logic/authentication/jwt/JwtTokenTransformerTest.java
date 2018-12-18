package com.tvestergaard.start.logic.authentication.jwt;

import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.logic.authentication.EagerAuthenticationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTokenTransformerTest
{

    @Test
    public void unpackSuccess() throws Exception
    {
        JwtSecret           secret           = new BasicJwtSecret(256 / 8);
        JwtTokenTransformer tokenTransformer = new JwtTokenTransformer(secret);

        User user = new User();
        user.setId(1);
        String token = tokenTransformer.generate(EagerAuthenticationContext.user(user));
        assertEquals(1, (int) tokenTransformer.unpack(token).getUser());
    }

    @Test
    public void unpackFailureSecret()
    {
        assertThrows(JwtUnpackingException.class, () -> {
            JwtSecret           secretA        = new BasicJwtSecret(new byte[]{1});
            JwtSecret           secretB        = new BasicJwtSecret(new byte[]{2});
            JwtTokenTransformer tokenGenerator = new JwtTokenTransformer(secretA);
            JwtTokenTransformer tokenVerifier  = new JwtTokenTransformer(secretB);

            User user = new User();
            tokenVerifier.unpack(tokenGenerator.generate(EagerAuthenticationContext.user(user)));
        });
    }

    @Test
    public void unpackFailureToken()
    {
        assertThrows(JwtUnpackingException.class, () -> {
            JwtSecret           secretA        = new BasicJwtSecret(new byte[]{1});
            JwtSecret           secretB        = new BasicJwtSecret(new byte[]{2});
            JwtTokenTransformer tokenGenerator = new JwtTokenTransformer(secretA);
            JwtTokenTransformer tokenVerifier  = new JwtTokenTransformer(secretB);

            User user = new User();

            // Illegal header format
            tokenVerifier.unpack("a" + tokenGenerator.generate(EagerAuthenticationContext.user(user)));
        });
    }
}
