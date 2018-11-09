package cn.net.leadu.dto.order;

import cn.net.leadu.domain.VehicleInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by LEO on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarInfo {
    private Long vehicleId;//车型id
    private List<String> colors;//颜色
    private String structure;//结构
    private String size;//尺寸（长/宽/高）
    private String engine; // 发动机型号
    private String gearBox;//变速箱
    private String driveMode;//驱动方式
    private String fuelType;//燃油方式
    private String fuelConsumption;//油耗
    private List<MeritInfo> meritInfos;//特点

    public CarInfo(VehicleInfo vehicleInfo, List<MeritInfo> meritInfos, List<String> colors){
        this.vehicleId = vehicleInfo.getId();
        this.colors = colors;
        this.meritInfos = meritInfos;
        this.structure = vehicleInfo.getStructure();
        this.size = vehicleInfo.getSize();
        this.engine = vehicleInfo.getEngine();
        this.gearBox = vehicleInfo.getGearBox();
        this.driveMode = vehicleInfo.getDriveMode();
        this.fuelType = vehicleInfo.getFuelType();
        this.fuelConsumption = vehicleInfo.getFuelConsumption();
        this.meritInfos = meritInfos;
    };
}
