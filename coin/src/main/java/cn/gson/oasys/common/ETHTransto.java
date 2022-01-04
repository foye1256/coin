package cn.gson.oasys.common;
import java.math.BigDecimal;
import java.util.concurrent.Callable;

public class ETHTransto implements Callable<String> {
  
	private String to;
	private BigDecimal values;
	private String privateKey;

    public ETHTransto(String to ,BigDecimal values,String privateKey) {
        this.to = to;
        this.values = values;
        this.privateKey = privateKey;
    }

    @Override
    public String call() throws Exception {
    	
    	AccountUtils au = new AccountUtils();
    	
    	au.transto(to, values, privateKey);
    	
    	return "";
    }
}