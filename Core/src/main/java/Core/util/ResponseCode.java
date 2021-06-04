package Core.util;

/**
 * 返回的错误码
 * @author Ming Qiu
 */
public enum ResponseCode {
    OK(0,"成功"),
    /***************************************************
     *    系统级错误
     **************************************************/
    INTERNAL_SERVER_ERR(500,"服务器内部错误"),
    //所有需要登录才能访问的API都可能会返回以下错误
    AUTH_INVALID_JWT(501,"JWT不合法"),
    AUTH_JWT_EXPIRED(502,"JWT过期"),

    //以下错误码提示可以自行修改
    //--------------------------------------------
    FIELD_NOT_VALID(503,"字段不合法"),
    //所有路径带id的API都可能返回此错误
    RESOURCE_ID_NOT_EXIST(504,"操作的资源id不存在"),
    RESOURCE_ID_OUT_SCOPE(505,"操作的资源id不是自己的对象"),
    FILE_NO_WRITE_PERMISSION(506,"目录文件夹没有写入的权限"),
    RESOURCE_FALSIFY(507, "信息签名不正确"),

    /***************************************************
     *    权限模块错误码
     **************************************************/
    BILL_TYPE_ERROR(600, "支付方式不存在"),
    BILL_ID_DO_NOT_EXIST(601, "支付单号不存在"),
    BILL_UNPAID(602, "待支付"),
    BILL_PAID(603, "已支付"),
    BILL_CANCEL(604, "已取消"),
    BILL_WITHDRAW(605, "已退款"),
    BILL_OUT_SCOPE(606, "订单不属于该需求"),

    /***************************************************
     *    权限模块错误码
     **************************************************/
    AUTH_NEED_LOGIN(704, "需要先登录"),


    /***************************************************
     *    订单模块错误码
     **************************************************/
    GOODS_NOT_EXIST(800,"商品不存在"),
    GOODS_STORE_NOT_ENOUGH(801,"商品库存不足"),
    ORDER_STATUS_FORBID(810, "订单状态禁止"),
    FEEDBACK_STATUS_FORBID(820, "反馈状态禁止"),
    VERIFICATION_STATUS_FORBID(830, "学生认证状态禁止"),


    /***************************************************
     *    图片模块错误码
     **************************************************/
    IMG_EXIST(900,"图片已存在"),
    IMG_NOT_EXIST(901, "图片不存在");

    private int code;
    private String message;
    ResponseCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(){
        return message;
    }

}
