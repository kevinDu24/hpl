package cn.net.leadu.service;

import cn.net.leadu.config.PeriodProperties;
import cn.net.leadu.dao.PurchaseOrderInfoRepository;
import cn.net.leadu.dao.RedisRepository;
import cn.net.leadu.dto.PurchaseOrderInfoDto;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.dto.message.MessageType;
import cn.net.leadu.utils.status.PurchaseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by pengchao on 2017/5/25.
 */
@Service
public class SystemService {

    @Autowired
    private RedisRepository redisRepository;


    @Autowired
    private PeriodProperties periodProperties;

    @Autowired
    private PurchaseOrderInfoRepository purchaseOrderInfoRepository;



    /**
     * 采购人员登录验证
     * @param phoneNum
     * @param code
     * @return
     */
    public ResponseEntity<Message> verifyCode(String phoneNum, String code){
        if(redisRepository.get(phoneNum) == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "验证码过期,请重新获取验证码。"), HttpStatus.OK);
        }
        if(redisRepository.get(phoneNum).equals(code)) {
            String purchaseStatus = null;
            String period = periodProperties.getPeriod();
            Object purchaseOrderInfo = purchaseOrderInfoRepository.getOrderInfo(phoneNum, period);
            //首次登录，场景1，车型页
            if(purchaseOrderInfo == null){
                purchaseStatus = PurchaseStatus.REGISTER.code();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, purchaseStatus), HttpStatus.OK);
            }
            Object[] objs = (Object[]) purchaseOrderInfo;
            PurchaseOrderInfoDto purchaseOrderInfoDto = new PurchaseOrderInfoDto(objs);
            //已注册，未提交预审批，场景3，车型页
            if(purchaseOrderInfoDto.getApplyStatus() == null || ("").equals(purchaseOrderInfoDto.getApplyStatus())){
                purchaseStatus = PurchaseStatus.LOGIN.code();
            }else if(purchaseOrderInfoDto.getApplyStatus() != null && !("").equals(purchaseOrderInfoDto.getApplyStatus()) && (purchaseOrderInfoDto.getCompany()==null || ("").equals(purchaseOrderInfoDto.getCompany()))){
                //场景2，场景4，跳转到预审批结果页
                purchaseStatus = PurchaseStatus.SUBMIT.code();
            }else if((purchaseOrderInfoDto.getCompany()!=null || !("").equals(purchaseOrderInfoDto.getCompany())) && !("1").equals(purchaseOrderInfoDto.getOrderStatus())){
                //填写了新进录单，未提交，录单页
                purchaseStatus = PurchaseStatus.PASS.code();
            }else if(("1").equals(purchaseOrderInfoDto.getOrderStatus())){
                //订单提交，流程结束页
                purchaseStatus = PurchaseStatus.ORDER_SUBMIT.code();
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, purchaseStatus), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "验证码有误，请重新输入。"), HttpStatus.OK);
    }




}
