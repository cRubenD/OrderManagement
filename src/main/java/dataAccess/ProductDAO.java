package dataAccess;

import model.Product;

import java.util.List;

/**
 * This class is a Data Access Object thta provides an interface for CRUd operations
 * on Product objects within a database.
 * It also extends the generic AbstractDAO class to offer specific implementation
 * for the product entity.
 * @author Ruben
 */
public class ProductDAO extends AbstractDAO<Product> {
    @Override
    public void insert(Product object) {
        super.insert(object);
    }
    @Override
    public void update(Product object, int id) {
        super.update(object, id);
    }
    @Override
    public void delete(int id) {
        super.delete(id);
    }
    @Override
    public Product findById(int id) {
        return super.findById(id);
    }
    @Override
    public List<Product> findAll() {
        return super.findAll();
    }
    @Override
    public Product findByName(String name) {
        return super.findByName(name);
    }
    @Override
    public int count() {
        return super.count();
    }
}
