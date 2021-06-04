package Sec.model.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/05/06 09:47
 */
@Data
public class GoodsInfoVo {

    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    @Min(1)
    private Integer amount;
}
