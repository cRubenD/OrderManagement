package dataAccess;

import connection.ConnectionFactory;
import model.Bill;
import model.Order;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.lang.Object;
import java.beans.PropertyDescriptor;

/**
 * A generic Data Access Object class for interacting with the database.
 * This abstract class provides a flexible and reusable framework for basic CRUD operations.
 * @param <T> The type of the model that this DAO manages
 * @author Ruben
 */
public abstract class AbstractDAO<T>{
    /**
     * Logger for capturing errors and warnings during database interactions
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    /**
     * The class object representing the type <T>
     */
    private final Class<T> type;

    /**
     * Constructor that initializes the type parameter for the DAO.
     * It uses reflection to determine the actual class type from the subclass.
     * @throws IllegalArgumentException if the generic type is not parametrized
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType parameterizedType) {
            this.type = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("Invalid parameterized type");
        }
    }

    /**
     * Create a SELECT query to find a record by a specific field.
     * @param field The name of the field to query by
     * @return A SQL SELECT query string
     */
    private String createSelectQuery(String field){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT");
        sb.append(" * ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ").append(field).append(" =?");
        return sb.toString();
    }

    /**
     * Finds a record by its ID.
     * @param id The ID of the record to find
     * @return The record of type T, or null if not found
     */
    public T findById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById "+ e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Finds a record in the database by its name.
     * @param name The name of the record to find
     * @return The record of type T, or null if no record is found
     */
    public T findByName(String name){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("name");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName "+ e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Inserts a new record in the database. The method uses reflection to set the
     * appropriate fields in the SQL INSERT statement. If the model has an
     * auto-incrementing ID, the method updates the object with the generated ID.
     * @param object The object of type T to insert into the database
     */
    public void insert(T object){
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO ");
        if(type.getSimpleName().equals("Order")){
            query.append("`Order`");
        }else{
            query.append(type.getSimpleName());
        }
        query.append("(");

        Field[] fields = type.getDeclaredFields();

        for(int i = 0; i < fields.length; i++){
            query.append(fields[i].getName());
            if(i < fields.length - 1){
                query.append(", ");
            }
        }
        query.append(") VALUES (");
        for(int i = 0; i < fields.length; i++){
            query.append("?");
            if(i < fields.length - 1){
                query.append(", ");
            }
        }
        query.append(")");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

            for(int i = 0; i < fields.length; i++){
                fields[i].setAccessible(true);
                Object value = fields[i].get(object);
                statement.setObject(i + 1, value);
            }

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next() && object instanceof Order) {
                int generatedId = generatedKeys.getInt(1);
                ((Order) object).setId(generatedId);
            }
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    /**
     * Updates an existing record in the database.
     * This method generates a SQL UPDATE query to modify the specified fields of a record with a given ID.
     * @param object The object of type T to update
     * @param id The ID of the record to update
     */
    public void update(T object, int id){
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder();

        query.append("UPDATE ");
        query.append(type.getSimpleName());
        query.append(" SET ");

        Field[] fields = type.getDeclaredFields();

        for(int i = 0; i < fields.length; i++){
            query.append(fields[i].getName());
            query.append(" = ?");
            if(i < fields.length - 1){
                query.append(", ");
            }
        }

        query.append(" WHERE id = ?");

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());

            for(int i = 0; i < fields.length; i++){
                fields[i].setAccessible(true);
                Object value = fields[i].get(object);
                statement.setObject(i + 1, value);
            }

            statement.setInt(fields.length + 1, id);

            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    /**
     * Deletes a record from the database based on its ID.
     * @param id The ID of the record to delete
     */
    public void delete(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder();

        query.append("DELETE FROM ");
        if(type.getSimpleName().equals("Order")){
            query.append("`Order`");
        }else{
            query.append(type.getSimpleName());
        }
        query.append(" WHERE id = ?");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Finds all records of type T in the database.
     * @return A list of all records found
     */
    public List<T> findAll(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ");
        if(type.getSimpleName().equals("Order")){
            query.append("`Order`");
        }else{
            query.append(type.getSimpleName());
        }

        List<T> results = new ArrayList<>();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());
            resultSet = statement.executeQuery();

            results = createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return results;
    }

    /**
     * Counts the total number of records of type T in the database.
     * @return The total count of records
     */
    public int count(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        String query;
        if(type.getSimpleName().equals("Order")){
            query= "SELECT COUNT(*) AS total FROM `Order`";
        }else{
            query = "SELECT COUNT(*) AS total FROM " + type.getSimpleName();
        }

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:count " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return count;
    }

    /**
     * Creates a list of instances of type <T> from a ResultSet.
     * This method utilizes reflection to map database fields to object properties.
     * It handles both record types and conventional class types.
     * @param resultSet The ResultSet from which to create instances.
     * @return A list of instances of type <T>.
     */
    private List<T> createObjects(ResultSet resultSet){
        List<T> list = new ArrayList<>();

        try{
            while (resultSet.next()){

                if(type.isRecord()){
                    int orderId = resultSet.getInt("orderId");
                    String clientName = resultSet.getString("clientName");
                    String productName = resultSet.getString("productName");
                    int quantity = resultSet.getInt("quantity");
                    float totalPrice = resultSet.getFloat("totalPrice");

                    T instance = (T) new Bill(orderId, clientName, productName, quantity, totalPrice);

                    list.add(instance);
                } else {

                    T instance = type.getDeclaredConstructor().newInstance();

                    for (Field field : type.getDeclaredFields()) {
                        String fieldName = field.getName();
                        Object value = resultSet.getObject(fieldName);

                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                        Method method = propertyDescriptor.getWriteMethod();

                        Class<?> parameterType = method.getParameterTypes()[0];

                        if (parameterType.isPrimitive()) {
                            if (parameterType == int.class && value instanceof Integer) {
                                method.invoke(instance, value);
                            } else if (parameterType == float.class && value instanceof Number) {
                                method.invoke(instance, ((Number) value).floatValue());
                            }
                        } else if (parameterType.isAssignableFrom(value.getClass())) {
                            method.invoke(instance, value);
                        }
                    }
                    list.add(instance);
                }
            }
        } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException |
                 IntrospectionException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


}
