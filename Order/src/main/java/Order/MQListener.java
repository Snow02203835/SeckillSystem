package Order;

import Core.util.ResponseCode;
import Order.model.bo.Order;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author snow create 2021/06/01 13:12
 */
@RocketMQMessageListener(
        topic = "orders",
        consumerGroup = "producer_group"
)
@Component
public class MQListener implements RocketMQListener<Order> {
    @Autowired
    private OrderService service;
    @Override
    public void onMessage(Order order) {
        ResponseCode code = service.createOrder(order);
        System.out.println("Errno: " + code.getCode() + ", Errmsg: " + code.getMessage());
    }
}
