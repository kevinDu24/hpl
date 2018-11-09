package cn.net.leadu.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by LEO on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleResult {

    private List<CarName> cars;
    private List<CarInfo> carInfos;
    private String vehicleId;
    private String color;
    private String product;


    public VehicleResult(List<CarName> cars, List<CarInfo> carInfos) {
        this.cars = cars;
        this.carInfos = carInfos;
    }
}
