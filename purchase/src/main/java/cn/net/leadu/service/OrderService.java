package cn.net.leadu.service;

import cn.net.leadu.config.PeriodProperties;
import cn.net.leadu.dao.PurchaseApplyInfoRepository;
import cn.net.leadu.dao.PurchaseOrderInfoRepository;
import cn.net.leadu.dao.VehicleInfoRepository;
import cn.net.leadu.domain.PurchaseApplyInfo;
import cn.net.leadu.domain.PurchaseApproval;
import cn.net.leadu.domain.PurchaseOrderInfo;
import cn.net.leadu.domain.VehicleInfo;
import cn.net.leadu.dto.*;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.dto.message.MessageType;
import cn.net.leadu.dto.order.*;
import cn.net.leadu.utils.commons.ItemDeserialize;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.net.leadu.utils.consts.GlobalConsts;
import  cn.net.leadu.dao.PurchaseApprovalRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pengchao on 2017/6/30.
 */
@Service
public class OrderService {
    @Autowired
    private PeriodProperties periodProperties;

    @Autowired
    private VehicleInfoRepository vehicleInfoRepository;

    @Autowired
    private PurchaseOrderInfoRepository purchaseOrderInfoRepository;

    @Autowired
    private JsonDecoder jsonDecoder;

    @Autowired
    private SaleInterface saleInterface;

    @Autowired
    private PurchaseApprovalRepository purchaseApprovalRepository;

    @Autowired
    private ApprovalInterface approvalInterface;

    @Autowired
    private ApprovalApplyInterface approvalApplyInterface;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PurchaseApplyInfoRepository purchaseApplyInfoRepository;

