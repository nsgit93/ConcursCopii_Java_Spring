package Persistence;

import java.util.ArrayList;

public interface IRepository<ID, T> {
    int size();
    void save(T entity) throws RepositoryException;
    void delete(ID id);
    void update(ID id, T entity);
    T findOne(ID id);
    ArrayList<T> findAll();
}
