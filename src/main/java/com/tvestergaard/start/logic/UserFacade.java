package com.tvestergaard.start.logic;

import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.data.repositories.UserRepository;
import com.tvestergaard.start.data.repositories.base.transactions.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Function;
import java.util.function.Supplier;

public class UserFacade<T extends Transaction>
{

    /**
     * The factory that produces transactions used by this facade.
     */
    private final Supplier<T> transactionFactory;

    /**
     * The factory that produces user repositories used by this facade.
     */
    private final Function<T, UserRepository> userRepositoryFactory;

    /**
     * Creates a new {@link UserFacade}.
     *
     * @param transactionFactory    The factory that produces transactions used by this facade.
     * @param userRepositoryFactory The factory that produces user repositories used by this facade.
     */
    public UserFacade(Supplier<T> transactionFactory, Function<T, UserRepository> userRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.userRepositoryFactory = userRepositoryFactory;
    }

    /**
     * Finds the user with the provided integer.
     *
     * @param id The id of the user to return.
     * @return The user with the provided id.
     * @throws ResourceNotFoundException When a user with the provided id does not exist.
     */
    public User get(Integer id) throws ResourceNotFoundException
    {
        try (UserRepository ur = userRepositoryFactory.apply(transactionFactory.get())) {
            User user = ur.get(id);
            if (user == null)
                throw new ResourceNotFoundException(User.class, id);

            return user;
        }
    }

    /**
     * Creates a user from the provided information.
     *
     * @param name     The name of the user to create.
     * @param email    The email of the user to create.
     * @param password The password of the user to create.
     * @return The newly created user entity.
     * @throws ResourceConflictException When a user with the provided {@code email} already exists.
     * @throws ResourceNotFoundException When a city with the provided id does not exist.
     */
    public User createUser(String name, String email, String password) throws ResourceConflictException
    {
        try (T transaction = transactionFactory.get()) {

            transaction.begin();
            UserRepository ur = userRepositoryFactory.apply(transaction);

            if (ur.getByEmail(email) != null)
                throw new ResourceConflictException(User.class, "A user with the provided email address already exists.");

            User user = ur.createUser(name, email, hash(password));
            transaction.commit();
            return user;
        }
    }

    /**
     * Hashes the provided password using the bcrypt algorithm.
     *
     * @param password The password to hash.
     * @return The resulting hash.
     */
    private String hash(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
