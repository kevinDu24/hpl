package cn.net.leadu.dao;

import cn.net.leadu.domain.PurchaseApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2017/6/29.
 */
public interface PurchaseApplyInfoRepository extends JpaRepository<PurchaseApplyInfo,Long> {
    PurchaseApplyInfo findByApprovalUuidAndPeriod(String uniqueMark, String period);
    List<PurchaseApplyInfo> findByPeriodAndStatus(String period, String status);
    PurchaseApplyInfo findByApprovalUuidAndPeriodAndStatus(String uniqueMark, String period, String status);
}
