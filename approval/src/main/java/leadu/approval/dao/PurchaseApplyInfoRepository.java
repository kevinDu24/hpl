package leadu.approval.dao;

import leadu.approval.domain.ApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2017/6/29.
 */
public interface PurchaseApplyInfoRepository extends JpaRepository<ApplyInfo,Long> {
    ApplyInfo findByApprovalUuidAndVersion(String uniqueMark, String period);
    List<ApplyInfo> findByVersionAndStatus(String period, String status);
}
