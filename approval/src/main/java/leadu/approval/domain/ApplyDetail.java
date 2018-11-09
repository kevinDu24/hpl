package leadu.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yuanzhenxia on 2017/7/20.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyDetail {
    @Id
    private String approvalUuid;// 唯一标识

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

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;
}
