package Order;

import Core.util.ResponseCode;
import Order.model.bo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;

    public ResponseCode createOrder(Order order){
        return dao.insertOrder(order);
    }
}
