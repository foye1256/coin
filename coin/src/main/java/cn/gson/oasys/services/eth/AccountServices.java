package cn.gson.oasys.services.eth;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
//import org.tron.trident.core.ApiWrapper;
//import org.tron.trident.core.contract.Trc20Contract;
//import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.crypto.tuwenitypes.Bytes32;
//import org.tron.trident.proto.Chain;
//import org.tron.trident.proto.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;

import cn.gson.oasys.common.AccountUtils;
import cn.gson.oasys.common.ByteArray;
import cn.gson.oasys.common.DesensitizeUtil;
import cn.gson.oasys.common.ERC20Token;
import cn.gson.oasys.common.StringtoDate;
import cn.gson.oasys.common.TRC20AccountUtils;
import cn.gson.oasys.common.TronUtils;
import cn.gson.oasys.feign.TronFullNodeFeign;
import cn.gson.oasys.feign.dt.EasyTransferByPrivate;
import cn.gson.oasys.feign.dt.GetTransactionSign;
import cn.gson.oasys.feign.dt.TriggerSmartContract;
import cn.gson.oasys.feign.res.HttpRes;
import cn.gson.oasys.model.dao.ethdao.AccountInfoDao;
import cn.gson.oasys.model.dao.ethdao.BatchDetailedInfoDao;
import cn.gson.oasys.model.dao.ethdao.BatchRecordInfoDao;
import cn.gson.oasys.model.dao.maildao.InMailDao;
import cn.gson.oasys.model.dao.maildao.MailnumberDao;
import cn.gson.oasys.model.dao.maildao.MailreciverDao;
import cn.gson.oasys.model.dao.notedao.AttachmentDao;
import cn.gson.oasys.model.dao.system.StatusDao;
import cn.gson.oasys.model.dao.system.TypeDao;
import cn.gson.oasys.model.entity.eth.AccountInfo;
import cn.gson.oasys.model.entity.eth.BatchDetailedInfo;
import cn.gson.oasys.model.entity.eth.BatchRecordInfo;
import cn.gson.oasys.model.entity.mail.Inmaillist;
import cn.gson.oasys.model.entity.mail.Mailnumber;
import cn.gson.oasys.model.entity.mail.Pagemail;
import cn.gson.oasys.model.entity.note.Attachment;
import cn.gson.oasys.model.entity.system.SystemStatusList;
import cn.gson.oasys.model.entity.system.SystemTypeList;
import cn.gson.oasys.model.entity.user.User;

@Service
@Transactional
public class AccountServices {
	
	private Logger log = LoggerFactory.getLogger("AccountServices");
	
	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private AccountInfoDao adao;
	
	@Autowired
	private MailreciverDao mrdao;

	@Autowired
	private InMailDao imdao;

	@Autowired
	private BatchRecordInfoDao recordDao;

	@Autowired
	private BatchDetailedInfoDao detailedDao;
	
	@Autowired
    private TronFullNodeFeign feign;
	
	private String rootpath;
	
	private static final BigDecimal WEI = new BigDecimal("1000000000000000000");
	//token的精度  就是小数点后面有多少位小数 然后1后面加多少个0就可以
    private static final BigDecimal decimal = new BigDecimal("1000000");
    
	@PostConstruct
	public void UserpanelController(){
		try {
			rootpath= ResourceUtils.getURL("classpath:").getPath().replace("/target/classes/","/static/attachment");
			System.out.println(rootpath);

		}catch (IOException e){
			System.out.println("获取项目路径异常");
		}
	}
	/**
	 * 收件箱
	 */
	public Page<Pagemail> recive(int page,int size,User tu,String val,String title){
		Page<Pagemail> pagelist=null;
		Pageable pa=new PageRequest(page, size);
		List<Order> orders = new ArrayList<>();
		SystemStatusList status=sdao.findByStatusModelAndStatusName("aoa_in_mail_list", val);
		SystemTypeList type=tydao.findByTypeModelAndTypeName("aoa_in_mail_list", val);
		if(("收件箱").equals(title)){
			if(StringUtil.isEmpty(val)){
				orders.add(new Order(Direction.ASC, "read"));
				Sort sort = new Sort(orders);
				pa=new PageRequest(page, size,sort);
				pagelist=mrdao.findmail(tu,false,pa);
			}else if(!Objects.isNull(status)){
				pagelist=mrdao.findmailbystatus(tu,status.getStatusId(),false,pa);
			}else if(!Objects.isNull(type)){
				pagelist=mrdao.findmailbytype(tu,type.getTypeId(),false,pa);
			}else{
				pagelist=mrdao.findmails(tu,false, val, pa);
			}
		}else{
			if(StringUtil.isEmpty(val)){
				orders.add(new Order(Direction.ASC, "read"));
				Sort sort = new Sort(orders);
				pa=new PageRequest(page, size,sort);
				pagelist=mrdao.findmail(tu,true,pa);
			}else if(!Objects.isNull(status)){
				pagelist=mrdao.findmailbystatus(tu,status.getStatusId(),true,pa);
			}else if(!Objects.isNull(type)){
				pagelist=mrdao.findmailbytype(tu,type.getTypeId(),false,pa);
			}else{
				pagelist=mrdao.findmails(tu, true,val, pa);
			}
		}
		return pagelist;
	}


