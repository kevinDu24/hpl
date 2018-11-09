package cn.net.leadu.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by pengchao on 2017/6/28.
 */
@Data
@Entity
public class VehicleInfo {

    @Id
    @GeneratedValue
    private Long id;
    private String name;//车辆名称
    private String nameUrl;//车辆icon
    private String structure;//结构
    private String size;//尺寸（长/宽/高）
    private String wheelbase;//轴距
    private String engine; // 发动机型号
    private String gearBox;//变速箱
    private String driveMode;//驱动方式
    private String fuelType;//燃油方式
    private String fuelConsumption;//油耗
    private String externalColor;// 外部颜色
    private String innerColor;//内部颜色
    private String  merits;//亮点
    private String  meritsUrl;//亮点icon
    private String period;//所属时期
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;
}
