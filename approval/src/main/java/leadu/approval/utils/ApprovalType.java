package leadu.approval.utils;

/**
 * 预审批状态类型
 * Created by zcHu on 2017/5/11.
 */
public enum ApprovalType {


    /**
     * {@code 0 新建申请}.
     */
    NEW("0", "新建申请"),

    /**
     * {@code 100 申请中}.
     */
    SUBMIT("100", "申请中"),


    /**
     * {@code 300 退回待修改，有法院非经济案件}.
     */
    BACK("300", "退回待修改"),

    /**
     * {@code 1000 通过}.
     */
    PASS("1000", "通过"),

    /**
     * {@code 1100 拒绝}.
     */
    REFUSE_NOREASON("1100", "拒绝");

    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    ApprovalType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
