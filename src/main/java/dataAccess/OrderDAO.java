package dataAccess;

import model.Order;

import java.util.List;

/**
 * This class is a Data Access Object thta provides an interface for CRUd operations
 * on Order objects within a database.
 * It also extends the generic AbstractDAO class to offer specific implementation
 * for the order entity.
 * @author Ruben
 */
public class OrderDAO extends AbstractDAO<Order>{

    @Override
    public void insert(Order object) {
        super.insert(object);
    }
    @Override
    public void update(Order object, int id) {
        super.update(object, id);
    }
    @Override
    public void delete(int id) {
        super.delete(id);
    }
    @Override
    public Order findByName(String name) {
        return super.findByName(name);
    }
    @Override
    public List<Order> findAll() {
        return super.findAll();
    }
    @Override
    public Order findById(int id) {
        return super.findById(id);
    }

    @Override
    public int count() {
        return super.count();
    }
}
