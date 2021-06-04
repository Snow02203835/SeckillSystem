package Sec.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/05/06 09:50
 */
@Data
public class GoodsListVo {
    @Valid
    @ApiModelProperty(value = "商品列表")
    @NotNull(message="商品列表不能为空")
    private List<GoodsInfoVo> goods;
}
