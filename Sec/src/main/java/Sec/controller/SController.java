package Sec.controller;

import Core.annotation.LoginUser;
import Core.util.Common;
import Core.annotation.Audit;
import Sec.model.vo.GoodsInfoVo;
import Sec.service.SService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@Api(value = "秒杀系统后端", tags = "orders")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class SController {

    @Autowired
    private SService service;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 用户下单
     * @author snow create 2021/04/29 10:51
     * @param userId 用户id
     * @param goodsVo 商品信息列表
     * @return 操作结果
     */
    @ApiOperation(value = "用户下单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "GoodsInfoVo", name = "goodsVo", value = "商品信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("orders")
    public Object adminAuditVerification(@ApiIgnore @LoginUser Long userId, @RequestBody GoodsInfoVo goodsVo){
//        System.out.println("UserID: " + userId + ", Goods: " + goodsVo);
        return Common.decorateResponseCode(service.purchaseGoods(userId, goodsVo));
    }

}

