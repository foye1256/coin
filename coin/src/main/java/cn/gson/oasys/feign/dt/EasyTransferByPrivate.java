package cn.gson.oasys.feign.dt;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

public class EasyTransferByPrivate implements Serializable {
    @Data
    public static class Param implements Serializable {
        private String toAddress;//所签名的交易
        private String privateKey;//交易发送账户的私钥, HEX 格式
        private BigDecimal amount;

    }
}
