package cn.net.leadu.dto;

import lombok.Data;

/**
 * Created by pengchao on 2017/7/5.
 */
@Data
public class AttachmentDto {

    private String idCardFaceUrl;//身份证反面

    private String idCardBackUrl;//身份证正面

    private String creditLetterUrl;//征信委托授权书

    private String drivingLicenseFaceUrl;//驾驶证正面

    private String drivingLicenseBackUrl;//驾驶证附件

    private String jobCertificateUrl;//工作证明
}
