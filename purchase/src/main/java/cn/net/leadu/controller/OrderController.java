package cn.net.leadu.controller;

import cn.net.leadu.domain.PurchaseOrderInfo;
import cn.net.leadu.dto.AttachmentDto;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.dto.order.VehicleSubmitDto;
import cn.net.leadu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



/**
 * Created by zcHu on 17/5/10.
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 车型选择页面初始化数据取得
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public ResponseEntity<Message> init(String phoneNum){
        return orderService.init(phoneNum);
    }

    /**
     * 车型选择页面提交操作
     * @param vehicleSubmitDto
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResponseEntity<Message> submit(@RequestBody VehicleSubmitDto vehicleSubmitDto, String phoneNum){
        return orderService.submit(vehicleSubmitDto, phoneNum);
    }

    /**
     * 订单信息提交
     * @param purchaseOrderInfo
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/neworderrecord", method = RequestMethod.POST)
    public ResponseEntity<Message> newOrderRecord(@RequestBody PurchaseOrderInfo purchaseOrderInfo, String phoneNum){
        return orderService.newOrderRecord(purchaseOrderInfo, phoneNum);
    }

    /**
     * 获取还款借记卡开户行
     * @param phoneNum 当前登录的用户
     * @return
     */
    @RequestMapping(value = "/banks", method = RequestMethod.GET)
    public ResponseEntity<Message> getBanks(String phoneNum) {
        return orderService.getBanks(phoneNum);
    }

    /**
     * 初始化订单信息提交
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/getneworderrecord", method = RequestMethod.GET)
    public ResponseEntity<Message> getNewOrderRecord( String phoneNum){
        return orderService.getNewOrderRecord(phoneNum);
    }

    /**
     * 录单提交接口
     * @param phoneNum 当前登录的用户
     * @return
     */
    @RequestMapping(value = "/ordersubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> orderSubmit(String phoneNum, @RequestBody AttachmentDto attachmentDto) {
        return orderService.orderSubmit(phoneNum, attachmentDto);
    }
}

