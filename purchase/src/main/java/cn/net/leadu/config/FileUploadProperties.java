package cn.net.leadu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pengchao on 2016/09/19.
 */
@ConfigurationProperties(prefix = "file")
@Data
public class FileUploadProperties {
    private String attachmentPath;
    private String requestAttachmentPath;
}
