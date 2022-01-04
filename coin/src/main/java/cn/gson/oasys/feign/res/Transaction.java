package cn.gson.oasys.feign.res;

import lombok.Data;

@Data
public class Transaction {
    private String signature;
    private String txID;
}
