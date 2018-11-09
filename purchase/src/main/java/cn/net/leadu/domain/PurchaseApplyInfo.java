package cn.net.leadu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2017/6/29.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseApplyInfo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String approvalUuid; //申请人的唯一标识

    private String applyNum; //申请单号(from主系统)

    private String status; //预审批状态

    private String reason; //退回原因

    private String isAutoApproval; // 0: 自动审批， 1:人工审批

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

    private String period; //活动期数

    private String orderStatus;// 订单状态: 0: 未提交， 1: 已提交

}
