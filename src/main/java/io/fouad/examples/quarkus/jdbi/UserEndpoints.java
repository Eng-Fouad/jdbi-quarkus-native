package io.fouad.examples.quarkus.jdbi;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/users")
public class UserEndpoints {
    
    private static final AtomicInteger USERS_COUNT = new AtomicInteger();
    private static final String[] NAMES = {
        "Liam",         "Olivia",
        "Noah",         "Emma",
        "Oliver",       "Charlotte",
        "Elijah",       "Amelia",
        "James",        "Ava",
        "William",      "Sophia",
        "Benjamin",     "Isabella",
        "Lucas",        "Mia",
        "Henry",        "Evelyn",
        "Theodore",     "Harper"
    };
    
    @Inject DbUtils dbUtils;
    
    @GET
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewUser() {
        String name = NAMES[new Random().nextInt(NAMES.length)];
        var user = new User(USERS_COUNT.incrementAndGet(), name);
        dbUtils.jdbi().useExtension(UserDao.class, userDao -> userDao.insertBean(user));
        return user.toString();
    }
    
    @GET
    @Path("/list")
    @Produces(MediaType.TEXT_PLAIN)
    public String listUsers() {
        List<User> users = dbUtils.jdbi().withExtension(UserDao.class, UserDao::listUsers);
        return users.toString();
    }
}