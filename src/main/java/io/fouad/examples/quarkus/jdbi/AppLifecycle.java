package io.fouad.examples.quarkus.jdbi;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class AppLifecycle {
	
	@Inject DbUtils dbUtils;
	
	void onStart(@Observes StartupEvent ev) {
		dbUtils.jdbi().useExtension(UserDao.class, UserDao::createTable);
	}
	
	void onShutdown(@Observes ShutdownEvent ev) {
		dbUtils.jdbi().useExtension(UserDao.class, UserDao::dropTable);
	}
}