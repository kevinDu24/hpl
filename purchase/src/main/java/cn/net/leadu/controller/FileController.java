package cn.net.leadu.controller;

import cn.net.leadu.dto.message.Message;
import cn.net.leadu.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by LEO on 16/10/9.
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 文件上传
     * @param type
     * @param file
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> uploadFile(String type, MultipartFile file){
        return fileUploadService.uploadFile(type, file);
    }

    /**
     * 文件下载
     * @param response
     * @param type
     * @param fileName
     * @param loadDate
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/download/{type}/{loadDate}/{fileName}", method = RequestMethod.GET)
    public byte[] downloadFile(HttpServletResponse response, @PathVariable("type") String type, @PathVariable("fileName") String fileName, @PathVariable("loadDate") String loadDate) throws IOException {
        return fileUploadService.downloadFile(response, fileName, loadDate, type);
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
    @RequestMapping(value="/saveurl",method = RequestMethod.POST)
    public ResponseEntity<Message> saveUrl(String phoneNum , String idCardFaceUrl,String idCardBackUrl,
                                           String creditLetterUrl,String drivingLicenseFaceUrl,String drivingLicenseBackUrl,String jobCertificateUrl){
        return fileUploadService.saveUrl(phoneNum, idCardFaceUrl,idCardBackUrl,creditLetterUrl,drivingLicenseFaceUrl,drivingLicenseBackUrl,jobCertificateUrl);
    }
}
