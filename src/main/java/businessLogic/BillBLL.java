package businessLogic;

import dataAccess.BillDAO;
import model.Bill;

import java.util.List;

/**
 * This class provides business logic for managing bills in the application.
 * @author Ruben
 */
public class BillBLL {

    BillDAO billDAO;
    public BillBLL(){

    }
    public void insertBill(Bill bill){
        billDAO = new BillDAO();
        billDAO.insert(bill);
    }

    public List<Bill> findAll(){
        billDAO = new BillDAO();
        return billDAO.findAll();
    }
}
