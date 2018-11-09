package cn.net.leadu.dto;

import cn.net.leadu.utils.commons.ItemColumn;
import cn.net.leadu.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/24.
 */
@Data
public class FinancingPreApplyInfoDto {

    @ItemColumn(keyPath = "#idCardInfo#name", itemDateType = ItemDataTypeEnum.StringType)
    private String idCardName;//姓名

    @ItemColumn(keyPath = "#idCardInfo#idCardNum", itemDateType = ItemDataTypeEnum.StringType)
    private String idCardNum;//身份证号码

    @ItemColumn(keyPath = "#phoneNum", itemDateType = ItemDataTypeEnum.StringType)
    private String mobile;//手机号

    @ItemColumn(keyPath = "#contact1Name", itemDateType = ItemDataTypeEnum.StringType)
    private String contact1Name;//联系人1姓名

    @ItemColumn(keyPath = "#contact1Mobile", itemDateType = ItemDataTypeEnum.StringType)
    private String contact1Mobile;//联系人1手机

    @ItemColumn(keyPath = "#contact1Relationship", itemDateType = ItemDataTypeEnum.StringType)
    private String contact1Relationship;//联系人1关系

    @ItemColumn(keyPath = "#contact2Name", itemDateType = ItemDataTypeEnum.StringType)
    private String contact2Name;//联系人2姓名

    @ItemColumn(keyPath = "#contact2Mobile", itemDateType = ItemDataTypeEnum.StringType)
    private String contact2Mobile;//联系人2手机

    @ItemColumn(keyPath = "#contact2Relationship", itemDateType = ItemDataTypeEnum.StringType)
    private String contact2Relationship;//联系人2关系

    @ItemColumn(keyPath = "#mateName", itemDateType = ItemDataTypeEnum.StringType)
    private String mateName;//配偶姓名

    @ItemColumn(keyPath = "#mateMobile", itemDateType = ItemDataTypeEnum.StringType)
    private String mateMobile;//配偶手机

    @ItemColumn(keyPath = "#mateIdty", itemDateType = ItemDataTypeEnum.StringType)
    private String mateIdty;//配偶证件号码

    @ItemColumn(keyPath = "#mateCompany", itemDateType = ItemDataTypeEnum.StringType)
    private String mateCompany;//配偶工作单位

    @ItemColumn(keyPath = "#mateAddress", itemDateType = ItemDataTypeEnum.StringType)
    private String mateAddress;//配偶地址

    @ItemColumn(keyPath = "#driveLicenceInfo#name", itemDateType = ItemDataTypeEnum.StringType)
    private String driveLicenceName;//驾驶证姓名

    @ItemColumn(keyPath = "#driveLicenceInfo#quasiDriveType", itemDateType = ItemDataTypeEnum.StringType)
    private String quasiDriveType;//准驾车型

    @ItemColumn(keyPath = "#driveLicenceInfo#licenceNum", itemDateType = ItemDataTypeEnum.StringType)
    private String driveLicenceNum;//证件编号

    @ItemColumn(keyPath = "#driveLicenceInfo#effectiveTerm", itemDateType = ItemDataTypeEnum.StringType)
    private String driveLicenceEffectiveTerm;//有效期限

    @ItemColumn(keyPath = "#driveLicenceInfo#fn", itemDateType = ItemDataTypeEnum.StringType)
    private String driveLicenceFn;//驾驶证档案号


}
