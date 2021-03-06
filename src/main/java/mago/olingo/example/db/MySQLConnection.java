package mago.olingo.example.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MySQLConnection implements IConnection {

    public static final String PERSISTENCE_UNIT = "mysql_PU";
    private static MySQLConnection instance = null;

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    private MySQLConnection() {
        this.createConnection();
    }

    public static MySQLConnection getInstance() {
        if (instance == null) {
            instance = new MySQLConnection();
        }
        return instance;
    }

    public void createConnection() {
        if(instance == null){
            this.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            this.entityManager = this.factory.createEntityManager();
        }
    }

    public void closeConnection() {
        this.entityManager.close();
        this.factory.close();
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void finalize() {
        this.closeConnection();
    }

}
