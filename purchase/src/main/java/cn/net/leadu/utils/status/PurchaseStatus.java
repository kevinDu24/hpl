package cn.net.leadu.utils.status;

/**
 * Created by pengchao on 2017/6/29.
 */
public enum PurchaseStatus {


    /**
     * {@code 0 场景1}.
     * 首次注册-> 车型选择页面
     */
    REGISTER("0", "注册"),

    /**
     * {@code 1  场景3}.
     * 已注册，未提交预审批 -> 车型选择页面
     */
    LOGIN("1", "已注册"),

    /**
     * {@code 2 场景2}.
     * 提交预审批,未审核 -> 预审批结果页面
     */
    SUBMIT("2", "已提交预审批"),


    /**
     * {@code 3 场景4}.
     * 预审批通过，未提交订单 -> 新进录单页面
     */
    PASS("3", "预审批通过"),


    /**
     * {@code 4 场景5}.
     * 新进录单提交 -> 流程结束页面
     */
    ORDER_SUBMIT("4", "录单完成并提交");

    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    PurchaseStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
