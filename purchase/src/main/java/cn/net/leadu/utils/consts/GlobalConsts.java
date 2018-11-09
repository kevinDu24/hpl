package cn.net.leadu.utils.consts;

/**
 * Created by LEO on 16/9/1.
 */
public enum GlobalConsts {
    SIGN("12940581fbbf2df3b9739fe34a3344b8"), TIMESTAMP("1429604397531");

    private final String value;

    GlobalConsts(String value) {
        this.value = value;
    }

    public String value(){
        return this.value;
    }

}
