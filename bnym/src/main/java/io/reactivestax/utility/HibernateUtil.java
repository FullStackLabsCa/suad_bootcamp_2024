package io.reactivestax.utility;

import io.reactivestax.repository.entity.RuleSet;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


@Getter
@Slf4j
public class HibernateUtil {
    private static volatile HibernateUtil instance;
    @Getter
    private static final ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();

    private static SessionFactory sessionFactory;
    private static final String DEFAULT_RESOURCE = "hibernate.cfg.xml";
    @Setter
    private static String configResource = DEFAULT_RESOURCE;

    private HibernateUtil() {}

    private static synchronized SessionFactory buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration()
                        .configure(HibernateUtil.configResource)
                        .addAnnotatedClass(RuleSet.class);
                StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (HibernateException e) {
                log.error("Initial Session Factory creation failed: ", e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    //Returns the singleton instance of HibernateUtil.
    public static synchronized HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }


    public Session getConnection() {
        Session session = threadLocalSession.get();
        if (session == null) {
            session = buildSessionFactory().openSession();
            threadLocalSession.set(session);
        }
        return session;
    }

}
