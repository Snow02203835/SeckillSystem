package Sec.service;

import Core.util.ResponseCode;
import Sec.dao.SDao;
import Sec.model.bo.Order;
import Sec.model.vo.GoodsInfoVo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SService {

    @Autowired
    private SDao dao;
    @Autowired
    private RocketMQTemplate rocketMQ;

    /**
     * 购买商品
     * @author snow create 2021/05/18 16:11
     * @param userId 用户id
     * @param vo 商品信息
     * @return 操作结果
     */
    public ResponseCode purchaseGoods(Long userId, GoodsInfoVo vo){
//        ResponseCode code = dao.decreaseGoodsStoreFromDataBase(vo.getId(), vo.getAmount());
        ResponseCode code = dao.decreaseGoodsStoreFromRedis(vo.getId(), vo.getAmount());
        if(code == ResponseCode.OK){
            try {
//                dao.insertOrder(new Order(userId, vo)); // insert order directly
                rocketMQ.convertAndSend("orders", new Order(userId, vo)); // insert order through rocketMQ asynchronously
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return code;
    }
}
