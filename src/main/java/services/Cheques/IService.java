package Cheques;

import java.sql.SQLException;

public interface IService<T> {

    void add(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;


    T getById(int id) throws  SQLException;
}