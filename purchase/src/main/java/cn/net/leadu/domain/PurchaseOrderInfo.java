package cn.net.leadu.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by pengchao on 2017/6/29.
 */
@Data
@Entity
public class PurchaseOrderInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String phoneNum;//手机号

    private String vehicleId;//车型id

    private String vehicleColor;//车型颜色

    private String financeType;//套餐类型

    private String company;//工作单位

    private String workAdd;//工作地址

    private String liveAdd;//实际居住地

    private String localAdd;//户籍地址

    private String bankUser;//银行户名

    private String bankCard;//银行卡号

    private String bankName;//银行

    private String identifyFrontImg;//身份证正面

    private String identifyBehindImg;//身份证反面

    private String driveFrontImg;//驾驶证正面

    private String driveBehindImg;//驾驶证反面

    private String creditProxy;//征信委托授权书

    private String workCertificate;//工作证明

    private String period; //活动期数

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateTime;



}
