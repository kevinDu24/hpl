package cn.net.leadu.dao;

import cn.net.leadu.domain.PurchaseOrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by pengchao on 2017/5/25.
 */
public interface PurchaseOrderInfoRepository extends JpaRepository<PurchaseOrderInfo, Long> {

    @Query(nativeQuery = true, value = "SELECT\n" +
            "\tpoi.phone_num,\n" +
            "\tpai.status,\n" +
            "\tpai.order_status, poi.company \n" +
            "FROM\n" +
            "\tpurchase_order_info poi\n" +
            "LEFT JOIN purchase_apply_info pai ON poi.phone_num = pai.approval_uuid\n" +
            "AND poi.period = pai.period\n" +
            "WHERE poi.phone_num = ?1\n" +
            "AND poi.period = ?2\n" +
            "ORDER BY poi.create_time DESC\n" +
            "LIMIT 1")
    Object getOrderInfo(String phoneNum, String period);

    PurchaseOrderInfo findByPhoneNumAndPeriod(String phoneNum, String period);
}
