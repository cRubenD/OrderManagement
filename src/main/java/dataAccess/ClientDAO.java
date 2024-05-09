package dataAccess;

import model.Client;
import java.util.List;

/**
 * This class is a Data Access Object thta provides an interface for CRUd operations
 * on Client objects within a database.
 * It also extends the generic AbstractDAO class to offer specific implementation
 * for the client entity.
 * @author Ruben
 */
public class ClientDAO extends AbstractDAO<Client>{

    @Override
    public void insert(Client object) {
        super.insert(object);
    }
    @Override
    public void update(Client object, int id) {
        super.update(object, id);
    }
    @Override
    public void delete(int id) {
        super.delete(id);
    }
    @Override
    public List<Client> findAll() {
        return super.findAll();
    }
    @Override
    public Client findById(int id) {
        return super.findById(id);
    }
    @Override
    public Client findByName(String name) {
        return super.findByName(name);
    }
    @Override
    public int count() {
        return super.count();
    }
}
