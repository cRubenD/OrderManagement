package dataAccess;

import model.Bill;

import java.util.List;

/**
 * This class is a Data Access Object thta provides an interface for CRUd operations
 * on Bill objects within a database.
 * It also extends the generic AbstractDAO class to offer specific implementation
 * for the bill entity.
 * @author Ruben
 */
public class BillDAO extends AbstractDAO<Bill>{
    @Override
    public void insert(Bill object) {
        super.insert(object);
    }

    @Override
    public void update(Bill object, int id) {
        super.update(object, id);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public Bill findByName(String name) {
        return super.findByName(name);
    }

    @Override
    public Bill findById(int id) {
        return super.findById(id);
    }

    @Override
    public List<Bill> findAll() {
        return super.findAll();
    }

    @Override
    public int count() {
        return super.count();
    }
}
