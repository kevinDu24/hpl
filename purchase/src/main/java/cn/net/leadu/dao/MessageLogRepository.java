package cn.net.leadu.dao;

import cn.net.leadu.domain.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 短信日志 dao 层
 * Created by qiaohao on 2017/5/12.
 */
public interface MessageLogRepository extends JpaRepository<MessageLog,String> {
}
