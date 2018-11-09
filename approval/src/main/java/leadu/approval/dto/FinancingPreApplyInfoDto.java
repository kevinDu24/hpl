package leadu.approval.dto;

import lombok.Data;

/**
 * Created by pengchao on 2017/5/24.
 */
@Data
public class FinancingPreApplyInfoDto {

    private String applicantName;//姓名

    private String idCardNum;//身份证号码

    private String applicantPhoneNum;//手机号

    private String applicantConmany;// 工作单位

    private String yearlySalary;// 年薪

    private String homeAddress;// 居住地

    private String contact1Name;//联系人1姓名

    private String contact1Mobile;//联系人1手机

    private String contact1Relationship;//联系人1关系

    private String contact2Name;//联系人2姓名

    private String contact2Mobile;//联系人2手机

    private String contact2Relationship;//联系人2关系

    private String mateName;//配偶姓名

    private String mateMobile;//配偶手机

    private String mateIdty;//配偶证件号码



}
