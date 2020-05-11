package orm;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EntityManager<E> implements DbContext<E>{

    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field primary = this.getId(entity.getClass());
        primary.setAccessible(true);
        Object value = primary.get(entity);

        if (value == null || (int) value <= 0){
            return this.doInsert(entity,primary);
        }else {
           this.doUpdate(entity,primary);
        }
        return false;
    }

    private boolean doInsert(E entity, Field primary) throws IllegalAccessException, SQLException {
        String query = "INSERT INTO " + this.getTableName(entity.getClass()) + " ";
        String columns = "(";
        String values = "(";

        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field f:fields) {
            f.setAccessible(true);
            if(!f.isAnnotationPresent(Id.class)) {
                columns += "`" + this.getColumnName(f) + "`,";

                Object value = f.get(entity);

                if (value instanceof Date){
                    values += "'"
                            + new SimpleDateFormat("yyyy-MM-dd")
                            .format(value) + "',";
                }else {
                    values += "'" + value + "',";
                }
            }
        }
        columns = columns.substring(0,columns.length() - 1) + ")";
        values = values.substring(0,values.length() - 1) + ")";
        query += columns + "VALUES " + values;
        return connection.prepareStatement(query).execute();
    }

    private boolean doUpdate(E entity, Field primary) throws IllegalAccessException, SQLException {
        String query = "UPDATE "+ this.getTableName(entity.getClass()) + " SET ";
        String columnAndValue = "";
        String where = "WHERE ";

        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field f:fields) {
            f.setAccessible(true);
            Object value = f.get(entity);
            if(f.isAnnotationPresent(Id.class)) {

              where += this.getColumnName(f) + " = " + value;

            }else {
               columnAndValue += "`" + this.getColumnName(f) + "` = ";


                if (value instanceof Date){
                    columnAndValue += "'"
                            + new SimpleDateFormat("yyyy-MM-dd")
                            .format(value) + "',";
                }else {
                    columnAndValue += "'" + value + "',";
                }
            }
        }
        columnAndValue = columnAndValue.substring(0,columnAndValue.length() - 1);
        query += columnAndValue + where;

        return connection.prepareStatement(query).execute();
    }

    public Iterable<E> find(Class<E> table) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return this.find(table,null);
    }

    public Iterable<E> find(Class<E> table, String where) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + this.getTableName(table) +
                " WHERE 1 " + (where != null ? "AND " + where : "");
        ResultSet resultSet = statement.executeQuery(query);
        List<E> entities = new ArrayList<>();
        while (resultSet.next()){
            E entity = table.getDeclaredConstructor().newInstance();
            this.fillEntity(table,resultSet,entity);
            entities.add(entity);
        }


        return entities;
    }

    public E findFirst(Class<E> table) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return this.findFirst(table,null);
    }

    public E findFirst(Class<E> table, String where) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + this.getTableName(table) +
                " WHERE 1 " + (where != null ? "AND " + where : "") + " LIMIT 1";
        ResultSet resultSet = statement.executeQuery(query);
        E entity = table.getDeclaredConstructor().newInstance();
        resultSet.next();
        this.fillEntity(table,resultSet,entity);
        return entity;
    }

    private Field getId(Class entity){
        return Arrays.stream(entity.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity does not have primary key"));
    }
    private String getTableName(Class entity){
        String tableName = "";
        tableName = ((Entity)entity.getAnnotation(Entity.class)).name();

        if (tableName.isEmpty()){
            tableName = entity.getSimpleName();
        }
        return tableName;
    }

    private String getColumnName(Field field){
        String columnName = "";
        columnName = field.getAnnotation(Column.class).name();

        if (columnName.isEmpty()){
            columnName = field.getName();
        }
        return columnName;
    }
    private void fillEntity(Class<E> table,ResultSet resultSet, E entity) throws SQLException, IllegalAccessException {
        Field[] fields = table.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            this.fillField(field,entity,resultSet,this.getColumnName(field));
        }
    }
    private void fillField(Field field, Object instance, ResultSet resultSet,String fieldName) throws SQLException, IllegalAccessException {
        field.setAccessible(true);
        if (field.getType() == Integer.class || field.getType() == int.class){
            field.set(instance,resultSet.getInt(fieldName));
        }else if(field.getType() == Date.class){
            field.set(instance,resultSet.getDate(fieldName));
        }else if(field.getType() == String.class){
            field.set(instance,resultSet.getString(fieldName));
        }
    }
}
