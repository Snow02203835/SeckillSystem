package Order;

import Core.util.ResponseCode;
import Order.mapper.OrderPoMapper;
import Order.model.bo.Order;
import Order.model.po.OrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class OrderDao {
    @Autowired
    private OrderPoMapper orderMapper;

    /**
     * 写入订单到数据库
     * @author snow create 2021/05/18 17:43
     * @param order 订单信息bo
     * @return 操作结果
     */
    public ResponseCode insertOrder(Order order){
        try {
            order.setGmtCreate(LocalDateTime.now());
            OrderPo orderPo = order.createPo();
            int effectRows = orderMapper.insert(orderPo);
            if(effectRows == 1){
                order.setId(orderPo.getId());
                return ResponseCode.OK;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResponseCode.INTERNAL_SERVER_ERR;
    }
}
