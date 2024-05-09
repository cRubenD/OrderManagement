package businessLogic;

import dataAccess.OrderDAO;
import model.Order;

/**
 * This class provides business logic for managing orders in the application.
 * @author Ruben
 */
public class OrderBLL {

    OrderDAO orderDAO;

    public OrderBLL(){

    }

    public void insertOrder(Order order){
        orderDAO = new OrderDAO();
        orderDAO.insert(order);
    }

    public int count(){
        orderDAO = new OrderDAO();
        return orderDAO.count();
    }
}
