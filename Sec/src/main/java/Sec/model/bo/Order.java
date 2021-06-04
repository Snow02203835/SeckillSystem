package Sec.model.bo;

import Sec.model.vo.GoodsInfoVo;
import Core.model.VoObject;
import Sec.model.po.OrderPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author snow create 2021/05/07 21:06
 */
@Data
public class Order implements VoObject, Serializable {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Integer amount;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Order(OrderPo po){
        this.id = po.getId();
        this.amount = po.getAmount();
        this.userId = po.getUserId();
        this.goodsId = po.getGoodsId();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public Order(Long userId, GoodsInfoVo vo){
        this.userId = userId;
        this.goodsId = vo.getId();
        this.amount = vo.getAmount();
    }

    public OrderPo createPo(){
        OrderPo po = new OrderPo();
        po.setId(this.id);
        po.setAmount(this.amount);
        po.setUserId(this.userId);
        po.setGoodsId(this.goodsId);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
