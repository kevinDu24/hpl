package cn.net.leadu.service;

import cn.net.leadu.config.FileUploadProperties;
import cn.net.leadu.config.PeriodProperties;
import cn.net.leadu.domain.PurchaseOrderInfo;
import cn.net.leadu.dto.message.Message;
import cn.net.leadu.dto.message.MessageType;
import cn.net.leadu.utils.Utils;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import cn.net.leadu.dao.PurchaseOrderInfoRepository;

/**
 * Created by pengchao on 2016/3/3.
 */
@Service
public class FileUploadService {

    @Autowired
    private FileUploadProperties fileUploadProperties;
    @Autowired
    private PeriodProperties periodProperties;
    @Autowired
    private PurchaseOrderInfoRepository purchaseOrderInfoRepository;

    /**
     * 文件上传转发器
     * @param type
     * @param file
     * @return
     */
    public ResponseEntity<Message> uploadFile(String type, MultipartFile file){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        if(type.equals("attachment")){
            return saveFile(fileUploadProperties.getAttachmentPath() + uploadDate + "/", file, fileUploadProperties.getRequestAttachmentPath() +  type + "/" + uploadDate + "/" );
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未指定上传类型"), HttpStatus.OK);
    }
    /**
     * 文件路徑保存
     * @param phoneNum
     * @param idCardFaceUrl
     * @param idCardBackUrl
     * @param creditLetterUrl
     * @param drivingLicenseFaceUrl
     * @param drivingLicenseBackUrl
     * @param jobCertificateUrl
     * @return
     */
    public ResponseEntity<Message> saveUrl(String phoneNum , String idCardFaceUrl,String idCardBackUrl,
                                           String creditLetterUrl,String drivingLicenseFaceUrl,String drivingLicenseBackUrl,String jobCertificateUrl){
        String period = periodProperties.getPeriod();
        PurchaseOrderInfo purchaseOrderInfo =  purchaseOrderInfoRepository.findByPhoneNumAndPeriod(phoneNum,period);
        if(purchaseOrderInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR , "此用户不存在"), HttpStatus.OK);
        }
        purchaseOrderInfo.setCreditProxy(creditLetterUrl);
        purchaseOrderInfo.setDriveFrontImg(drivingLicenseFaceUrl);
        purchaseOrderInfo.setDriveBehindImg(drivingLicenseBackUrl);
        purchaseOrderInfo.setWorkCertificate(jobCertificateUrl);
        purchaseOrderInfo.setIdentifyBehindImg(idCardBackUrl);
        purchaseOrderInfo.setIdentifyFrontImg(idCardFaceUrl);
        purchaseOrderInfoRepository.save(purchaseOrderInfo);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 保存文件a4f7a5ce99ad
     * @param savePath
     * @param file
     * @param serverPath
     * @return
     */
    public ResponseEntity<Message> saveFile(String savePath, MultipartFile file, String serverPath){
        Message message = null;
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + Utils.getFileSuffix(file.getOriginalFilename());
            try {
                System.out.println(serverPath + fileName);
                System.out.println(savePath + fileName);
                FileUtils.writeByteArrayToFile(new File(savePath + fileName), file.getBytes());
                Map map = Maps.newHashMap();
                map.put("url", serverPath + fileName);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, map), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件上传失败"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件为空,上传失败"), HttpStatus.OK);
    }

    /**
     * 保存多文件
     * @param savePath
     * @param files
     * @param serverPath
     * @return
     */
    public ResponseEntity<Message> saveFiles(String savePath, MultipartFile[] files, String serverPath){
        List<Map> fileList = new ArrayList<Map>();
        if(files.length == 0 ){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件为空,上传失败"), HttpStatus.OK);
        }
        for (int i=0; i<files.length; i++ ){
            if (files[i] != null && !files[i].isEmpty()) {
                String fileName = UUID.randomUUID().toString() + Utils.getFileSuffix(files[i].getOriginalFilename());
                try {
                    Map map = Maps.newHashMap();
                    System.out.println(serverPath + fileName);
                    System.out.println(savePath + fileName);
                    FileUtils.writeByteArrayToFile(new File(savePath + fileName), files[i].getBytes());
                    map.put("url", serverPath + fileName);
                    fileList.add(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件上传失败"), HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件为空,上传失败"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, fileList), HttpStatus.OK);
    }


    /**
     * 文件下载
     * @param response
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public byte[] downloadFile(HttpServletResponse response, String fileName, String loadDate, String type) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String path = null;
        if("attachment".equals(type)){
            path = fileUploadProperties.getAttachmentPath() +  loadDate + "/" + fileName;
        }else {
            String errorMessage = "抱歉. 你访问的文件不存在！";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        File file = new File(path);
        if(!file.exists()){
            String errorMessage = "抱歉. 你访问的文件不存在！";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        return null;
    }

}