    /**
     * 车型选择页面初始化数据取得
     *
     * @param phoneNum
     * @return
     */
    @Transactional
    public ResponseEntity<Message> init(String phoneNum) {

        // 获取车辆基础信息
        List<VehicleInfo> vehicleInfos = vehicleInfoRepository.findByPeriod(periodProperties.getPeriod());
        if (vehicleInfos == null) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "初始化失败！"), HttpStatus.OK);
        }
        // 车行选择radio选项值
        List<CarName> cars = new ArrayList();
        // 车辆信息集合
        List<CarInfo> carInfos = new ArrayList();
        for (VehicleInfo vehicleInfo : vehicleInfos) {
            // 车辆信息
            CarName car = new CarName(vehicleInfo.getId(), vehicleInfo);
            cars.add(car);
            // 车辆颜色
            List<String> colors = buildColor(vehicleInfo.getInnerColor(), vehicleInfo.getExternalColor());
            // 车辆特点
            List<MeritInfo> meritInfos = buildMerits(vehicleInfo.getMerits(), vehicleInfo.getMeritsUrl());
            CarInfo carInfo = new CarInfo(vehicleInfo, meritInfos, colors);
            carInfos.add(carInfo);
        }
        VehicleResult result = new VehicleResult(cars, carInfos);
        // check此手机号数据是否存在
        PurchaseOrderInfo orderInfo = purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum, periodProperties.getPeriod());
        if (orderInfo != null) {
            result.setVehicleId(orderInfo.getVehicleId());
            result.setColor(orderInfo.getVehicleColor());
            result.setProduct(orderInfo.getFinanceType());
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, result), HttpStatus.OK);
    }

    private List<String> buildColor(String innerColor, String externalColor) {
        List<String> colors = new ArrayList();
        String[] innerArr = innerColor.split(";");
        String[] externalArr = externalColor.split(";");
        for (int i = 0; i < innerArr.length; i++) {
            for (int j = 0; j < externalArr.length; j++) {
                String color = "内" + innerArr[i] + "外" + externalArr[j];
                colors.add(color);
            }
        }
        return colors;
    }

    private List<MeritInfo> buildMerits(String merits, String meritsUrl) {
        List<MeritInfo> meritInfos = new ArrayList();
        String[] meritsArr = merits.split(";");
        String[] meritsUrlArr = meritsUrl.split(";");
        for (int i = 0; i < meritsArr.length; i++) {
            MeritInfo meritInfo = new MeritInfo();
            meritInfo.setName(meritsArr[i]);
            meritInfo.setUrl(meritsUrlArr[i]);
            meritInfos.add(meritInfo);
        }
        return meritInfos;
    }

    /**
     * 车型选择页面提交操作
     *
     * @param vehicleSubmitDto
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> submit(VehicleSubmitDto vehicleSubmitDto, String phoneNum) {
        // check此手机号数据是否存在
        PurchaseOrderInfo orderInfo = purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum, periodProperties.getPeriod());
        if (orderInfo == null) {
            PurchaseOrderInfo result = new PurchaseOrderInfo();
            result.setPhoneNum(phoneNum);
            result.setVehicleId(vehicleSubmitDto.getVehicleType());
            result.setVehicleColor(vehicleSubmitDto.getColor());
            result.setFinanceType(vehicleSubmitDto.getProduct());
            result.setCreateTime(new Date());
            result.setPeriod("1");
            purchaseOrderInfoRepository.save(result);

        } else {
            orderInfo.setVehicleId(vehicleSubmitDto.getVehicleType());
            orderInfo.setVehicleColor(vehicleSubmitDto.getColor());
            orderInfo.setFinanceType(vehicleSubmitDto.getProduct());
            orderInfo.setUpdateTime(new Date());
            purchaseOrderInfoRepository.save(orderInfo);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 订单信息提交
     *
     * @param purchaseOrderInfo
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> newOrderRecord(PurchaseOrderInfo purchaseOrderInfo, String phoneNum) {
        // check此手机号数据是否存在
        PurchaseOrderInfo orderInfo = purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum, periodProperties.getPeriod());
        if (orderInfo == null) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "此用户不存在"), HttpStatus.OK);
        } else {
            orderInfo.setBankCard(purchaseOrderInfo.getBankCard());
            orderInfo.setBankUser(purchaseOrderInfo.getBankUser());
            orderInfo.setBankName(purchaseOrderInfo.getBankName());
            orderInfo.setCompany(purchaseOrderInfo.getCompany());
            orderInfo.setLiveAdd(purchaseOrderInfo.getLiveAdd());
            orderInfo.setLocalAdd(purchaseOrderInfo.getLocalAdd());
            orderInfo.setWorkAdd(purchaseOrderInfo.getWorkAdd());
            purchaseOrderInfoRepository.save(orderInfo);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 获取还款借记卡开户行
     *
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> getBanks(String phoneNum) {
        PurchaseOrderInfo orderInfo = purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum, periodProperties.getPeriod());
        if (orderInfo == null) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "此用户不存在"), HttpStatus.OK);
        }
        String result = saleInterface.getBanks("queryRepaymentBankList", orderInfo.getBankUser(), GlobalConsts.SIGN.value(), GlobalConsts.TIMESTAMP.value());
        return jsonDecoder.singlePropertyDecoder(result, "repaymentBankList");
    }

    /**
     * 初始化订单信息提交
     *
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> getNewOrderRecord(String phoneNum) {
        // check此手机号数据是否存在
        PurchaseOrderInfo orderInfo = purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum, periodProperties.getPeriod());
        if (orderInfo.getWorkAdd() == null||orderInfo.getWorkAdd().isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,orderInfo), HttpStatus.OK);
        }
    }
    /**
     * 录单提交主系统接口
     *
     * @param phoneNum
     * @return
     */
    @Transactional
    public ResponseEntity<Message> orderSubmit(String phoneNum, AttachmentDto attachmentDto) {
        String period = periodProperties.getPeriod();
        PurchaseOrderInfo purchaseOrderInfo =  purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum,period);
        if(purchaseOrderInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR , "此用户不存在"), HttpStatus.OK);
        }
        purchaseOrderInfo.setCreditProxy(attachmentDto.getCreditLetterUrl());
        purchaseOrderInfo.setDriveFrontImg(attachmentDto.getDrivingLicenseFaceUrl());
        purchaseOrderInfo.setDriveBehindImg(attachmentDto.getDrivingLicenseBackUrl());
        purchaseOrderInfo.setWorkCertificate(attachmentDto.getJobCertificateUrl());
        purchaseOrderInfo.setIdentifyBehindImg(attachmentDto.getIdCardBackUrl());
        purchaseOrderInfo.setIdentifyFrontImg(attachmentDto.getIdCardFaceUrl());
        purchaseOrderInfoRepository.save(purchaseOrderInfo);
        OrderRecordDto orderRecordDto = new OrderRecordDto();
        PurchaseOrderInfo orderInfo = purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum, period);
        List<PurchaseApproval> approvalList = purchaseApprovalRepository.findDriveInfo(phoneNum, period);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        FinancingPreApplyInfoDto resultDto = new FinancingPreApplyInfoDto();
        try {
            resultDto = ItemDeserialize.deserialize(FinancingPreApplyInfoDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR ,"获取驾驶证信息异常"), HttpStatus.OK);
        }
        orderRecordDto.setApplyId(phoneNum);
        orderRecordDto.setBankCard(orderInfo.getBankCard());
        orderRecordDto.setBankName(orderInfo.getBankName());
        orderRecordDto.setBankUser(orderInfo.getBankUser());
        orderRecordDto.setVehicleId(orderInfo.getVehicleId());
        orderRecordDto.setVehicleColor(orderInfo.getVehicleColor());
        orderRecordDto.setFinanceType(orderInfo.getFinanceType());
        orderRecordDto.setCompany(orderInfo.getCompany());
        orderRecordDto.setWorkAdd(orderInfo.getWorkAdd());
        orderRecordDto.setLiveAdd(orderInfo.getLiveAdd());
        orderRecordDto.setLocalAdd(orderInfo.getLocalAdd());
        orderRecordDto.setIdentifyFrontImg(attachmentDto.getIdCardFaceUrl());
        orderRecordDto.setIdentifyBehindImg(attachmentDto.getIdCardBackUrl());
        orderRecordDto.setDriveFrontImg(attachmentDto.getDrivingLicenseFaceUrl());
        orderRecordDto.setDriveBehindImg(attachmentDto.getDrivingLicenseBackUrl());
        orderRecordDto.setCreditProxy(attachmentDto.getCreditLetterUrl());
        orderRecordDto.setWorkCertificate(attachmentDto.getJobCertificateUrl());
        orderRecordDto.setDriveLicenceName(resultDto.getDriveLicenceName());
        orderRecordDto.setDriveLicenceNum(resultDto.getDriveLicenceNum());
        orderRecordDto.setDriveLicenceEffectiveTerm(resultDto.getDriveLicenceEffectiveTerm());
        orderRecordDto.setDriveLicenceFn(resultDto.getDriveLicenceFn());
        orderRecordDto.setQuasiDriveType(resultDto.getQuasiDriveType());
        Message financePreApplyResultDto;
        try{
            JSONObject jsonObject = JSONObject.fromObject(orderRecordDto);
            String param = jsonObject.toString();
            String result = approvalApplyInterface.coreServer("manualFinancingPreApplyCJ", param);
            financePreApplyResultDto = objectMapper.readValue(result, Message.class);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
        }
        if("ERROR".equals(financePreApplyResultDto.getStatus())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, financePreApplyResultDto.getError()), HttpStatus.OK);
        }
        PurchaseApplyInfo purchaseApplyInfo = purchaseApplyInfoRepository.findByApprovalUuidAndPeriod(phoneNum, period);
        purchaseApplyInfo.setIsAutoApproval("1");
        purchaseApplyInfo.setUpdateTime(new Date());
        purchaseApplyInfo.setOrderStatus("1");
        purchaseApplyInfoRepository.save(purchaseApplyInfo);

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }
}
