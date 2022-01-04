package cn.gson.oasys.feign.res;

import lombok.Data;

@Data
public class HttpRes {
    private Result result;
    private Transaction transaction;
}
