<!-- <#include "/common/commoncss.ftl"> -->
<link rel="stylesheet" href="css/common/tanchuang.css" />
<style>
.modal-open {
    overflow: auto;
}
.box-header{
  text-align: center;
  border-bottom: 0px solid #f4f4f4;
}
.title{
	text-align: center;
}

.inpu{
 margin-top: -6px;

}

.control-label{
	display:inline-block;
	font-weight: 400;
}
.bo{
	margin: 0px auto;
	width: 80%;
}


.table th,.chebox,.table>tbody>tr>td{
font-weight: 400;
 text-align: center;
}
.inside{
width: 100%;
}
.inside thead{
background-color: rgba(76, 175, 95, 0.06);
}
.inside>tbody>tr>td{
 border-top: 0px solid #ddd;
}
.inside>tbody>tr>td{
border-bottom: 1px solid #ddd;
border-left: 1px solid #ddd;
}
.tdrig{
border-right: 1px solid #ddd;
}
.bo>tbody>tr>td,.inside>thead>tr>th {
    border-top: 0px solid #ddd;
    border-bottom: 0px solid #ddd;
    border-left: 0px solid #ddd;
}

.text {
	min-height: 100px;
}
.reciver{
	position: relative;
    float: right;
    margin-top: -28px;
    right: 5px;
    cursor: pointer;
}
</style>
<div class="row" style="padding-top: 10px;">
	<div class="col-md-2">
		<h1 style="font-size: 24px; margin: 0;" class="">批量生成钱包</h1>
	</div>
	<div class="col-md-10 text-right">
		<a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
			disabled="disabled">批量生成钱包</a>
	</div>
</div>
<div class="row" style="padding-top: 15px;">
	<div class="col-md-12">
		
		<div class="bgc-w box">
			<form action="evec" enctype="multipart/form-data" method="post" onsubmit="return check();" >
			<div class="box-header">
				<table class="bo table ">
				<tr >
					<div class="col-md-3 form-group">
							<label class="control-label" style="float:left;"><img src="images/eth.jpg" style="width:100px;height:100px;"></label>
					<div>
				</tr>
				
				<tr >
					<td class="title"><label class="control-label">数量</label></td>
					<td  colspan="6"><input type="text" class="form-control inpu" name="proId.num"/></td>
				</tr>
				
				<tr >

					<td colspan="14" style="text-align: right;" >
					<input   type="text" class="days" name="proId.procseeDays" hidden="hidden"/>
					<input   type="text" value="生成钱包" name="val" hidden="hidden"/>
						<input class="btn btn-primary" id="save" type="submit" value="提交" />
						<input class="btn btn-default" id="cancel" type="button" value="取消"
						onclick="window.history.back();" />
					</td>
					
				</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
</div>
<script>

	$(function(){
		$(".text").focus(function(){
			var $star=new Date($("#starTime").val());
			var $end=new Date($("#endTime").val());
			tt=$end.getTime()-$star.getTime();
			$(".days").val(Math.ceil(tt/ (24*60*60*1000)));
		});
	})

//表单提交前执行的onsubmit()方法；返回false时，执行相应的提示信息；返回true就提交表单到后台校验与执行
function check() {
	console.log("开始进入了");
	//提示框可能在提交之前是block状态，所以在这之前要设置成none
	$('.alert-danger').css('display', 'none');
	var isRight = 1;
	$('.form-control').each(function(index) {
		// 如果在这些input框中，判断是否能够为空
		if ($(this).val() == "") {
			if($(this).hasClass("cha")){
				return true;
			}
			// 排除哪些字段是可以为空的，在这里排除
			/* if (index == 5||index == 6) {
				return true;
			}  */
			
			// 获取到input框的兄弟的文本信息，并对应提醒；
			console.log(index);
			var errorMess = "红色提示框不能为空!";
			// 对齐设置错误信息提醒；红色边框
			$(this).parent().addClass("has-error has-feedback");
			$('.alert-danger').css('display', 'block');
			// 提示框的错误信息显示
			$('.error-mess').text(errorMess);
			
			isRight = 0;
			return false;
			
		} else {
			return true;
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

<script type="text/javascript" src="js/common/data.js"></script>
<script type="text/javascript" src="plugins/My97DatePicker/WdatePicker.js"></script>