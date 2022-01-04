package cn.gson.oasys.controller.web3jeth;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

public class Web3jEth {

	public void run(){
	    try{
	      Web3j web3j =  Web3j.build(new HttpService("http://localhost:7545"));
	      Request<?,Web3ClientVersion> request = web3j.web3ClientVersion();
	      Web3ClientVersion web3ClientVersion = request.send();
	      String clientVersion = web3ClientVersion.getWeb3ClientVersion();
	      System.out.println("Client version: "+clientVersion);
	    }catch(Exception e){
	      System.out.print(e);
	    }
	  }
}
