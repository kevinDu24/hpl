package leadu.approval.dao;

import leadu.approval.domain.ApplyDetail;
import leadu.approval.domain.ApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yuanzhenxia on 2017/7/20.
 */
public interface ApplyDetailRepository extends JpaRepository<ApplyDetail,Long> {
}
