package com.earandap.vehiculos.ui.components;

/**
 *
 * @author felix.batista
 */
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    
    private SessionFactory sessionFactory;

    public  HibernateUtil(String dbUrl, String dbUsername, String dbPassword, String dbDriver) {
        try {
            Configuration configuration = new Configuration()
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .setProperty("hibernate.connection.driver_class", dbDriver)
                    .setProperty("hibernate.connection.url", dbUrl)
                    .setProperty("hibernate.connection.username", dbUsername)
                    .setProperty("hibernate.connection.password", dbPassword);
                    //.setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/test")
                    //.setProperty("hibernate.order_updates", "true");
            StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(ssrb.build());
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getSession() throws HibernateException {
        Session session = getSessionFactory().openSession();
        return session;
    }
}
