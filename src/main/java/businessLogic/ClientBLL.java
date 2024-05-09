package businessLogic;

import dataAccess.ClientDAO;
import javafx.scene.control.TextField;
import model.Client;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class provides business logic for managing clients in the application.
 * @author Ruben
 */

public class ClientBLL {

    ClientDAO clientDAO;

    public ClientBLL() {

    }
    public ClientBLL(@NotNull List<TextField> clientField){
        String name = (clientField.get(0).getText());
        String phone = (clientField.get(1).getText());
        String email = (clientField.get(2).getText());
        Client client = new Client(name, phone, email);

        insertClient(client);
    }
    public List<Client> findAll(){
        clientDAO = new ClientDAO();
        return clientDAO.findAll();
    }

    private void insertClient(Client client){
        clientDAO = new ClientDAO();
        clientDAO.insert(client);
    }

    public void deleteClient(Client client){
        clientDAO = new ClientDAO();
        clientDAO.delete(client.getId());
    }

    public int count(){
        clientDAO = new ClientDAO();
        return clientDAO.count();
    }

    public void updateClient(Client client){
        clientDAO = new ClientDAO();
        clientDAO.update(client, client.getId());
    }

    public Client findByName(String name){
        clientDAO = new ClientDAO();
        return clientDAO.findByName(name);
    }

}
