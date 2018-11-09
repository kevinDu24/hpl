package cn.net.leadu.dao;

import cn.net.leadu.domain.PurchaseApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by pengchao on 2017/5/25.
 */
public interface PurchaseApprovalRepository extends JpaRepository<PurchaseApproval, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM purchase_approval t1 WHERE t1.unique_mark =?1 AND t1.create_user =?2 AND t1.period =?3")
    List<PurchaseApproval> findFullInfo(String uniqueMark, String user, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM purchase_approval t1 WHERE t1.item_key LIKE '%#driveLicenceInfo%' AND t1.unique_mark =?1 AND t1.period =?2")
    List<PurchaseApproval> findDriveInfo(String uniqueMark, String version);
}
