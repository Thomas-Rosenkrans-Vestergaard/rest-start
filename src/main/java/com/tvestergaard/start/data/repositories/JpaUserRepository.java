package com.tvestergaard.start.data.repositories;

import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.data.repositories.base.JpaCrudRepository;
import com.tvestergaard.start.data.repositories.base.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 * An implementation of the {@code UserRepository} interface, backed by a JPA data source.
 */
public class JpaUserRepository extends JpaCrudRepository<Integer, User> implements UserRepository
{

    /**
     * Creates a new {@link JpaUserRepository}.
     *
     * @param entityManager The entity manager that operations are performed upon.
     */
    public JpaUserRepository(EntityManager entityManager)
    {
        super(entityManager, Integer.class, "id", User.class);
    }

    /**
     * Creates a new {@link JpaUserRepository}.
     *
     * @param entityManagerFactory The entity manager factory from which the entity manager - that operations are
     *                             performed upon - is created.
     */
    public JpaUserRepository(EntityManagerFactory entityManagerFactory)
    {
        super(entityManagerFactory, Integer.class, "id", User.class);
    }

    /**
     * Creates a new {@link JpaUserRepository}.
     *
     * @param transaction The transaction from which the entity manager - that operations are performed upon - is
     *                    created.
     */
    public JpaUserRepository(JpaTransaction transaction)
    {
        super(transaction, Integer.class, "id", User.class);
    }

    /**
     * Inserts a new user into the repository with the provided information. Note that the operation is not committed.
     *
     * @param name         The name of the new user.
     * @param email        The email of the new user.
     * @param passwordHash The password hash of the new user.
     * @return The resulting new user.
     */
    @Override
    public User createUser(String name, String email, String passwordHash)
    {
        User user = new User(name, email, passwordHash);
        getEntityManager().persist(user);
        return user;
    }

    @Override
    public User getByEmail(String email)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
