package Order.model.bo;

import Core.model.VoObject;
import Order.model.po.OrderPo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author snow create 2021/05/07 21:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
