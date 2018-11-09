package cn.net.leadu.service;

import cn.net.leadu.config.PeriodProperties;
import cn.net.leadu.dao.PurchaseApplyInfoRepository;
import cn.net.leadu.domain.PurchaseApplyInfo;
import cn.net.leadu.dto.contract.ContractMsg;
import cn.net.leadu.dto.contract.ContractStateDto;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.dto.message.MessageType;
import cn.net.leadu.utils.consts.GlobalConsts;
import cn.net.leadu.utils.status.ApprovalType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pengchao on 2017/7/1.
 */
@Service
public class ContractService {
    @Autowired
    private  ContractInterface contractInterface;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PeriodProperties periodProperties;

    @Autowired
    private PurchaseApplyInfoRepository purchaseApplyInfoRepository;

    public ResponseEntity<Message> getContractMsg(String phoneNum) {
        String period = periodProperties.getPeriod();
        PurchaseApplyInfo purchaseApplyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndPeriodAndStatus(phoneNum, period, ApprovalType.PASS.code());
        if(purchaseApplyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到订单审核状态！"), HttpStatus.OK);
        }
        String result = contractInterface.query("queryContractState", "", GlobalConsts.TIMESTAMP.value(),
                    GlobalConsts.SIGN.value(), purchaseApplyInfo.getApplyNum(), null, null, null, null, null, null, null, null);
        ContractMsg contractMsg = null;
        try {
            contractMsg = objectMapper.readValue(result, ContractMsg.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询审核状态失败！"), HttpStatus.OK);
        }
        if(contractMsg.getContractinfo() == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到订单审核状态！"), HttpStatus.OK);
        }
        ContractStateDto contractInfo =contractMsg.getContractinfo();
        List<Map> list = (List<Map>) contractInfo.getContractstatelist();
        if(list.isEmpty()){
            Map resultMap = new HashMap();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String firstCommitTime = sdf.format(purchaseApplyInfo.getUpdateTime());
            resultMap.put("BASQZT", "申请待审核");
            resultMap.put("BASHRQ", firstCommitTime);
            list.add(resultMap);
            contractMsg.getContractinfo().setContractstatelist(list);
        }
        Collections.sort(list, (arg0, arg1) -> Long.parseLong(arg0.get("BASHRQ").toString()) > Long.parseLong(arg1.get("BASHRQ").toString()) ? 1 : -1);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contractMsg), HttpStatus.OK);
    }
}
