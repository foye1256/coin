<!--盒子身体-->
<div class="box-body no-padding">
	<div style="padding: 5px;">
			
		<a class="btn btn-sm btn-default bac repeats" href="downloadAccount?addresss=${addresss}"  title="下载">
						<span class="glyphicon glyphicon-arrow-down"></span></a>
	</div>
	<div class="table-responsive">
		<table class="table table-hover table-striped">
			<thead>
				<tr>
					<th scope="col">类型</th>
					<th scope="col">地址</th>
					<th scope="col">创建时间</th>
					<th scope="col">备注</th>
				</tr>
			</thead>
			<tbody class="update" id = "update">
				<#if account??> <#list account as list>
				<tr>
					<td><span>${list.typename}</span></td>
					<td class="address" ><span id="address" class="address" >${list.accountname}</span></td>
					<td><span>${list.creattime}</span></td>
					<td><span>${list.remake}</span></td>
				</tr>
				</#list> </#if>
			</tbody>
		</table>
	</div>
</div>
<!--盒子尾-->

<script>

$(function(){
	/**
	 * checkebox的全选与反选
	 */
	
	$("[name=items]:checkbox").click(function(){
		var flag=true;
		
		$("[name=items]:checkbox").each(function(){
			if(!this.checked){
				flag=false;
			}
		});
		if(flag){
			    $(".chec span").removeClass("glyphicon-unchecked").addClass("glyphicon-stop");
		}else{
			$(".chec span").removeClass("glyphicon-stop").addClass("glyphicon-unchecked");
		}
		if ($(this).prop('checked')) {
			 $(this).attr("checked","checked");
		} else {
			$(this).removeAttr("checked");
		}

	})
	
	$(".chec").click(function(e){
		e.preventDefault();
		var $this=$(".chec span");
		if($this.hasClass("glyphicon-unchecked")){
			 $(".chec span").removeClass("glyphicon-unchecked").addClass("glyphicon-stop");
		}else{
			$(".chec span").removeClass("glyphicon-stop").addClass("glyphicon-unchecked");
		}
		$("[name=items]:checkbox").each(function(){
			
			if($this.hasClass("glyphicon-stop")){
				/*$(this).prop("checked","checked");*/
				$(this).prop("checked",!$(this).attr("checked"));
			}else{
				$(this).removeAttr("checked");
			}
				
		})
	})
	
	$(".close").click(function(){
		$('.alert-danger').css('display', 'none');
	
	}); 
	
	/* $(".commen").click(function(){
		var $val=$(this).text();
		$(".thistable").load("mailpaixu",{val:$val});
	
	}); */
	
	/* $(".download").click(function(){
		//提示框可能在提交之前是block状态，所以在这之前要设置成none
		$('.alert-danger').css('display', 'none');
		
		 var  arry=new Array();
		 
		 $("[name=items]:checkbox").each(function(){
			 if(this.checked){
    				//获取被选中了的邮件id
				var $address=$(this).parents("td").siblings(".address").children("span").text();
    			arry.push($address);
    		}
		 })
		 
		 if(arry.length==0){
			
			$('.alert-danger').css('display', 'block');
			// 提示框的错误信息显示
			$('.error-mess').text("请先批量生成钱包！");
			 return;
		 }
		 
		 var values=arry.toString();
		 
		 if (values.length == 0) {
		 	$('.alert-danger').css('display', 'block');
			// 提示框的错误信息显示
			$('.error-mess').text("请先批量生成钱包！");
		 	return;
		 }
		 console.log("values:"+values);
		 $(".thistable").load("downloadAccount",{ids:values}); 
	});  */
	
	$(".chazhao").click(function() {
		
		var con = $(".cha").val();
		var remake = $(".remake").val();
		var type = $(".type").val();
		$(".thistable").load("creatAccount", {
			val : con,
			remake : remake,
			type : type
		});
	});

})

</script>