package jm.task.core.jdbc.util;

import java.util.Properties;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Util {

    private static final String URL_USERDB = "jdbc:mysql://localhost:3306/usersdb";
    private static final String NAME_USER = "root";
    private static final String PASSWORD = "GfdtkCjatby5555555555";
    private static final String DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // for old java (< 8 version)
    private static final String SHOW_SQL = "true";
    private static final String CURRENT_SESSION_CONTEXT_CLASS = "thread";
    private static final String HBM2DDL_AUTO = "create-drop";
    private static final Logger UTIL_LOGGER = LogManager.getLogger(Util.class);

    private static Connection connection;
    private static SessionFactory sessionFactory;


    public static Logger getUtilLogger() {   //Just in case
        return UTIL_LOGGER;
    }

    // Подключение через JDBC
    public static Connection getConnection() {
        connection = null;
        try {
            Class.forName(DRIVER); // for old java (< 8 version)
            connection = DriverManager.getConnection(URL_USERDB, NAME_USER, PASSWORD);
        } catch (SQLException sqlException) {
            UTIL_LOGGER.log(Level.ERROR,"Problem with connection", sqlException);
        } catch (ClassNotFoundException classNotFoundException) {
            UTIL_LOGGER.log(Level.ERROR,"Driver not found", classNotFoundException);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                UTIL_LOGGER.log(Level.ERROR,"Problem closing database connection", sqlException);
            }
        }
    }

    // Подключение через Hibernate
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, DRIVER);
                settings.put(Environment.URL, URL_USERDB);
                settings.put(Environment.USER, NAME_USER);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, DIALECT);

                settings.put(Environment.SHOW_SQL, SHOW_SQL);

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, CURRENT_SESSION_CONTEXT_CLASS);

                settings.put(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Throwable ex) {
                UTIL_LOGGER.log(Level.ERROR,"Initial SessionFactory creation failed", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (Throwable ex) {
                UTIL_LOGGER.log(Level.ERROR,"Error closing SessionFactory if connection to database is lost", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }
}
