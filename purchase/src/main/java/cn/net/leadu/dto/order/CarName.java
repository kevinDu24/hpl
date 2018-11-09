package cn.net.leadu.dto.order;

import cn.net.leadu.domain.VehicleInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarName {
    private String key;
    private String value;
    private String url;

    public CarName(Long i, VehicleInfo vehicleInfo) {
        this.key = String.valueOf(i);
        this.value = vehicleInfo.getName();
        this.url = vehicleInfo.getNameUrl();
    }
}
