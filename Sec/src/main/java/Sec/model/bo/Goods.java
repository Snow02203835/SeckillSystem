package Sec.model.bo;

import Core.model.VoObject;
import Sec.model.po.GoodsPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author snow create 2021/05/07 20:59
 */
@Data
public class Goods implements VoObject, Serializable {
    private Long id;
    private Integer store;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public Goods(GoodsPo po){
        if (po == null){
            return;
        }
        this.id = po.getId();
        this.store = po.getStore();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public GoodsPo createPo(){
        GoodsPo po = new GoodsPo();
        po.setId(this.id);
        po.setStore(this.store);
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
