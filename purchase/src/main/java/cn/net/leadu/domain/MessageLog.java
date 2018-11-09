package cn.net.leadu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 短信日志
 * Created by qiaohao on 2017/5/12.
 */
@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageLog {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String phone;  //发送手机号

    private Date sendTime; //发送时间

    private String content; //发送内容

    private String projectName;//项目名称 例如 vms车辆管理系统

    private String serviceName;//业务名称  例如 用户注册

    private String classFunctionName;//类与方法名称 例如 cn.leadu.control.service.impl.类名.方法名

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

}
