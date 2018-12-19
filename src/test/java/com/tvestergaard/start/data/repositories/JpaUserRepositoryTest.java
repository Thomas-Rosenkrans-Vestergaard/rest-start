package com.tvestergaard.start.data.repositories;

import com.tvestergaard.start.JpaTestConnection;
import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.data.repositories.base.JpaCrudRepositoryTester;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JpaUserRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaCrudRepositoryTester<User, Integer, JpaUserRepository> tester =
                new JpaCrudRepositoryTester<>(
                        () -> new JpaUserRepository(JpaTestConnection.create()),
                        (repository) -> createUserMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, User> createUserMap(JpaUserRepository repository)
    {
        TreeMap<Integer, User> map = new TreeMap<>();
        for (int i = 1; i <= 5; i++) {
            User user = repository.createUser("user" + i, "user" + i + "@email.com", "password" + i);
            map.put(user.getId(), user);
        }

        return map;
    }

    @Test
    void getByEmail()
    {
        try (JpaUserRepository tur = new JpaUserRepository(JpaTestConnection.create())) {
            tur.begin();
            assertNull(tur.getByEmail("does_not_exist"));

            assertNull(tur.getByEmail("some@email.com"));
            User user = tur.createUser("email-user", "some@email.com", "pass");
            assertEquals(user, tur.getByEmail("some@email.com"));
        }
    }
}
