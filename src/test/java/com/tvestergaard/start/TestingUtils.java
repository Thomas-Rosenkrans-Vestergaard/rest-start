package com.tvestergaard.start;

import com.tvestergaard.start.data.entities.User;
import com.tvestergaard.start.data.repositories.UserRepository;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class TestingUtils
{

    private static Random      random     = new Random();
    private static Set<String> userEmails = new HashSet<>();

    public static User randomUser(UserRepository repository)
    {
        return repository.createUser(
                randomString(),
                randomUnique(TestingUtils::randomString, userEmails),
                randomString()
        );
    }

    private static <T extends Enum<T>> T randomEnum(Class<T> c)
    {
        int x = random.nextInt(c.getEnumConstants().length);
        return c.getEnumConstants()[x];
    }

    private static <T> T randomUnique(Supplier<T> f, Set<T> set)
    {
        while (true) {
            T attempt = f.get();
            if (!set.contains(attempt)) {
                set.add(attempt);
                return attempt;
            }
        }
    }

    private static String randomString()
    {
        int           leftLimit          = 97; // letter 'a'
        int           rightLimit         = 122; // letter 'z'
        int           targetStringLength = 10;
        StringBuilder buffer             = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }
}
