package org.jbei.ice.storage.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jbei.ice.lib.common.logging.Logger;

/**
 * Helper class to Initialize Hibernate, and obtain new sessions.
 *
 * @author Zinovii Dmytriv, Timothy Ham, Hector Plahar
 */
public class HibernateUtil {

    // thread safe global object that is instantiated once
    private static SessionFactory sessionFactory;

    // singleton
    private HibernateUtil() {
    }

    private static String BASE_FILE = "hibernate.cfg.xml";
    private static String MOCK_FILE = "mock.cfg.xml";
    private static String AWS_FILE = "aws.cfg.xml";
    private static String DEFAULT_FILE = "default.cfg.xml";

    /**
     * Open a new {@link Session} from the sessionFactory.
     * This needs to be closed when done with
     *
     * @return New Hibernate {@link Session}.
     */
    public static Session newSession() {
        return getSessionFactory().openSession();
    }

    /**
     * @return session bound to context.
     */
    protected static Session currentSession() {
        return getSessionFactory().getCurrentSession();
    }

    public static void beginTransaction() {
        getSessionFactory().getCurrentSession().beginTransaction();
    }

    public static void commitTransaction() {
        getSessionFactory().getCurrentSession().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        getSessionFactory().getCurrentSession().getTransaction().rollback();
    }

    /**
     * Initialize a in-memory mock database for testing.
     */
    public static void initializeMock() {
        initialize(Type.MOCK);
    }

    private static synchronized void initialize(Type type) {
        if (sessionFactory == null) {
            Logger.info("Initializing session factory for type " + type.name());
            Configuration configuration = new Configuration().configure(BASE_FILE);
            if (type == Type.MOCK) {
                configuration.configure(MOCK_FILE);
            } else {
                String environment = System.getProperty("environment");

                if (environment != null && environment.equals("aws")) {
                    configuration.configure(AWS_FILE);
                } else {
                    configuration.configure(DEFAULT_FILE);
                }
            }

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();

            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Entry.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Plasmid.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Strain.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Part.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.ArabidopsisSeed.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Link.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.SelectionMarker.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Sequence.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Feature.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.SequenceFeature.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.SequenceFeatureAttribute.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Comment.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Account.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Attachment.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Sample.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.AccountPreferences.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Group.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.TraceSequence.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.TraceSequenceAlignment.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Configuration.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Storage.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Folder.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Parameter.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.AnnotationLocation.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.BulkUpload.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Permission.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Message.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Preference.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.RemotePartner.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Request.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Audit.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Experiment.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.ShotgunSequence.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.Configuration.class);
            configuration.addAnnotatedClass(org.jbei.ice.storage.model.ApiKey.class);

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
    }

    /**
     * Retrieve the {@link SessionFactory}.
     *
     * @return Hibernate sessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initialize(Type.NORMAL);
        }
        return sessionFactory;
    }

    public static void close() {
        currentSession().disconnect();
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            Logger.info("Closing session factory");
            sessionFactory.close();
        }
    }

    /**
     * initialization types
     */
    private enum Type {
        NORMAL, MOCK
    }
}
