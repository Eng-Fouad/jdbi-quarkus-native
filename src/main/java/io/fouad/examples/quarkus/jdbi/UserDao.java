package io.fouad.examples.quarkus.jdbi;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface UserDao {
	@SqlUpdate("CREATE TABLE users (id INTEGER PRIMARY KEY, name VARCHAR)")
	void createTable();
	
	@SqlUpdate("INSERT INTO users(id, name) VALUES (:id, :name)")
	void insertBean(@BindBean User user);
	
	@SqlQuery("SELECT * FROM users ORDER BY id DESC")
	@RegisterBeanMapper(User.class)
	List<User> listUsers();
	
	@SqlUpdate("DROP TABLE users")
	void dropTable();
}