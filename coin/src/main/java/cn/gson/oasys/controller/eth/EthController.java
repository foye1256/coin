package cn.gson.oasys.controller.eth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import cn.gson.oasys.common.formValid.BindingResultVOUtil;
import cn.gson.oasys.common.formValid.MapToList;
import cn.gson.oasys.common.formValid.ResultEnum;
import cn.gson.oasys.common.formValid.ResultVO;
import cn.gson.oasys.model.dao.ethdao.AccountInfoDao;
import cn.gson.oasys.model.dao.ethdao.BatchDetailedInfoDao;
import cn.gson.oasys.model.dao.ethdao.BatchRecordInfoDao;
import cn.gson.oasys.model.dao.maildao.InMailDao;
import cn.gson.oasys.model.dao.maildao.MailnumberDao;
import cn.gson.oasys.model.dao.maildao.MailreciverDao;
import cn.gson.oasys.model.dao.notedao.AttachmentDao;
import cn.gson.oasys.model.dao.roledao.RoleDao;
import cn.gson.oasys.model.dao.system.StatusDao;
import cn.gson.oasys.model.dao.system.TypeDao;
import cn.gson.oasys.model.dao.user.DeptDao;
import cn.gson.oasys.model.dao.user.PositionDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.eth.AccountInfo;
import cn.gson.oasys.model.entity.eth.BatchDetailedInfo;
import cn.gson.oasys.model.entity.eth.BatchRecordInfo;
import cn.gson.oasys.model.entity.mail.Mailnumber;
import cn.gson.oasys.model.entity.system.SystemTypeList;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.services.eth.AccountServices;
import cn.gson.oasys.services.mail.MailServices;
import cn.gson.oasys.services.process.ProcessService;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.format.VerticalAlignment;
import jxl.write.*;

@Controller
//@RestController
@RequestMapping("/")
public class EthController {
	
	
	@Autowired
	private MailnumberDao mndao;
	
	@Autowired
	private StatusDao sdao;
	@Autowired
	private TypeDao tydao;
	@Autowired
	private UserDao udao;
	@Autowired
	private DeptDao ddao;
	@Autowired
	private RoleDao rdao;
	@Autowired
	private PositionDao pdao;
	@Autowired
	private InMailDao imdao;
	@Autowired
	private MailreciverDao mrdao;
	@Autowired
	private AttachmentDao AttDao;
	@Autowired
	private MailServices mservice;
	@Autowired
	private ProcessService proservice;
	
	@Autowired
	private AccountServices aServices;
	
	@Autowired
	private AccountInfoDao adao;
	
	@Autowired
	private BatchRecordInfoDao recordDao;
	
	@Autowired
	private BatchDetailedInfoDao detailedDao;
	
