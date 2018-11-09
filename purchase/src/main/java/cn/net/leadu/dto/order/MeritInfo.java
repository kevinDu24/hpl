package cn.net.leadu.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeritInfo {
    private String name;
    private String url;
}
