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


public class Util {

    private static SessionFactory sessionFactory;
    private static final String URL_USERDB = "jdbc:mysql://localhost:3306/usersdb";
    private static final String NAME_USER = "root";
    private static final String PASSWORD = "GfdtkCjatby5555555555";
    private static final String DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String SHOW_SQL = "true";
    private static final String CURRENT_SESSION_CONTEXT_CLASS = "thread";
    private static final String HBM2DDL_AUTO = "create-drop";

    private static Connection connection;

    // Подключение через JDBC
    public static Connection getConnection() {
        connection = null;
        try {
            connection = DriverManager.getConnection(URL_USERDB, NAME_USER, PASSWORD);
        } catch (SQLException e) {
            e.getStackTrace();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.getStackTrace();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        try {
            sessionFactory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