	/**
	 * ????????????token
	 * @return
	 */
	@RequestMapping("sendToken")
	public  String sendToken(@SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		//????????????
//		User user=udao.findOne(userId);
		
		//??????eth ??????
		List<AccountInfo>  eAccountList=adao.findByTypeOrderByCreateTimeDesc("ETH");
		//??????TRX??????
		List<AccountInfo>  bAccountList=adao.findByTypeOrderByCreateTimeDesc("TRX");
		
		//???????????????
//		Page<Pagemail> pagelist=mservice.recive(page, size, user, null,"?????????");
//		List<Map<String, Object>> maillist=mservice.mail(pagelist);
		
		Page<AccountInfo> pagelist=aServices.pages(page, size, null,"ETH","0");
		List<Map<String, Object>> alist=aServices.sendToken(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("alist",alist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("ethSize", eAccountList.size());
		model.addAttribute("bscSize", bAccountList.size());
		
		model.addAttribute("mess", "????????????");
		model.addAttribute("type", "ETH");
		model.addAttribute("sort", "&type=ETH");
		return "eth/sendToken";
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("aAccount")
	public  String account3(HttpServletRequest req,@SessionAttribute("userId") Long userId,Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
//		Pageable pa=new PageRequest(page, size);
//		User mu=udao.findOne(userId);
		String type=req.getParameter("type");
		
		Page<AccountInfo> pagelist=aServices.pages(page, size, null,type,"0");
		List<Map<String, Object>> alist=aServices.sendToken(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("alist",alist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("mess", "????????????");
		model.addAttribute("type",type);
		return "eth/allaccount";
	}
	
	/**
	 *??????????????????
	 */
	@RequestMapping("accountTitle")
	public String accountSerch(@SessionAttribute("userId") Long userId, Model model,HttpServletRequest req,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size){
		
		String type=req.getParameter("type");
		String shorts = null;
		String val=null;
		
		if(!StringUtil.isEmpty(req.getParameter("val"))){
			val=req.getParameter("val");
		}
		
		if(!StringUtil.isEmpty(req.getParameter("short"))){
			shorts=req.getParameter("short");
		}
		
		Page<AccountInfo> pagelist=aServices.pages(page, size, val,type,shorts);
		List<Map<String, Object>> alist=aServices.sendToken(pagelist);
		System.out.println("TotalPages"+pagelist.getTotalPages());
		System.out.println("Number:"+pagelist.getNumber());
		model.addAttribute("page", pagelist);
		model.addAttribute("alist",alist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("mess", "????????????");
		model.addAttribute("type",type);
		return "eth/tokenBody";
	}
	
	/**
	 * ?????????????????????
	 */
	@RequestMapping("accoutPaixu")
	public String accoutPaixu(HttpServletRequest request, @SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size){
		// ???????????????id?????????
//		User tu = udao.findOne(userId);
		//?????????????????????
		String type= null;
		String sort = null;
		if(!StringUtil.isEmpty(request.getParameter("type"))){
			type = request.getParameter("type");
		}
		
		if(!StringUtil.isEmpty(request.getParameter("sort"))){
			sort=request.getParameter("sort");
		}
		
		Page<AccountInfo> pagelist=aServices.accoutPaixu(page, size,type,sort);
		List<Map<String, Object>> alist=aServices.sendToken(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("alist",alist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("mess", "????????????");
		model.addAttribute("type",type);
		
		return "eth/tokenBody";
		
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("send")
	public  String send(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		
		User mu=udao.findOne(userId);
		//?????????????????????id
		String addressIds="";
		//???????????????????????????ID
		String ids = request.getParameter("ids");
		String type = request.getParameter("type");
		StringTokenizer st = new StringTokenizer(ids, ",");
		while (st.hasMoreElements()) {
			String nextToken = st.nextToken();
			AccountInfo  aAccountInfo=adao.findByAccountId(Long.parseLong(nextToken));
			addressIds = addressIds+aAccountInfo.getAddress()+";\n";
			System.out.println(nextToken);
		}
		
		List<String> gasList = new ArrayList<String>();
//		List<SystemTypeList> tokenlist = new ArrayList<SystemTypeList>();
		List<SystemTypeList> walletlist = new ArrayList<SystemTypeList>();
		
		if("TRX".equals(type)){
//			tokenlist=tydao.findByTypeModel("aoa_trx_token");
			walletlist=tydao.findByTypeModel("trx_main_wallet");
			model.addAttribute("gasPrice", "0");
		}else {
			//???????????????gas??????
			gasList = aServices.getGasPriceAndLimit();
//			tokenlist=tydao.findByTypeModel("aoa_eth_token");
			walletlist=tydao.findByTypeModel("eth_main_wallet");
			
			model.addAttribute("gasPrice", gasList.get(0));
		}
		
		model.addAttribute("fromAddress", walletlist.get(0).getTypeValue());
		model.addAttribute("addressIds", addressIds);
//		model.addAttribute("tokenlist", tokenlist);
		model.addAttribute("tokenType", type);
		model.addAttribute("type", type);
		
		model.addAttribute("id", "token");
		
		return "eth/editSend";
	}
	
	/**
	 * ??????token
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("pushToken")
	public String pushToken(HttpServletRequest request,@SessionAttribute("userId") Long userId) throws IllegalStateException, IOException{
//		User tu=udao.findOne(userId);
		
		String type=request.getParameter("type");
		String fromAddress=request.getParameter("fromAddress");
		String amount=request.getParameter("amount");
		String tokenType=request.getParameter("tokenType");
		String addressIds=request.getParameter("addressIds");
		String gasPrice=request.getParameter("gasPrice");
		String remake=request.getParameter("remake");
		
		aServices.pushToken(type,fromAddress,amount,tokenType,addressIds,gasPrice,remake);
		
		return "redirect:/sendToken";
	}
	
	/**
	 * token????????????
	 * @return
	 */
	@RequestMapping("imputationToken")
	public  String imputationToken(@SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		//????????????
//		User user=udao.findOne(userId);
		
		//??????eth ??????
		List<AccountInfo>  eAccountList=adao.findByTypeOrderByCreateTimeDesc("ETH");
		//??????TRX??????
		List<AccountInfo>  bAccountList=adao.findByTypeOrderByCreateTimeDesc("TRX");
		
		Page<AccountInfo> pagelist=aServices.pages(page, size, null,"ETH","0");
		List<Map<String, Object>> alist=aServices.sendToken(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("alist",alist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("ethSize", eAccountList.size());
		model.addAttribute("bscSize", bAccountList.size());
		
		model.addAttribute("mess", "????????????");
		model.addAttribute("type", "ETH");
		model.addAttribute("sort", "&type=ETH");
		
		return "eth/imputationToken";
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("imputation")
	public  String imputation(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		
		User mu=udao.findOne(userId);
		//?????????????????????id
		String addressIds="";
		String type = request.getParameter("type");
		//???????????????????????????ID
		String ids = request.getParameter("ids");
		StringTokenizer st = new StringTokenizer(ids, ",");
		while (st.hasMoreElements()) {
			String nextToken = st.nextToken();
			AccountInfo  aAccountInfo=adao.findByAccountId(Long.parseLong(nextToken));
			addressIds = addressIds+aAccountInfo.getAddress()+";\n";
			System.out.println(nextToken);
		}
		
		List<String> gasList = new ArrayList<String>();
		List<SystemTypeList> walletlist = new ArrayList<SystemTypeList>();
		
		if("TRX".equals(type)){
			walletlist=tydao.findByTypeModel("trx_main_wallet");
			model.addAttribute("gasPrice", "0");
		}else {
			//???????????????gas??????
			gasList = aServices.getGasPriceAndLimit();
			walletlist=tydao.findByTypeModel("eth_main_wallet");
			
			model.addAttribute("gasPrice", gasList.get(0));
		}
				
		model.addAttribute("toAddress", walletlist.get(0).getTypeValue());
		model.addAttribute("addressIds", addressIds);
		model.addAttribute("tokenType", type);
		model.addAttribute("type", type);
		model.addAttribute("id", "token");
		
		return "eth/editSend1";
	}
	
	/**
	 * ??????token
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("pushImputationToken")
	public String pushImputationToken(HttpServletRequest request,@SessionAttribute("userId") Long userId) throws IllegalStateException, IOException{
//		User tu=udao.findOne(userId);
		String type=request.getParameter("type");
		String toAddress=request.getParameter("toAddress");
		String amount=request.getParameter("amount");
		String tokenType=request.getParameter("tokenType");
		String addressIds=request.getParameter("addressIds");
		String gasPrice=request.getParameter("gasPrice");
		String remake=request.getParameter("remake");
		
		aServices.imputationToken(type,toAddress,amount,tokenType,addressIds,gasPrice,remake);
		
		return "redirect:/imputationToken";
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("updateBlance")
	public String updateBlance(HttpServletRequest req, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size){
		
		String type=req.getParameter("type");
		//??????id
		String ids=req.getParameter("ids");
		StringTokenizer st = new StringTokenizer(ids, ",");
		
		while (st.hasMoreElements()) {
			String nextToken = st.nextToken();
			aServices.updateBlances(type,nextToken);
		}
		
		Page<AccountInfo> pagelist=aServices.pages(page, size, null,type,"0");
		List<Map<String, Object>> alist=aServices.sendToken(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("alist",alist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("mess", "????????????");
		model.addAttribute("type",type);
		return "eth/tokenBody";
		
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("accountmanage")
	public String account(@SessionAttribute("userId") Long userId, Model model){
		// ????????????????????????id???????????????
//		User tu = udao.findOne(userId);
		
		List<SystemTypeList> typelist=tydao.findByTypeModel("aoa_type");
		
		
		model.addAttribute("typelist", typelist);
//		model.addAttribute("addresss", "");
		model.addAttribute("url", "creatAccount");
		return "eth/accountManage1";
	}

	/**
	 * ????????????
	 */
	@RequestMapping("creatAccount")
	public String creatAccount(HttpServletRequest request, @SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size){
		// ???????????????id?????????
//		User tu = udao.findOne(userId);
		//?????????????????????
		String num =null;
		String remake = null;
		String type =null;
		if(!StringUtil.isEmpty(request.getParameter("num"))){
			
			num = request.getParameter("num");
		}
		
		if(!StringUtil.isEmpty(request.getParameter("remake"))){
			
			remake = request.getParameter("remake");
		}
		
		if(!StringUtil.isEmpty(request.getParameter("type"))){
			
			type = request.getParameter("type");
		}
		
		Map<String, Object> map=aServices.creatAccount(num,type,remake);
		
		List<SystemTypeList> typelist=tydao.findByTypeModel("aoa_type");
		
		model.addAttribute("typelist", typelist);
		model.addAttribute("num", num);
		model.addAttribute("remake", remake);
		model.addAttribute("type", type);
		
		model.addAttribute("account", map.get("list"));
		model.addAttribute("addresss", map.get("addresss"));
		model.addAttribute("url", "creatAccount");
		
		return "eth/accountManage2";
	}
	
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("batchRecordList")
	public  String batchRecordList(@SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		
		Page<BatchRecordInfo> pagelist=aServices.getRecordPages(page, size, null);
		List<Map<String, Object>> blist=aServices.batchRecord(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("blist",blist);
		model.addAttribute("url","recordTitle");
		model.addAttribute("mess", "????????????");
		
		return "eth/recordList";
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping("batchDetailedShow")
	public  String batchDetailedShow(HttpServletRequest request,@SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		
		String batchNum = request.getParameter("batchNum");
		
		BatchRecordInfo recordInfo = recordDao.findByBatchNum(batchNum);
		List<BatchDetailedInfo> detailedList = detailedDao.getByBatchNum(batchNum);
		Page<BatchDetailedInfo> pagelist=aServices.getDetailedPages(page, size, batchNum);
		
		List<Map<String, Object>> blist=aServices.batchDetailed(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("blist",blist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("size", detailedList.size());
		
		model.addAttribute("mess", "????????????");
		model.addAttribute("type", recordInfo.getType());
		model.addAttribute("batchNum", batchNum);
		
		return "eth/detailedList";
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("batchSend")
	public  String batchSend(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		
//		User mu=udao.findOne(userId);
		//?????????????????????id
		String addressIds="";
		//???????????????????????????ID
		String type = request.getParameter("type");
		String ids = request.getParameter("ids");
		
		StringTokenizer st = new StringTokenizer(ids, ",");
		while (st.hasMoreElements()) {
			addressIds = addressIds+st.nextToken()+";\n";
		}
		
		List<String> gasList = new ArrayList<String>();
		List<SystemTypeList> walletlist = new ArrayList<SystemTypeList>();
		
		if("TRX".equals(type)){
			walletlist=tydao.findByTypeModel("trx_main_wallet");
			model.addAttribute("gasPrice", "0");
		}else {
			//???????????????gas??????
			gasList = aServices.getGasPriceAndLimit();
			walletlist=tydao.findByTypeModel("eth_main_wallet");
			
			model.addAttribute("gasPrice", gasList.get(0));
		}
		
		model.addAttribute("fromAddress", walletlist.get(0).getTypeValue());
		model.addAttribute("addressIds", addressIds);
		model.addAttribute("tokenType", type);
		model.addAttribute("type", type);
		model.addAttribute("id", "token");
		
		return "eth/detailedEditSend";
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("batchImputation")
	public  String batchImputation(Model model, @SessionAttribute("userId") Long userId,HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size) {
		
		//?????????????????????id
		String addressIds="";
		//???????????????????????????ID
		String type = request.getParameter("type");
		String ids = request.getParameter("ids");
		StringTokenizer st = new StringTokenizer(ids, ",");
		while (st.hasMoreElements()) {
			addressIds = addressIds+st.nextToken()+";\n";
		}
		
		List<String> gasList = new ArrayList<String>();
		List<SystemTypeList> walletlist = new ArrayList<SystemTypeList>();
		
		if("TRX".equals(type)){
			walletlist=tydao.findByTypeModel("trx_main_wallet");
			model.addAttribute("gasPrice", "0");
		}else {
			//???????????????gas??????
			gasList = aServices.getGasPriceAndLimit();
			walletlist=tydao.findByTypeModel("eth_main_wallet");
			
			model.addAttribute("gasPrice", gasList.get(0));
		}
				
		model.addAttribute("toAddress", walletlist.get(0).getTypeValue());
		model.addAttribute("addressIds", addressIds);
		model.addAttribute("tokenType", type);
		model.addAttribute("type", type);
		model.addAttribute("id", "token");
		
		return "eth/detailedEditSend2";
	}
	
	/**
	 * ??????token
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("batchPushToken")
	public String batchPushToken(HttpServletRequest request,@SessionAttribute("userId") Long userId) throws IllegalStateException, IOException{
		
		String type=request.getParameter("type");
		String fromAddress=request.getParameter("fromAddress");
		String amount=request.getParameter("amount");
		String tokenType=request.getParameter("tokenType");
		String addressIds=request.getParameter("addressIds");
		String gasPrice=request.getParameter("gasPrice");
		String remake=request.getParameter("remake");
		
		aServices.pushToken(type,fromAddress,amount,tokenType,addressIds,gasPrice,remake);
		
		return "redirect:/batchRecordList";
	}
	
	/**
	 * ??????token
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("batchPushImputationToken")
	public String batchPushImputationToken(HttpServletRequest request,@SessionAttribute("userId") Long userId) throws IllegalStateException, IOException{
		
		String type=request.getParameter("type");
		String toAddress=request.getParameter("toAddress");
		String amount=request.getParameter("amount");
		String tokenType=request.getParameter("tokenType");
		String addressIds=request.getParameter("addressIds");
		String gasPrice=request.getParameter("gasPrice");
		String remake=request.getParameter("remake");
		
		aServices.imputationToken(type,toAddress,amount,tokenType,addressIds,gasPrice,remake);
		
		return "redirect:/batchRecordList";
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("detailedUpdateBlance")
	public String detailedUpdateBlance(HttpServletRequest req, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size){
		
		String batchNum = req.getParameter("batchNum");
		
		aServices.updateDetailedBlances(batchNum);
		
		BatchRecordInfo recordInfo = recordDao.findByBatchNum(batchNum);
		List<BatchDetailedInfo> detailedList = detailedDao.getByBatchNum(batchNum);
		Page<BatchDetailedInfo> pagelist=aServices.getDetailedPages(page, size, batchNum);
		
		List<Map<String, Object>> blist=aServices.batchDetailed(pagelist);
		
		model.addAttribute("page", pagelist);
		model.addAttribute("blist",blist);
		model.addAttribute("url","accountTitle");
		model.addAttribute("size", detailedList.size());
		
		model.addAttribute("mess", "????????????");
		model.addAttribute("type", recordInfo.getType());
		model.addAttribute("batchNum", batchNum);
		
		return "eth/detailedList";
		
	}
	
	/**
     * ????????????
     *
     * @return
     */
    @RequestMapping({"/download"})
    public void download() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        HttpServletRequest request = requestAttributes.getRequest();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String createTime = sdf.format(new Date());
        
        String batchNum = request.getParameter("batchNum");
        // ?????????
        String filename = "????????????_"+createTime+".xls";

        try {

            // ??????????????????
            String path = request.getSession().getServletContext().getRealPath("") + "/" + filename;

            // ???????????????????????????????????????????????????????????????linux?????????????????????
            //String path =  this.getClass().getClassLoader().getResource("").getPath()+"/"+filename;

            File name = new File(path);
            // ????????????????????????
            WritableWorkbook workbook = Workbook.createWorkbook(name);
            // ?????????
            WritableSheet sheet = workbook.createSheet("????????????", 0);
            // ????????????;
            WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

            WritableCellFormat cellFormat = new WritableCellFormat(font);
            // ??????????????????;
            cellFormat.setBackground(Colour.WHITE);
            // ????????????;
            cellFormat.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);
            // ??????????????????????????????;
            cellFormat.setAlignment(Alignment.CENTRE);
            // ??????????????????;
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            // ?????????1,5,6????????????????????????;
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 60);
            sheet.setColumnView(2, 100);
            // ???sheet???????????????????????????????????????????????????;
            sheet.getSettings().setDefaultColumnWidth(20);
            // ???sheet??????????????????????????????????????????????????????????????????1/20????????????,?????????????????????????????????????????????
            // sheet.getSettings().setDefaultRowHeight(30 * 20);
            // ??????????????????;
            cellFormat.setWrap(true);

            // ?????????
            Label label0 = new Label(0, 0, "ID", cellFormat);
            Label label1 = new Label(1, 0, "??????", cellFormat);
            Label label2 = new Label(2, 0, "??????", cellFormat);
            Label label3 = new Label(3, 0, "??????", cellFormat);

            sheet.addCell(label0);
            sheet.addCell(label1);
            sheet.addCell(label2);
            sheet.addCell(label3);

            // ????????????????????????????????????????????????????????????;
            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
            // ??????????????????????????????;
            cellFormat2.setAlignment(Alignment.CENTRE);
            // ??????????????????;
            cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat2.setBackground(Colour.WHITE);
            cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);
            cellFormat2.setWrap(true);

            // ????????????
            int n = 1;

            // ??????????????????
            List<AccountInfo> addressList = aServices.getDownloadData(batchNum);
            if (addressList != null && addressList.size() > 0) {

                // ??????
                for (AccountInfo a : addressList) {

                    Label lt0 = new Label(0, n, a.getAccountId() + "", cellFormat2);
                    Label lt1 = new Label(1, n, a.getAddress(), cellFormat2);
                    Label lt2 = new Label(2, n, a.getPrivateKey(), cellFormat2);
                    Label lt3 = new Label(3, n, a.getCreateTime().toString(), cellFormat2);

                    sheet.addCell(lt0);
                    sheet.addCell(lt1);
                    sheet.addCell(lt2);
                    sheet.addCell(lt3);

                    n++;
                }
            }

            //????????????????????????
            workbook.write();
            //?????????
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // ??????????????????excel

        OutputStream out = null;
        try {

            // 1.?????????????????????????????????
            /** ????????????jsp????????????????????????????????????????????????????????????action??????????????????????????????
             * String filename = request.getParameter("filename");
             */
            /**
             if (request.getMethod().equalsIgnoreCase("GET")) {
             filename = new String(filename.getBytes("iso8859-1"), "utf-8");
             }
             */

            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(filename, "utf-8"));

            // 2.??????
            out = response.getOutputStream();
            String path3 = request.getSession().getServletContext().getRealPath("") + "/" + filename;

            // inputStream?????????????????????????????????????????????????????????????????????
            InputStream is = new FileInputStream(path3);

            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ????????????
     *
     * @return
     */
    @RequestMapping({"/downloadAccount"})
    public void downloadAccount() {
    	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	HttpServletResponse response = requestAttributes.getResponse();
    	HttpServletRequest request = requestAttributes.getRequest();

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	String createTime = sdf.format(new Date());


    	if(!StringUtil.isEmpty(request.getParameter("addresss"))) {
    		String addresss = request.getParameter("addresss");


    		// ?????????
    		String filename = "????????????_"+createTime+".xls";

    		try {

    			// ??????????????????
    			String path = request.getSession().getServletContext().getRealPath("") + "/" + filename;

    			// ???????????????????????????????????????????????????????????????linux?????????????????????
    			//String path =  this.getClass().getClassLoader().getResource("").getPath()+"/"+filename;

    			File name = new File(path);
    			// ????????????????????????
    			WritableWorkbook workbook = Workbook.createWorkbook(name);
    			// ?????????
    			WritableSheet sheet = workbook.createSheet("????????????", 0);
    			// ????????????;
    			WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

    			WritableCellFormat cellFormat = new WritableCellFormat(font);
    			// ??????????????????;
    			cellFormat.setBackground(Colour.WHITE);
    			// ????????????;
    			cellFormat.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);
    			// ??????????????????????????????;
    			cellFormat.setAlignment(Alignment.CENTRE);
    			// ??????????????????;
    			cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
    			// ?????????1,5,6????????????????????????;
    			sheet.setColumnView(0, 15);
    			sheet.setColumnView(1, 65);
    			sheet.setColumnView(2, 100);
    			// ???sheet???????????????????????????????????????????????????;
    			sheet.getSettings().setDefaultColumnWidth(20);
    			// ???sheet??????????????????????????????????????????????????????????????????1/20????????????,?????????????????????????????????????????????
    			// sheet.getSettings().setDefaultRowHeight(30 * 20);
    			// ??????????????????;
    			cellFormat.setWrap(true);

    			// ?????????
    			Label label0 = new Label(0, 0, "ID", cellFormat);
    			Label label1 = new Label(1, 0, "??????", cellFormat);
    			Label label2 = new Label(2, 0, "??????", cellFormat);
    			Label label3 = new Label(3, 0, "??????", cellFormat);
    			Label label4 = new Label(4, 0, "??????", cellFormat);

    			sheet.addCell(label0);
    			sheet.addCell(label1);
    			sheet.addCell(label2);
    			sheet.addCell(label3);
    			sheet.addCell(label4);

    			// ????????????????????????????????????????????????????????????;
    			WritableFont font2 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
    			WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
    			// ??????????????????????????????;
    			cellFormat2.setAlignment(Alignment.CENTRE);
    			// ??????????????????;
    			cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
    			cellFormat2.setBackground(Colour.WHITE);
    			cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);
    			cellFormat2.setWrap(true);

    			// ????????????
    			int n = 1;

    			StringTokenizer st = new StringTokenizer(addresss, ",");
    			while (st.hasMoreElements()) {

    				AccountInfo accountInfo = adao.findByAddress(st.nextToken());

    				Label lt0 = new Label(0, n, accountInfo.getAccountId() + "", cellFormat2);
    				Label lt1 = new Label(1, n, accountInfo.getAddress(), cellFormat2);
    				Label lt2 = new Label(2, n, accountInfo.getPrivateKey(), cellFormat2);
    				Label lt3 = new Label(3, n, accountInfo.getCreateTime().toString(), cellFormat2);
    				Label lt4 = new Label(4, n, accountInfo.getRemake(), cellFormat2);

    				sheet.addCell(lt0);
    				sheet.addCell(lt1);
    				sheet.addCell(lt2);
    				sheet.addCell(lt3);
    				sheet.addCell(lt4);

    				n++;
    			}

    			//????????????????????????
    			workbook.write();
    			//?????????
    			workbook.close();

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		// ??????????????????excel

    		OutputStream out = null;
    		try {

    			// 1.?????????????????????????????????
    			/** ????????????jsp????????????????????????????????????????????????????????????action??????????????????????????????
    			 * String filename = request.getParameter("filename");
    			 */
    			/**
             if (request.getMethod().equalsIgnoreCase("GET")) {
             filename = new String(filename.getBytes("iso8859-1"), "utf-8");
             }
    			 */

    			response.addHeader("content-disposition", "attachment;filename="
    					+ java.net.URLEncoder.encode(filename, "utf-8"));

    			// 2.??????
    			out = response.getOutputStream();
    			String path3 = request.getSession().getServletContext().getRealPath("") + "/" + filename;

    			// inputStream?????????????????????????????????????????????????????????????????????
    			InputStream is = new FileInputStream(path3);

    			byte[] b = new byte[4096];
    			int size = is.read(b);
    			while (size > 0) {
    				out.write(b, 0, size);
    				size = is.read(b);
    			}
    			out.close();
    			is.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
	 *?????????????????? 
	 */
	@RequestMapping("batchQueryBalance")
	public String batchQueryBalance(HttpServletRequest req,Model model) {
		List<SystemTypeList> typelist=tydao.findByTypeModel("aoa_type");
		
		model.addAttribute("typelist", typelist);
		return "eth/batchQueryBalance";
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("checkBalance")
	public String checkBalance(HttpServletRequest request, @SessionAttribute("userId") Long userId, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size){
		// ???????????????id?????????
//		User tu = udao.findOne(userId);
		//?????????????????????
		String token =null;
		String addressIds = null;
		String type =null;
		
		if(!StringUtil.isEmpty(request.getParameter("type"))){
			type = request.getParameter("type");
		}

		if(!StringUtil.isEmpty(request.getParameter("token"))){
			
			token = request.getParameter("token");
		}
		
		if(!StringUtil.isEmpty(request.getParameter("addressIds"))){
			
			addressIds = request.getParameter("addressIds");
		}
		
		List<Map<String, Object>> accountList = aServices.checkBalance(type,token,addressIds);
		
		List<SystemTypeList> typelist=tydao.findByTypeModel("aoa_type");
		
		model.addAttribute("typelist", typelist);
		
		model.addAttribute("type", type);
		model.addAttribute("token", token);
		model.addAttribute("addressIds", addressIds);
		model.addAttribute("account", accountList);
		model.addAttribute("url", "checkBalance");
		
		return "eth/batchQueryBalance1";
	}
    
}
