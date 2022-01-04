
<script charset="utf-8" src="plugins/kindeditor/kindeditor-min.js"></script>
<script charset="utf-8" src="plugins/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="js/mail/mail.js" ></script>
<link rel="stylesheet" href="plugins/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="css/common/tanchuang.css" />
<style>
.ke-container{
border-radius: 5px;
}
</style>
<div class="bgc-w box box-primary" style="min-height: 613px;">
	<!--盒子头-->
	<div class="box-header">
		<h3 class="box-title">发送Token</h3>
		<div class="box-tools">
			<div class="pull-right top">
				<button type="button" class="btn btn-md btn-primary">
					<span class="glyphicon glyphicon-chevron-down ">展开更多</span>
				</button>
			</div>
		</div>
	</div>
	<!--盒子身体-->
	<form action="batchPushToken" method="post" enctype="multipart/form-data" onsubmit="return check();" >
	<div class="box-body">
		<!--錯誤信息提示  -->
		<div class="alert alert-danger alert-dismissable" style="display:none;" role="alert">
			错误信息:<button class="thisclose close" type="button">&times;</button>
			<span class="error-mess"></span>
		</div>
		
		<input type="hidden" name="type" value="${type}">
		
		<div id="MoreDiv">
			
			<div class="form-group">
				<label class="control-label"><span>发送地址</span></label>
				<input name="fromAddress" type="text" value="${(fromAddress)!''}"
					id="ctl00_cphMain_txtSubject" class="form-control" placeholder="发送地址：" />
			</div>
		
		</div>
		
		<div class="form-group">
			<label class="control-label"><span>发送数量</span></label>
			<input name="amount" type="text" 
				id="ctl00_cphMain_txtSubject" class="form-control" placeholder="数量：" value="${(amount)!''}"/>
		</div>
			
		<div class="form-group">
			<label class="control-label"><span>代币地址</span></label>
			<input name="tokenType" type="text" 
				id="ctl00_cphMain_txtSubject" class="form-control" placeholder="请输入代币地址：" value="${(tokenType)!''}"/>
		</div>

		<div class="form-group">
			<label class="control-label"><span>汽油费</span></label>
			<input name="gasPrice" type="text" 
				id="ctl00_cphMain_txtSubject" class="form-control" placeholder="汽油费：" value="${(gasPrice)!''}"/>
		</div>

		<div class="form-group">
			<label class="control-label"><span>接收地址</span></label>
			<textarea rows="6" cols="20" name="addressIds"  class="form-control" style="margin: 0px -0.5px 0px 0px; height: 114px; width: 100%;">${(addressIds)!''}</textarea>
		</div>
		
		<div class="form-group">
			<label class="control-label"><span>备注</span></label>
			<input name="remake" type="text" 
				id="ctl00_cphMain_txtSubject" class="form-control" placeholder="备注：" value="${(remake)!''}"/>
		</div>
		
		<!--盒子尾-->
		<div class="box-footer foots">
			<div class="left1">
				<a id="ctl00_cphMain_lnbDiscard" class="btn btn-default " href="batchRecordList"><span>放弃</span></a>
			</div>
			<div class="pull-right right1 ">
				<input type="submit" class="btn btn-primary " name="fasong" value="发送">
				
			</div>
		</div>	
	</form>
</div>
<script type="text/javascript">
$(function(){
	$("#account").change(function(){
		console.log("qq");
		var options=$("#account option:selected");
		if(options.val()=="0"){
			console.log("www");
			$("#recive_list").prop("readonly",true);
		}else{
			console.log("sss");
			$("#recive_list").removeAttr("readonly");
		}
	});
});
//验证邮箱
function isMailNo(mail){
	var pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/; 
	return pattern.test(mail);
}

function alertCheck(errorMess){
		
		$('.alert-danger').css('display', 'block');
		// 提示框的错误信息显示
		$('.error-mess').text(errorMess);
		
}
//表单提交前执行的onsubmit()方法；返回false时，执行相应的提示信息；返回true就提交表单到后台校验与执行
function check() {

	console.log("开始进入了");
	//提示框可能在提交之前是block状态，所以在这之前要设置成none
	$('.alert-danger').css('display', 'none');
	var isRight = 1;
	$('.form-control').each(function(index) {
		// 如果在这些input框中，判断是否能够为空
		var brother = $(this).siblings('.control-label').text();
		if(brother != ""){
			if ($(this).val() == "") {
				 
				//var errorMess = "红色提示框不能为空!";
				
				var errorMess = "[" + brother + "输入框信息不能为空]";
				// 对齐设置错误信息提醒；红色边框
				$(this).parent().addClass("has-error has-feedback");
				$('.alert-danger').css('display', 'block');
				// 提示框的错误信息显示
				$('.error-mess').text(errorMess);
				
				isRight = 0;
				return false;
				
			}else{
				isRight = 1;
				return true;
			} 
		}
		
	});
	
	if (isRight == 0) {
		//modalShow(0);
		 return false;
	} else if (isRight == 1) {
		//modalShow(1);
		 return true;
	}
//	return false;
}


</script>