	/**
	 * 封装json
	 */
	public List<Map<String,Object>> mail(Page<Pagemail> mail){
		List<Pagemail> maillist=mail.getContent();
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < maillist.size(); i++) {
			Map<String,Object> result=new HashMap<>();
			String typename=tydao.findname(maillist.get(i).getMailType());
			SystemStatusList status=sdao.findOne(maillist.get(i).getMailStatusid());
			result.put("typename", typename);
			result.put("statusname", status.getStatusName());
			result.put("statuscolor", status.getStatusColor());
			result.put("star", maillist.get(i).getStar());
			result.put("read", maillist.get(i).getRead());
			result.put("time", maillist.get(i).getMailCreateTime());
			result.put("reciver", maillist.get(i).getInReceiver());
			result.put("title", maillist.get(i).getMailTitle());
			result.put("mailid", maillist.get(i).getMailId());
			result.put("fileid", maillist.get(i).getMailFileid());
			list.add(result);

		}
		return list;
	}

	/**
	 * 发件箱
	 */
	public Page<Inmaillist> inmail(int page,int size,User tu,String val,String title){
		Page<Inmaillist> pagemail=null;
		Pageable pa=new PageRequest(page, size);
		List<Order> orders = new ArrayList<>();
		SystemStatusList status=sdao.findByStatusModelAndStatusName("aoa_in_mail_list", val);
		SystemTypeList type=tydao.findByTypeModelAndTypeName("aoa_in_mail_list", val);
		if(("发件箱").equals(title)){
			if(StringUtil.isEmpty(val)){
				orders.add(new Order(Direction.DESC, "mailStatusid"));
				Sort sort = new Sort(orders);
				pa=new PageRequest(page, size,sort);
				pagemail=imdao.findByPushAndMailUseridAndDelOrderByMailCreateTimeDesc(true,tu,false,pa);
			}else if(!Objects.isNull(status)){
				pagemail=imdao.findByMailUseridAndMailStatusidAndPushAndDelOrderByMailCreateTimeDesc(tu, status.getStatusId(),true,false, pa);
			}else if(!Objects.isNull(type)){
				pagemail=imdao.findByMailUseridAndMailTypeAndPushAndDelOrderByMailCreateTimeDesc(tu, type.getTypeId(),true,false, pa);
			}else{
				pagemail=imdao.findbymailUseridAndPushAndDel(tu,true,false,val,pa);
			}
		}else{
			//草稿箱
			if(StringUtil.isEmpty(val)){
				orders.add(new Order(Direction.DESC, "mailStatusid"));
				Sort sort = new Sort(orders);
				pa=new PageRequest(page, size,sort);
				pagemail=imdao.findByPushAndMailUseridAndDelOrderByMailCreateTimeDesc(false,tu,false,pa);
			}else if(!Objects.isNull(status)){
				pagemail=imdao.findByMailUseridAndMailStatusidAndPushAndDelOrderByMailCreateTimeDesc(tu, status.getStatusId(),false,false, pa);
			}else if(!Objects.isNull(type)){
				pagemail=imdao.findByMailUseridAndMailTypeAndPushAndDelOrderByMailCreateTimeDesc(tu, type.getTypeId(),true,false, pa);
			}else{
				pagemail=imdao.findbymailUseridAndPushAndDel(tu,false,false,val,pa);
			}
		}
		return pagemail;

	}
	/**
	 * 发件箱封装
	 */
	public List<Map<String,Object>> maillist(Page<Inmaillist> mail){
		List<Inmaillist> maillist=mail.getContent();
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < maillist.size(); i++) {
			Map<String,Object> result=new HashMap<>();
			String typename=tydao.findname(maillist.get(i).getMailType());
			SystemStatusList status=sdao.findOne(maillist.get(i).getMailStatusid());
			result.put("typename", typename);
			result.put("statusname", status.getStatusName());
			result.put("statuscolor", status.getStatusColor());
			result.put("star", maillist.get(i).getStar());
			result.put("read", true);
			result.put("time", maillist.get(i).getMailCreateTime());
			result.put("reciver", maillist.get(i).getInReceiver());
			result.put("title", maillist.get(i).getMailTitle());
			result.put("mailid", maillist.get(i).getMailId());
			result.put("fileid", maillist.get(i).getMailFileid());
			list.add(result);

		}
		return list;
	}

	/**
	 * 账号
	 * @param page
	 * @param size
	 * @param tu
	 * @param val
	 * @return
	 */
	public Page<AccountInfo> index(int page,int size,User tu,String val,Model model){
		Page<AccountInfo> account=null;
		List<Order> orders = new ArrayList<>();
		Pageable pa=new PageRequest(page, size);
		if(StringUtil.isEmpty(val)){
			orders.addAll(Arrays.asList(new Order(Direction.ASC, "status"), new Order(Direction.DESC, "createTime")));
			Sort sort = new Sort(orders);
			pa=new PageRequest(page, size, sort);
			account=adao.findByAccountUserId(tu,pa);
			//			account=adao.findListByType(pa);
		}else if (("类型").equals(val)) {
			account=adao.findByAccountUserIdOrderByType(tu,pa);
			model.addAttribute("sort", "&val="+val);
		}else if(("状态").equals(val)){
			account=adao.findByAccountUserIdOrderByStatus(tu,pa);
			model.addAttribute("sort", "&val="+val);
		}else if(("创建时间").equals(val)){
			account=adao.findByAccountUserIdOrderByCreateTimeDesc(tu,pa);
			model.addAttribute("sort", "&val="+val);
		}else{
			//名字的模糊查询
			account = adao.findByAddressLike(val,pa);
			model.addAttribute("sort", "&val="+val);
		}
		return account;
	}

	public List<Map<String, Object>> up(Page<AccountInfo> num){
		List<AccountInfo> account=num.getContent();
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < account.size(); i++) {
			Map<String, Object> result=new HashMap<>();
			SystemStatusList status=sdao.findOne(account.get(i).getStatus());
			result.put("accountid", account.get(i).getAccountId());
			result.put("typename", account.get(i).getType());
			result.put("address", account.get(i).getAddress());
			result.put("statusname", status.getStatusName());
			result.put("statuscolor", status.getStatusColor());
			result.put("creattime", account.get(i).getCreateTime());
			list.add(result);
		}
		return list;
	}

	public List<Map<String, Object>> sendToken(Page<AccountInfo> num){
		List<AccountInfo> account=num.getContent();
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < account.size(); i++) {
			Map<String, Object> result=new HashMap<>();

//			SystemStatusList status=sdao.findOne(account.get(i).getStatus());

			result.put("accountid", account.get(i).getAccountId());
//			result.put("statusname", status.getStatusName());
//			result.put("statuscolor", status.getStatusColor());
			result.put("typename", account.get(i).getType());
			if("TRX".equals(account.get(i).getType())) {
				result.put("address", DesensitizeUtil.around1(account.get(i).getAddress(), 10, 10));
			}else {
				result.put("address", DesensitizeUtil.around(account.get(i).getAddress(), 5, 4));
			}
			result.put("fullAddress", account.get(i).getAddress());
			result.put("eth", account.get(i).getBalanceValue());
			result.put("usdt", account.get(i).getBalanceValue1());
			result.put("creattime", account.get(i).getCreateTime().toString());
			result.put("remake", account.get(i).getRemake());
			list.add(result);
		}
		return list;
	}

	/**
	 * 上传附件
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public Attachment upload(MultipartFile file,User mu) throws IllegalStateException, IOException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
		File root = new File(rootpath,simpleDateFormat.format(new Date()));
		File savepath = new File(root,mu.getUserName());

		if (!savepath.exists()) {
			savepath.mkdirs();
		}
		String fileName=file.getOriginalFilename();
		if(!StringUtil.isEmpty(fileName)){
			String suffix=FilenameUtils.getExtension(fileName);
			String newFileName = UUID.randomUUID().toString().toLowerCase()+"."+suffix;
			File targetFile = new File(savepath,newFileName);
			file.transferTo(targetFile);

			Attachment attachment=new Attachment();
			attachment.setAttachmentName(file.getOriginalFilename());
			attachment.setAttachmentPath(targetFile.getAbsolutePath().replace("\\", "/").replace(rootpath, ""));
			attachment.setAttachmentShuffix(suffix);
			attachment.setAttachmentSize(file.getSize());
			attachment.setAttachmentType(file.getContentType());
			attachment.setUploadTime(new Date());
			attachment.setUserId(mu.getUserId()+"");

			return attachment;
		}
		return null;
	}

	/**
	 * 删除账号
	 */
	public void dele(Long id){
		adao.delete(id);
	}

	/**
	 * 校验中文
	 * @param str
	 * @return
	 */
	public  boolean isContainChinese(String str) {

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 发外部邮件
	 * @return 
	 */
	public void pushmail(String account,String password,String reciver,
			String name,String title,String content,String affix,String filename){
		String file=null;
		if(!StringUtil.isEmpty(affix)){
			File root = new File(rootpath,affix);
			file=root.getAbsolutePath();
		}	
		// 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
		String myEmailAccount = account;
		String myEmailPassword = password;

		// 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
		//qq  smtp.qq.com
		String myEmailSMTPHost = "smtp.qq.com";

		// 收件人邮箱（替换为自己知道的有效邮箱）
		//  String receiveMailAccount = "398005446@qq.com";

		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();                    // 参数配置
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
		props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

		// 开启 SSL 安全连接。
		// SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
		//                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
		//                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);


		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

		// 3. 创建一封邮件
		MimeMessage message;
		try {
			message = createMimeMessage(session, myEmailAccount, reciver,name ,title, content,file,filename);

			// 4. 根据 Session 获取邮件传输对象
			Transport transport = session.getTransport();

			// 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
			transport.connect(myEmailAccount, myEmailPassword);

			// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
			transport.sendMessage(message, message.getAllRecipients());

			// 7. 关闭连接
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		}



	}
	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,
			String name,String title,String content,String affix,String filename) throws Exception {
		// 1. 创建一封邮件
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
		message.setFrom(new InternetAddress(sendMail, name, "UTF-8"));

		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "XX用户", "UTF-8"));

		// 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
		message.setSubject(title, "UTF-8");

		if(!StringUtil.isEmpty(affix)){

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(content, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);
			// 添加附件
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(affix);//附件路径
			// 添加附件的内容
			messageBodyPart.setDataHandler(new DataHandler(source));
			// 添加附件的标题
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			messageBodyPart.setFileName("=?GBK?B?"+ enc.encode(filename.getBytes()) + "?=");
			multipart.addBodyPart(messageBodyPart);

			// 将multipart对象放到message中
			message.setContent(multipart,"text/html;charset=UTF-8");
		}else{
			// 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
			message.setContent(content, "text/html;charset=UTF-8");
		}
		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}


	public Map<String, Object> creatAccount(String val, String type, String remake) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		String addresss = "";
		int num = Integer.valueOf(val);
		for(int i = 0;i< num; i++) {
			try {
				Map<String, Object> result=new HashMap<>();
				AccountInfo account = new AccountInfo();
				
				Map<String, Object> accountMap = new HashMap<String, Object>();

				if("TRX".equals(type)) {
					JSONObject json = feign.generateAddress();
					String address = json.getString("address");
			        String privateKey = json.getString("privateKey");
			        String hexAddress = json.getString("hexAddress");
			        account.setAddress(address);
			        account.setPrivateKey(privateKey);
					account.setHexAddress(hexAddress);
					account.setDeal("TRC20");
				}else {
					accountMap = AccountUtils.createAccount();
					account.setDeal("ERC20");
					account.setMnemonic(accountMap.get("mnemonic")+"");
					account.setPrivateKey(accountMap.get("privateKey")+"");
					account.setPublicKey(accountMap.get("publicKey")+"");
					account.setAddress(accountMap.get("address")+"");
				}
				
				account.setType(type);
				account.setRemake(remake);
				account.setStatus(30L);
				account.setCreateTime(new Date());
				adao.save(account);

				addresss = addresss+account.getAddress()+",";
				result.put("accountid", account.getAccountId());
				result.put("typename", type);
				result.put("accountname", account.getAddress());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String createTime = sdf.format(account.getCreateTime());
				result.put("creattime", createTime);
				result.put("remake", account.getRemake());
				list.add(result);
			} catch (MnemonicLengthException e) {
				e.printStackTrace();
			}
		}
		
		map.put("addresss", addresss);
		map.put("list", list);
		return map;
	}
	
	public List<Map<String, Object>> getAccountList(List<AccountInfo> accountList) {
		List<Map<String, Object>> list = new ArrayList<>();
		for(AccountInfo account : accountList) {
			Map<String, Object> result=new HashMap<>();

			result.put("accountid", account.getAccountId());
			result.put("typename", account.getType());
			result.put("accountname", account.getAddress());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = sdf.format(account.getCreateTime());
			result.put("creattime", createTime);
			result.put("remake", account.getRemake());
			list.add(result);
		}
		return list;
	}
	
	public Page<AccountInfo> pages(int page, int size, String val, String type, String shorts) {
		Page<AccountInfo> account=null;
		Pageable pa=new PageRequest(page, size);
		List<Order> orders = new ArrayList<>();
		//SystemStatusList status=sdao.findByStatusModelAndStatusName("trans_status", val);
		
		if(StringUtil.isEmpty(val)){
			if("ASC".equals(shorts)) {
				orders.addAll(Arrays.asList(new Order(Direction.ASC, "createTime")));
			}else {
				orders.addAll(Arrays.asList(new Order(Direction.DESC, "createTime")));
			}
			
			Sort sort = new Sort(orders);
			pa=new PageRequest(page, size, sort);
			account=adao.findAccount(type,pa);
		}else {
			if("ASC".equals(shorts)) {
				orders.addAll(Arrays.asList(new Order(Direction.ASC, "createTime")));
			}else {
				orders.addAll(Arrays.asList(new Order(Direction.DESC, "createTime")));
			}
			Sort sort = new Sort(orders);
			pa=new PageRequest(page, size, sort);
			account=adao.findAccounts(type,val,pa);
		}
		

		return account;
	}
	
	public Page<AccountInfo> accoutPaixu(int page, int size, String type, String sort) {
		Page<AccountInfo> account=null;
		Pageable pa=new PageRequest(page, size);
		List<Order> orders = new ArrayList<>();
		
		if(StringUtil.isEmpty(sort)){
			orders.addAll(Arrays.asList(new Order(Direction.DESC, "createTime")));
			Sort sorts = new Sort(orders);
			pa=new PageRequest(page, size, sorts);
			account=adao.findAccount(type,pa);
		}else {
			if("时间".equals(sort)) {
				orders.addAll(Arrays.asList(new Order(Direction.DESC, "createTime")));
				Sort sorts = new Sort(orders);
				pa=new PageRequest(page, size, sorts);
				account=adao.findAccount(type,pa);
			}else if ("Token数量".equals(sort)){
				orders.addAll(Arrays.asList(new Order(Direction.DESC, "balanceValue1")));
				Sort sorts = new Sort(orders);
				pa=new PageRequest(page, size, sorts);
				account=adao.findAccount(type,pa);
			}else {
				orders.addAll(Arrays.asList(new Order(Direction.DESC, "balanceValue")));
				Sort sorts = new Sort(orders);
				pa=new PageRequest(page, size, sorts);
				account=adao.findAccount(type,pa);
			}
			
		}
		
		return account;
	}

	public void pushToken(String type,String fromAddress, String amount, String tokenType, String addressIds, String gasPrice, String remake) {

		try {
			if("TRX".equals(type)) {
				//获取账户私钥
				List<SystemTypeList> trxmainPrivate=tydao.findByTypeModel("trx_main_privateKey");
				String trxprivateKey = trxmainPrivate.get(0).getTypeValue();

				List<BatchDetailedInfo> detailList = new ArrayList<BatchDetailedInfo>();
				StringTokenizer addresss = new StringTokenizer(addressIds.replaceAll("\r\n", ""), ";");

				while (addresss.hasMoreElements()) {
					String toAddress = addresss.nextToken();
					
					//TRX
					if("TRX".equals(tokenType)) {
						
						BigDecimal values = new BigDecimal(amount);
						BigDecimal blance = balanceOfTron(fromAddress);
						if(blance.compareTo(values) > -1) {
							try {
								//快捷转账 API. 直接使用私钥从账户地址转账TRX. 集成了签名和广播功能。
								String easytransferbyprivate = easytransferbyprivate(toAddress, trxprivateKey, values.multiply(decimal));
								//使用离线方式						
//							String transferTrx = transferTrx(fromAddress,trxprivateKey,toAddress,new Long(values.multiply(decimal).toString()));
//							System.out.println(transferTrx);
								
								updateTRXBlance(toAddress,remake);
								
								BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,30L);
								detailList.add(detailedInfo);
							} catch (Exception e) {
								e.printStackTrace();
								
								updateTRXBlance(toAddress,remake+"交易异常");

								BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,31L);
								detailList.add(detailedInfo);
							}
							
						}
					}else {
						//TRC 代币
						try {
							String sendTokenTransaction = sendTokenTransaction(tokenType, fromAddress, trxprivateKey, amount, toAddress, null);
							System.out.println(sendTokenTransaction);
//						String transferToken = transferToken(fromAddress,toAddress,trxprivateKey,tokenType,amount);
//						System.out.println(transferToken);
							
							updateTokenBlance(tokenType,toAddress,remake);
							
							BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,30L);
							detailList.add(detailedInfo);
						} catch (Exception e) {
							e.printStackTrace();
							
							updateTokenBlance(tokenType,toAddress,remake+"交易异常");

							BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,31L);
							detailList.add(detailedInfo);
						}
					}
					
				}
				
				//插入批次记录信息
				if(detailList != null && detailList.size() > 0) {
					inserBatchInfo(detailList,remake);
				}
				
			}else {
				AccountUtils au = new AccountUtils();

				//获取账户私钥
				List<SystemTypeList> mainPrivate=tydao.findByTypeModel("eth_main_privateKey");
				String privateKey = mainPrivate.get(0).getTypeValue();

				List<BatchDetailedInfo> detailList = new ArrayList<BatchDetailedInfo>();

				StringTokenizer addresss = new StringTokenizer(addressIds.replaceAll("\r\n", ""), ";");
				int countTokens = addresss.countTokens();
				ExecutorService executor =   
						Executors.newFixedThreadPool(countTokens); //使用线程池 

				//获取nonce，交易笔数
				BigInteger nonce = au.getNonce(fromAddress);
				while (addresss.hasMoreElements()) {
					String toAddress = addresss.nextToken();

					if("ETH".equals(tokenType)) {
						BigDecimal values = new BigDecimal(amount);
						String blance = au.getBlanceOf(fromAddress);
						if(!(amount.compareTo(blance) > 0)) {
							try {
								au.transto(toAddress,values,privateKey);

								updateBlance(toAddress,remake);

								BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,30L);
								detailList.add(detailedInfo);

							} catch (Exception e) {
								e.printStackTrace();

								updateBlance(toAddress,remake+"交易异常");

								BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,31L);
								detailList.add(detailedInfo);
							}

						}
					}else {
						BigInteger values = new BigInteger(amount+"000000000000000000");

						try {
							FutureTask<String> futureTask =   
									new FutureTask<String>(new ERC20Token(fromAddress, toAddress, values, privateKey, tokenType,nonce,gasPrice));  
							//执行FutureTask ，如果call()没有执行完成依然会等待  
							executor.submit(futureTask);
							nonce = nonce.add(new BigInteger("1"));
							//							au.transferERC20Token(fromAddress, toAddress, values, privateKey, tokenType);

							updateBlance(toAddress,remake);

							BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,30L);
							detailList.add(detailedInfo);

						} catch (Exception e) {
							e.printStackTrace();

							updateBlance(toAddress,remake+"交易异常");

							BatchDetailedInfo detailedInfo = doDetailInfo(toAddress,remake,31L);
							detailList.add(detailedInfo);
						}
					}
				}

				executor.shutdown();

				//插入批次记录信息
				if(detailList != null && detailList.size() > 0) {
					inserBatchInfo(detailList,remake);
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void imputationToken(String type,String toAddress, String amount, String tokenType, String addressIds,String gasPrice,String remake) {
		try {

			if("TRX".equals(type)) {

				List<BatchDetailedInfo> detailList = new ArrayList<BatchDetailedInfo>();
				StringTokenizer addresss = new StringTokenizer(addressIds.replaceAll("\r\n", ""), ";");

				while (addresss.hasMoreElements()) {
					String fromAddress = addresss.nextToken();
					
					//获取账户私钥：
					AccountInfo  aAccountInfo=adao.findByAddress(fromAddress);
					
					//TRX
					if("TRX".equals(tokenType)) {
						
						BigDecimal values = new BigDecimal(amount);
						BigDecimal blance = balanceOfTron(fromAddress);
						if(blance.compareTo(values) > -1) {
							try {
								//快捷转账 API. 直接使用私钥从账户地址转账TRX. 集成了签名和广播功能。
								String easytransferbyprivate = easytransferbyprivate(toAddress, aAccountInfo.getPrivateKey(), values.multiply(decimal));
								
								updateTRXBlance(fromAddress,remake);
								
								BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,30L);
								detailList.add(detailedInfo);
							} catch (Exception e) {
								e.printStackTrace();
								
								updateTRXBlance(fromAddress,remake+"交易异常");

								BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,31L);
								detailList.add(detailedInfo);
							}
							
						}
					}else {
						//TRC 代币
						try {
							String sendTokenTransaction = sendTokenTransaction(tokenType, fromAddress, aAccountInfo.getPrivateKey(), amount, toAddress, null);
							System.out.println(sendTokenTransaction);
							
							updateTokenBlance(tokenType,fromAddress,remake);
							
							BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,30L);
							detailList.add(detailedInfo);
						} catch (Exception e) {
							e.printStackTrace();
							
							updateTokenBlance(tokenType,fromAddress,remake+"交易异常");

							BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,31L);
							detailList.add(detailedInfo);
						}
					}
					
				}
				
				//插入批次记录信息
				if(detailList != null && detailList.size() > 0) {
					inserBatchInfo(detailList,remake);
				}
			
			}else {
				AccountUtils au = new AccountUtils();

				List<BatchDetailedInfo> detailList = new ArrayList<BatchDetailedInfo>();
				StringTokenizer addresss = new StringTokenizer(addressIds.replaceAll("\r\n", ""), ";");
				int countTokens = addresss.countTokens();
				ExecutorService executor =   
						Executors.newFixedThreadPool(countTokens); //使用线程池 

				while (addresss.hasMoreElements()) {
					String fromAddress = addresss.nextToken();
					if("ETH".equals(tokenType)) {
						BigDecimal values = new BigDecimal(amount);
						String blance = "";
						try {
							blance = au.getBlanceOf(fromAddress);

							if(!(amount.compareTo(blance) > 0)) {
								//获取账户私钥：
								AccountInfo  aAccountInfo=adao.findByAddress(fromAddress);

								try {
									au.transto(toAddress,values,aAccountInfo.getPrivateKey());

									updateBlance(fromAddress,remake);

									BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,30L);
									detailList.add(detailedInfo);

								} catch (Exception e) {
									e.printStackTrace();

									updateBlance(fromAddress,remake+"交易异常");
									//异常处理
									BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,31L);
									detailList.add(detailedInfo);
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else {
						BigInteger values = new BigInteger(amount+"000000000000000000");

						//查询余额
						String balance = "";
						try {
							balance = au.getERC20Balance(fromAddress, tokenType);

							//余额大于归集金额，进行转账
							if(!(amount.compareTo(balance) > 0)) {
								//获取账户私钥：
								AccountInfo  aAccountInfo=adao.findByAddress(fromAddress);

								//获取nonce，交易笔数
								BigInteger nonce = au.getNonce(fromAddress);
								FutureTask<String> futureTask =   
										new FutureTask<String>(new ERC20Token(fromAddress, toAddress, values, aAccountInfo.getPrivateKey(), tokenType,nonce,gasPrice)); 
								//执行FutureTask
								executor.submit(futureTask);
								//							au.transferERC20Token(fromAddress, toAddress, values, aAccountInfo.getPrivateKey(), tokenType);

								updateBlance(fromAddress,remake);

								BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,30L);
								detailList.add(detailedInfo);
							}

						} catch (ExecutionException e) {
							e.printStackTrace();

							updateBlance(fromAddress,remake+"交易异常");

							BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,31L);
							detailList.add(detailedInfo);
						} catch (InterruptedException e) {
							e.printStackTrace();

							updateBlance(fromAddress,remake+"交易异常");
							BatchDetailedInfo detailedInfo = doDetailInfo(fromAddress,remake,31L);
							detailList.add(detailedInfo);
						}
					}
				}

				executor.shutdown();

				//插入批次记录信息
				if(detailList != null && detailList.size() > 0) {
					inserBatchInfo(detailList,remake);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void updateBlances(String type,String address) {

		try {
			AccountInfo  accountInfo=adao.findByAddress(address);
			String blance = "";
			String tokenBalance = "";
			if("TRX".equals(type)) {
				blance = balanceOfTron(accountInfo.getAddress())+"";
				//获取代币地址
				String typeToken = tydao.findTypeValue("TRXUSDT");
				tokenBalance = balanceOfTrc20(typeToken,accountInfo.getAddress())+"";
			}else {
				AccountUtils au = new AccountUtils();
				blance = au.getBlanceOf(accountInfo.getAddress());
				//获取代币地址
				String typeToken = tydao.findTypeValue("ETHUSDT");
				tokenBalance = au.getERC20Balance(accountInfo.getAddress(), typeToken);
			}
			accountInfo.setBalanceValue(blance);
			accountInfo.setBalanceValue1(tokenBalance);
			adao.updateBlances(blance,tokenBalance,accountInfo.getAccountId());
			
			List<BatchDetailedInfo> detailedList = detailedDao.findByAddress(address);
			if(detailedList != null && detailedList.size() > 0) {
				for(BatchDetailedInfo detailedInfo : detailedList) {
					detailedDao.updateBlances(blance,tokenBalance,detailedInfo.getDetailedId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateBlance(String address, String remake) {

		try {
			AccountInfo accountInfo = adao.findByAddress(address);
			AccountUtils au = new AccountUtils();
			String ethBlance = au.getBlanceOf(address);
			accountInfo.setBalanceValue(ethBlance);
			//获取代币地址
			String typeValue = tydao.findTypeValue("ETHUSDT");
			String erc20Balance = au.getERC20Balance(address, typeValue);
			accountInfo.setBalanceValue1(erc20Balance);

			String nremake = "";
			if(!StringUtil.isEmpty(accountInfo.getRemake())) {
				nremake = accountInfo.getRemake()+";\n"+ remake;
			}else {
				nremake = remake;
			}
			adao.updateBlance(ethBlance,erc20Balance,nremake,accountInfo.getAccountId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateDetailedBlances(String batchNum) {
		try {
			AccountUtils au = new AccountUtils();
			
			List<BatchDetailedInfo> detailedList = detailedDao.getByBatchNum(batchNum);
			if(detailedList != null && detailedList.size() > 0) {
				for(BatchDetailedInfo detailedInfo : detailedList) {
					String blance = "";
					String tokenBalance = "";
					if("TRX".equals(detailedInfo.getType())) {
						blance = balanceOfTron(detailedInfo.getAddress())+"";
						//获取代币地址
						String typeToken = tydao.findTypeValue("TRXUSDT");
						tokenBalance = balanceOfTrc20(typeToken,detailedInfo.getAddress())+"";
					}else {
						//获取账户私钥
						String typeValue = tydao.findTypeValue("ETHUSDT");
						blance = au.getBlanceOf(detailedInfo.getAddress());
						tokenBalance = au.getERC20Balance(detailedInfo.getAddress(), typeValue);
					}
					
					detailedDao.updateBlances(blance,tokenBalance,detailedInfo.getDetailedId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BatchDetailedInfo doDetailInfo(String toAddress, String remake, Long status) {
		BatchDetailedInfo detailedInfo = new BatchDetailedInfo();
		detailedInfo.setDetailedId(UUID.randomUUID().toString().replace("-", ""));
		detailedInfo.setAddress(toAddress);
		detailedInfo.setRemake(remake);
		detailedInfo.setDetailedNum(StringtoDate.getOrderIdByTime());

		detailedInfo.setStatus(status);
		return detailedInfo;
	}

	private void inserBatchInfo(List<BatchDetailedInfo> detailList, String remake) {
		String recordId = StringtoDate.getOrderIdByTime();

		for(BatchDetailedInfo detailedInfo : detailList) {
			AccountInfo accountInfo = adao.findByAddress(detailedInfo.getAddress());

			detailedInfo.setBatchNum(recordId);
			detailedInfo.setBalanceValue(accountInfo.getBalanceValue());
			detailedInfo.setBalanceValue1(accountInfo.getBalanceValue1());
			detailedInfo.setCreateTime(new Date());
			detailedInfo.setType(accountInfo.getType());

			detailedDao.save(detailedInfo);
		}

		BatchRecordInfo recordInfo = new BatchRecordInfo();

		recordInfo.setBatchId(UUID.randomUUID().toString().replace("-", ""));
		recordInfo.setBatchNum(recordId);
		recordInfo.setBatchContent(remake);
		recordInfo.setType(detailList.get(0).getType());
		recordInfo.setCreateTime(new Date());

		recordDao.save(recordInfo);
	}
	public Page<BatchRecordInfo> getRecordPages(int page, int size, String val) {
		Page<BatchRecordInfo> record=null;
		Pageable pa=new PageRequest(page, size);
		List<Order> orders = new ArrayList<>();

		if(StringUtil.isEmpty(val)){
			orders.addAll(Arrays.asList(new Order(Direction.DESC, "createTime")));
			Sort sort = new Sort(orders);
			pa=new PageRequest(page, size, sort);
			record=recordDao.findAll(pa);
		}else {
			orders.addAll(Arrays.asList(new Order(Direction.DESC, "createTime")));
			Sort sort = new Sort(orders);
			pa=new PageRequest(page, size, sort);
			record=recordDao.findList(val,pa);
		}

		return record;
	}

	public List<Map<String, Object>> batchRecord(Page<BatchRecordInfo> pagelist) {

		List<BatchRecordInfo> record=pagelist.getContent();
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < record.size(); i++) {
			Map<String, Object> result=new HashMap<>();

			result.put("batchId", record.get(i).getBatchId());
			result.put("batchNum", record.get(i).getBatchNum());
			result.put("batchContent", record.get(i).getBatchContent());
			result.put("creattime", record.get(i).getCreateTime().toString());
			result.put("remake", record.get(i).getRemake());
			list.add(result);
		}
		return list;

	}
	public Page<BatchDetailedInfo> getDetailedPages(int page, int size, String batchNum) {
		Page<BatchDetailedInfo> detailed=null;
		Pageable pa=new PageRequest(page, size);
		List<Order> orders = new ArrayList<>();

		orders.addAll(Arrays.asList(new Order(Direction.DESC, "createTime")));
		Sort sort = new Sort(orders);
		pa=new PageRequest(page, size, sort);
		detailed=detailedDao.findByBatchNum(batchNum,pa);

		return detailed;
	}

	public List<Map<String, Object>> batchDetailed(Page<BatchDetailedInfo> pagelist) {

		List<BatchDetailedInfo> detailed=pagelist.getContent();
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < detailed.size(); i++) {
			Map<String, Object> result=new HashMap<>();
			
			SystemStatusList status=sdao.findOne(detailed.get(i).getStatus());
			
			result.put("statusname", status.getStatusName());
			result.put("statuscolor", status.getStatusColor());
			result.put("detailedId", detailed.get(i).getDetailedId());
			result.put("typename", detailed.get(i).getType());
			result.put("batchNum", detailed.get(i).getBatchNum());
			if("TRX".equals(detailed.get(i).getType())) {
				result.put("address", DesensitizeUtil.around1(detailed.get(i).getAddress(), 10, 10));
			}else {
				result.put("address", DesensitizeUtil.around(detailed.get(i).getAddress(), 5, 4));
			}
			
			result.put("fullAddress", detailed.get(i).getAddress());
			result.put("eth", detailed.get(i).getBalanceValue());
			result.put("usdt", detailed.get(i).getBalanceValue1());
			result.put("creattime", detailed.get(i).getCreateTime().toString());
			result.put("remake", detailed.get(i).getRemake());
			list.add(result);
		}
		return list;

	}
	
	public List<String> getGasPriceAndLimit() {
		List<String> gasList = new ArrayList<String>();
		try {
			AccountUtils au = new AccountUtils();
			gasList = au.getGasPriceAndLimit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gasList;
	}
	
	public List<AccountInfo> getDownloadData(String batchNum) {
		List<AccountInfo> accountList = new ArrayList<AccountInfo>();
		List<BatchDetailedInfo> detailList = detailedDao.getByBatchNum(batchNum);
		
		if(detailList != null && detailList.size() > 0) {
			for(BatchDetailedInfo detailedInfo : detailList) {
				AccountInfo accountInfo = adao.findByAddress(detailedInfo.getAddress());
				
				accountList.add(accountInfo);
			}
		}
		return accountList;
	}
	public List<Map<String, Object>> checkBalance(String type, String token, String addressIds) {
		List<Map<String, Object>> list = new ArrayList<>();
		StringTokenizer addresss = new StringTokenizer(addressIds.replaceAll("\r\n", ""), ";");
		while (addresss.hasMoreElements()) {
			String nextToken = addresss.nextToken();
			String blance = "";
			String tokenBlance = "";
			try {
				Map<String, Object> result=new HashMap<>();

				if("TRX".equals(type)) {
					blance = balanceOfTron(nextToken)+"";
					tokenBlance = balanceOfTrc20(token,nextToken)+"";
				}else {
					AccountUtils au = new AccountUtils();
					blance = au.getBlanceOf(nextToken);
					tokenBlance = au.getERC20Balance(nextToken, token);
				}
				
				result.put("typename", type);
				result.put("accountname", nextToken);
				
				result.put("ethBlance", blance);
				result.put("balance", tokenBlance);
				list.add(result);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	/**
     * 补充0到64个字节
     *
     * @param dt
     * @return
     */
    private String addZero(String dt, int length) {
        StringBuilder builder = new StringBuilder();
        final int count = length;
        int zeroAmount = count - dt.length();
        for (int i = 0; i < zeroAmount; i++) {
            builder.append("0");
        }
        builder.append(dt);
        return builder.toString();
    }
    
	/**
     * 查询额度
     *
     * @param contract 合约地址
     * @param address  查询地址
     * @return
     */
    public BigDecimal balanceOfTrc20(String contract, String address) {
        String hexAddress = address;
        if (address.startsWith("T")) {
            hexAddress = TronUtils.toHexAddress(address);
        }
        String hexContract = contract;
        if (contract.startsWith("T")) {
            hexContract = TronUtils.toHexAddress(contract);
        }
        TriggerSmartContract.Param param = new TriggerSmartContract.Param();
        param.setContract_address(hexContract);
        param.setOwner_address(hexAddress);
        param.setFunction_selector("balanceOf(address)");
        String addressParam = addZero(hexAddress.substring(2), 64);
        param.setParameter(addressParam);
        TriggerSmartContract.Result result = feign.triggerSmartContract(param);
        if (result != null && result.isSuccess()) {
            String value = result.getConstantResult(0);
            if (value != null) {
            	BigDecimal balance = new BigDecimal(new BigInteger(value, 16)).divide(WEI, 3, RoundingMode.HALF_DOWN);
                return balance;
                
            }
        }
        return BigDecimal.ZERO;
    }
    
    private String castHexAddress(String address) {
        if (address.startsWith("T")) {
            return TronUtils.toHexAddress(address);
        }
        return address;
    }
    
    /**
     * 查询tron币数量
     *
     * @param address
     * @return
     */
    public BigDecimal balanceOfTron(String address) {
        final BigDecimal decimal = new BigDecimal("1000000");
        final int accuracy = 6;//六位小数
        Map<String, Object> param = new HashMap<>();
        param.put("address", castHexAddress(address));
        JSONObject obj = feign.getAccount(param);
        if (obj != null || obj.size() != 0) {
            BigInteger balance = obj.getBigInteger("balance");
            if(balance != null && balance.compareTo(BigInteger.ZERO) > 0) {
            	return new BigDecimal(balance).divide(decimal, accuracy, RoundingMode.FLOOR);
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 创建智能合约参数
     * @param fromAddress 
     *
     * @return
     */
    private TriggerSmartContract.Param createTriggerSmartContractParam(String fromAddress,String contractAddress) {
        TriggerSmartContract.Param tscParam = new TriggerSmartContract.Param();
        tscParam.setOwner_address(fromAddress);
        tscParam.setContract_address(contractAddress);
        tscParam.setFee_limit(1000000000L);
        return tscParam;
    }
    
    /**
     * 发送trc20交易  返回交易id
     *
     * @param toAddress 收币地址
     * @param amount    转出数量
     * @param remark    备注
     * @return
     */
    public String sendTrc20Transaction(String fromAddress,String toAddress, String amount, String remark) {
        try {
            String hexAddress = toAddress;
            if (toAddress.startsWith("T")) {
                hexAddress = TronUtils.toHexAddress(toAddress);
            }
            String hexFromAddress = fromAddress;
            if (fromAddress.startsWith("T")) {
            	hexFromAddress = TronUtils.toHexAddress(fromAddress);
            }
            if (StringUtils.isEmpty(hexAddress)) {
                return null;
            }
            if (StringUtils.isEmpty(hexFromAddress)) {
            	return null;
            }
            if (StringUtils.isEmpty(amount)) {
                return null;
            }
            BigDecimal a = new BigDecimal(amount);
            if (a.compareTo(BigDecimal.ZERO) <= 0) {
                return null;
            }
            if (remark == null) {
                remark = "";
            }
            String params = hexAddress + "@" + amount + "@" + remark;
            
            //获取代币地址
			String typeToken = tydao.findTypeValue("TRXUSDT");
	        if (typeToken.startsWith("T")) {
	        	typeToken = TronUtils.toHexAddress(typeToken);
	        }
            
            TriggerSmartContract.Param param = createTriggerSmartContractParam(hexFromAddress,typeToken);
            param.setFunction_selector("transfer(address,uint256)");
            String addressParam = addZero(hexAddress, 64);
            String amountParam = addZero(new BigDecimal(amount).multiply(decimal).toBigInteger().toString(16), 64);
            param.setParameter(addressParam + amountParam);
            System.out.println("创建交易参数:" + JSONObject.toJSONString(param));
            TriggerSmartContract.Result obj = feign.triggerSmartContract(param);
            System.out.println("创建交易结果:" + JSONObject.toJSONString(obj));
            if (!obj.isSuccess()) {
                return null;
            }
            //交易签名
            GetTransactionSign.Param signParam = new GetTransactionSign.Param();
            TriggerSmartContract.Transaction transaction = obj.getTransaction();
            transaction.getRaw_data().put("data", ByteArray.toHexString(remark.getBytes()));
            signParam.setTransaction(transaction);
            //获取账户私钥
			List<SystemTypeList> mainPrivate=tydao.findByTypeModel("eth_main_privateKey");
			String privateKey = mainPrivate.get(0).getTypeValue();
			
            signParam.setPrivateKey(privateKey);
            System.out.println("签名交易参数:" + JSONObject.toJSONString(signParam));
            Object dt = feign.getTransactionSign(signParam);
            System.out.println("签名交易结果:" + JSONObject.toJSONString(dt));
            //广播交易
            if (dt != null) {
            	System.out.println("广播交易参数:" + JSONObject.toJSONString(dt));
                JSONObject rea = feign.broadcastTransaction(dt);
                System.out.println("广播交易结果:" + JSONObject.toJSONString(rea));
                if (rea != null) {
                    Object result = rea.get("result");
                    if (result instanceof Boolean) {
                        if ((boolean) result) {
                            return (String) rea.get("txid");
                        }
                    }
                }
            }
        } catch (Throwable t) {
        	System.out.println(t.getMessage());
        }
        return null;
    }
    
    /**
     * 代币转账  trc20
     *
     * @param contract
     * @param fromAddress
     * @param privateKey  fromAddress的私钥
     * @param amount
     * @param toAddress
     * @param remark
     * @return
     */
    public String sendTokenTransaction(String contract, String fromAddress, String privateKey, String amount, String toAddress, String remark) {
        try {
            String hexFromAddress = castHexAddress(fromAddress);
            String hexToAddress = castHexAddress(toAddress);
            String hexContract = castHexAddress(contract);

            BigDecimal am = new BigDecimal(amount).multiply(WEI);
            BigInteger a = new BigInteger(am.toString());
            if (a.compareTo(BigInteger.ZERO) <= 0) {
            	log.error("转账失败:额度不符合规则 " + amount);
                return null;
            }
            if (remark == null) {
                remark = "";
            }
            TriggerSmartContract.Param param = new TriggerSmartContract.Param();
            param.setOwner_address(hexFromAddress);
            param.setContract_address(hexContract);
            param.setFee_limit(1000000000L);
            param.setFunction_selector("transfer(address,uint256)");
            String addressParam = addZero(hexToAddress, 64);
            String amountParam = addZero(a.toString(16), 64);
            param.setParameter(addressParam + amountParam);
            log.info("创建交易参数:" + JSONObject.toJSONString(param));
            TriggerSmartContract.Result obj = feign.triggerSmartContract(param);
            log.info("创建交易结果:" + JSONObject.toJSONString(obj));
            if (!obj.isSuccess()) {
            	log.error("创建交易失败");
                return null;
            }
            //交易签名
            GetTransactionSign.Param signParam = new GetTransactionSign.Param();
            TriggerSmartContract.Transaction transaction = obj.getTransaction();
            transaction.getRaw_data().put("data", ByteArray.toHexString(remark.getBytes()));
            signParam.setTransaction(transaction);
            signParam.setPrivateKey(privateKey);
            log.info("签名交易参数:" + JSONObject.toJSONString(signParam));
            Object dt = feign.getTransactionSign(signParam);
            log.info("签名交易结果:" + JSONObject.toJSONString(dt));
            //广播交易
            if (dt != null) {
            	log.info("广播交易参数:" + JSONObject.toJSONString(dt));
                JSONObject rea = feign.broadcastTransaction(dt);
                log.info("广播交易结果:" + JSONObject.toJSONString(rea));
                if (rea != null) {
                    Object result = rea.get("result");
                    if (result instanceof Boolean) {
                        if ((boolean) result) {
                            return (String) rea.get("txid");
                        }
                    }
                }
            }
        } catch (Throwable t) {
        	log.error(t.getMessage(), t);
        }
        return null;
    }
    
    public String easytransferbyprivate( String toAddress,String privateKey, BigDecimal amount){
        EasyTransferByPrivate.Param param = new EasyTransferByPrivate.Param();
        param.setAmount(amount);
        param.setPrivateKey(privateKey);
        String hexToAddress = castHexAddress(toAddress);
        param.setToAddress(hexToAddress);
//        param.setToAddress(toAddress);
        JSONObject obj = feign.easyTransferByPrivate(param);
        System.out.println(obj);
        if (obj != null) {
            HttpRes httpRes = JSON.toJavaObject(obj, HttpRes.class);
            if (httpRes.getResult().getResult()) {
                return httpRes.getTransaction().getTxID();
            }
        }
        return null;
    }
    
//    public String transferTrx(String fromAddress,String privateKey, String toAddress, long amount) throws IllegalException {
//    	
//    	ApiWrapper client = ApiWrapper.ofNile(privateKey);
//        Response.TransactionExtention result = client.transfer(fromAddress, toAddress, amount);
//        //签名交易
////        Chain.Transaction signedTransaction = client.signTransaction(result,SECP256K1.KeyPair.create(SECP256K1.PrivateKey.create(Bytes32.fromHexString(privateKey))));
//        Chain.Transaction signedTransaction = client.signTransaction(result);
//        System.out.println("签名交易结果："+signedTransaction.toString());
//        //广播交易
//        String time = client.broadcastTransaction(signedTransaction);
//     	return time;
//        
//    }
    
    /**
     * 代币转账  trc20
     */
//    public String transferToken(String fromAddress,String toAddress,String privateKey,String contractAddress,String amount) {
////    	BigDecimal am = new BigDecimal(amount).multiply(WEI);
////        BigInteger a = new BigInteger(am.toString());
//        
//    	ApiWrapper client = ApiWrapper.ofShasta(privateKey);
//
//        org.tron.trident.core.contract.Contract contract = client.getContract(contractAddress);
//
//        Trc20Contract token = new Trc20Contract(contract, fromAddress, client);
//
//        String txid = token.transfer(toAddress, Long.parseLong(amount), "memo", 1000000000L);
//
//        return txid;
//    }
    
    private void updateTRXBlance(String toAddress, String remake) {
		try {
			AccountInfo accountInfo = adao.findByAddress(toAddress);
			AccountUtils au = new AccountUtils();
			String ethBlance = balanceOfTron(toAddress)+"";
			accountInfo.setBalanceValue(ethBlance);

			String nremake = "";
			if(!StringUtil.isEmpty(accountInfo.getRemake())) {
				nremake = accountInfo.getRemake()+";\n"+ remake;
			}else {
				nremake = remake;
			}
			adao.updateBlance(ethBlance,nremake,accountInfo.getAccountId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private void updateTokenBlance(String tokenType,String toAddress, String remake) {
		try {
			AccountInfo accountInfo = adao.findByAddress(toAddress);
			AccountUtils au = new AccountUtils();
			String tokenBlance = balanceOfTrc20(tokenType,toAddress)+"";
			accountInfo.setBalanceValue1(tokenBlance);

			String nremake = "";
			if(!StringUtil.isEmpty(accountInfo.getRemake())) {
				nremake = accountInfo.getRemake()+";\n"+ remake;
			}else {
				nremake = remake;
			}
			adao.updateTokenBlance(tokenBlance,nremake,accountInfo.getAccountId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	
}
