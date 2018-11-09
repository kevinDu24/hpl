package cn.net.leadu.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Created by LEO on 16/9/29.
 */
@Data
public class CoreResult {
    @JsonIgnore
    private String modifyUserInfo;

    private CoreRes result;
}
