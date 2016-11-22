package mago.olingo.example.db;

import java.util.List;

public abstract class AbstractRepository <T> {

    protected IConnection connection;
    protected Class clazz;

    public AbstractRepository(IConnection connection, Class clazz){
        this.connection = connection;
        this.clazz = clazz;
    }

    public T save(T object){
        this.connection.getEntityManager().getTransaction().begin();
        this.connection.getEntityManager().persist(object);
        this.connection.getEntityManager().getTransaction().commit();
        return object;
    }

    public T get(Long id){
        return (T) this.connection.getEntityManager().find(this.clazz, id);
    }

    public abstract List<T> getAll();

    public void delete(T object){
        this.connection.getEntityManager().getTransaction().begin();
        this.connection.getEntityManager().remove(object);
        this.connection.getEntityManager().getTransaction().commit();
    }


}
