package cn.net.leadu.dao;

import cn.net.leadu.domain.VehicleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2017/5/25.
 */
public interface VehicleInfoRepository extends JpaRepository<VehicleInfo, Long> {
    List<VehicleInfo> findByPeriod(String period);
}
