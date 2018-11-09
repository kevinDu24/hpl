package cn.net.leadu.service;


import cn.net.leadu.dto.message.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pengchao on 2017/2/18.
 */
@Service("SmsMessageService")
public interface MessageService {
    ResponseEntity<Message> sendSingleMt(String phoneNum, String projectName, String serviceName, String classFunctionName);
}

