package io.fouad.examples.quarkus.jdbi;

import io.agroal.api.AgroalDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DbUtils {
    @Inject AgroalDataSource defaultDataSource;
    Jdbi jdbi;
    
    @PostConstruct
    void init() {
        jdbi = Jdbi.create(defaultDataSource).installPlugin(new SqlObjectPlugin());
    }
    
    public Jdbi jdbi(){return jdbi;}
}