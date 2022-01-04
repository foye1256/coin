
<#include "/common/commoncss.ftl"> 
<script type="text/javascript" src="js/common/iconfont.js"></script> 
<script charset="utf-8" src="plugins/kindeditor/kindeditor-min.js"></script>
<script charset="utf-8" src="plugins/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="js/mail/mail.js" ></script>
<link rel="stylesheet" href="plugins/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="css/mail/mail.css" />
<link rel="stylesheet" href="css/common/iconfont.css" />




<div class="row" style="padding-top: 10px;">
	<div class="col-md-2">
		<h1 style="font-size: 24px; margin: 0;" class="">批量归集</h1>
	</div>
	<div class="col-md-10 text-right">
		<a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
			disabled="disabled">钱包管理</a>
	</div>
</div>
<div class="row" style="padding-top: 15px;">
	<!--錯誤信息提示  -->
	<div class="alert alert-danger alert-dismissable" style="display:none;" role="alert">
		错误信息:<button class="thisclose close" type="button">&times;</button>
		<span class="error-mess"></span>
	</div>

	<div class="col-md-3">
		<a class="btn btn-primary imputation"
			style="width: 100%; margin-bottom: 20px;"> <span
			class="glyphicon glyphicon-indent-right"></span> 归集
		</a>
		<div class="bgc-w box box-solid">
			<div class="box-header">
				<h3 class="box-title">类型</h3>
				<span class="btn btn-xs btn-default pull-right des mm"> <i
					class="glyphicon glyphicon-minus"></i>
				</span>
			</div>
			<ul class="nav nav-pills nav-stacked files ">
				<li style="border-left: 3px solid blue;" class="getAccount"><span
					class="glyphicon glyphicon-record le"> ETH</span>
					<#if ethSize==0>
					<#else>
					 <span class="pull-right uncheck"><i class="btn btn-xs btn-primary">${ethSize}</i></span>
					</#if>
				</li>
				<li class="setAccount"><span class="glyphicon glyphicon-record le"> TRX</span>
					<#if bscSize==0>
					<#else>
					<span class="pull-right uncheck"><i class="btn btn-xs btn-primary">${bscSize}</i></span>
					</#if>
				
				</li>
				
			</ul>
		</div>

	</div>
	<div class="col-md-9 set ">
		<#include "/eth/allaccount.ftl">
		
	</div>
</div>
<script>

	$(function(){
		$('.getAccount').on('click',function(){
			$('.set').load('aAccount',{type:"ETH"});
		});	
		$('.setAccount').on('click',function(){
			$('.set').load('aAccount',{type:"TRX"});
		});
		/* $('.send').on('click',function(){
			$('.set').load('send');
		}); */
		
		$(".imputation").click(function(){
			//提示框可能在提交之前是block状态，所以在这之前要设置成none
			$('.alert-danger').css('display', 'none');
			
		 	if(confirm("确定要进行token批量归集吗？")){
				 var  arry=new Array();
				 var type=$(".types").text();
				 $("[name=items]:checkbox").each(function(){
					 if(this.checked){
		    				//获取被选中了的邮件id
						 var $mailid=$(this).parents("td").siblings(".accountid").children("span").text();
		    				arry.push($mailid);
		    			}
				 })
				 //
				 
				 if(arry.length==0){
					
					$('.alert-danger').css('display', 'block');
					// 提示框的错误信息显示
					$('.error-mess').text("请勾选需要归集的地址！");
					return;
				 }
				 var values=arry.toString();
				 $(".set").load("imputation",{ids:values,type:type});
			 }
		 });
	});
</script>
