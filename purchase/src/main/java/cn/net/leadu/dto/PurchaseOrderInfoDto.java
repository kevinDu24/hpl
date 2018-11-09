package cn.net.leadu.dto;

import lombok.Data;

/**
 * Created by pengchao on 2017/6/29.
 */
@Data
public class PurchaseOrderInfoDto {
    private String phoneNum;
    private String applyStatus;
    private String orderStatus;
    private String company;

    public PurchaseOrderInfoDto(Object[] objs) {
        this.phoneNum = objs[0] == null ? "" : objs[0].toString();
        this.applyStatus = objs[1] == null ? "" : objs[1].toString();
        this.orderStatus = objs[2] == null ? "" : objs[2].toString();
        this.company = objs[3] == null ? "" : objs[3].toString();
    }
}
