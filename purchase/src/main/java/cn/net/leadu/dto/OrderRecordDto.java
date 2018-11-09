package cn.net.leadu.dto;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2017/7/5.
 *
 * 向主系统提供录单信息
 */
@Data
public class OrderRecordDto {
    private String applyId;  // 申请编号
    private String vehicleId;// 车型id
    private String financeType;  // 产品方案
    private String vehicleColor;  // 车辆颜色
    private String company;// 工作单位
    private String workAdd;  // 工作地点
    private String liveAdd;// 实际居住地址
    private String localAdd;// 户籍地址
    private String bankUser;  // 银行卡户名
    private String bankCard;// 银行卡卡号
    private String bankName;// 开户行
    private String identifyFrontImg;  // 身份证正面
    private String identifyBehindImg;// 身份证反面
    private String driveFrontImg;  // 驾驶证正面附件
    private String driveBehindImg;// 驾驶证反面附件
    private String workCertificate;// 工作证明附加
    private String creditProxy;// 征信委托授权书附件
    private String driveLicenceName;  // 驾驶证项目
    private String driveLicenceEffectiveTerm;// 驾驶证有效日期
    private String quasiDriveType;  // 准驾车型
    private String driveLicenceNum;// 驾驶证号码
    private String driveLicenceFn;// 驾驶证档案号

}
