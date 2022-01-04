	<!--盒子身体-->
		<div class="box-body no-padding">
			<div style="padding: 5px;">
				<a class="btn btn-sm btn-default bac chec" href="##" title="全选/反选"><span
					class="glyphicon glyphicon-unchecked"></span></a>
				
				<a class="btn btn-sm btn-default bac" href="" title="刷新"><span
					class="glyphicon glyphicon-refresh"></span></a>
					
				<a class="btn btn-sm btn-default bac repeats" href="detailedUpdateBlance?batchNum=${batchNum}"  title="更新数量">
						<span class="glyphicon glyphicon-repeat"></span></a>
				
				<a class="btn btn-sm btn-default bac repeats" href="download?batchNum=${batchNum}"  title="下载">
						<span class="glyphicon glyphicon-arrow-down"></span></a>
				</div>
				
			<div class="table-responsive">
				<table class="table table-hover table-striped">
					<tr>
						<th scope="col">选择</th>
						<th scope="col">&nbsp;</th>
						<th scope="col">类型</th>
						<th scope="col">批次号</th>
						<th scope="col">地址</th>
						<th scope="col">${(type)!''}</th>
						<th scope="col">Token数量</th>
						<th scope="col">状态</th>
						<th scope="col">时间</th>
						<th scope="col">备注</th>
						
					</tr>
					<#if (blist?size gte 0) >
					<#list blist as detailed>
					<tr>
						<td >
						<span class="labels"><label><input name="items" type="checkbox"><i>✓</i></label></span>
						</td>
						<td><span></span></td>
						<td><span>${(detailed.typename)!''}</span></td>
						<td><span class="batchNum">${(detailed.batchNum)!''}</span></td>
						<td><span >${(detailed.address)!''}</span>
						<!-- <button class="copy" id="copyId">复制</button> -->
						</td>
						<td><span>${(detailed.eth)!''}</span></td>
						<td><span>${(detailed.usdt)!''}</span></td>
						<td>
							<div class="label ${detailed.statuscolor}">${(detailed.statusname)!''}</div>
						</td>
						<td><span>${(detailed.creattime)!''}</span></td>
						<td><span>${(detailed.remake)!''}</span></td>
						<td class="fullAddress" style="display:none;"><span id="fullAddress" >${detailed.fullAddress}</span></td>
						<td class="detailedId" style="display:none;"><span id="detailedId" >${detailed.detailedId}</span></td>
					</tr>
					</#list>
					</#if>
				</table>
			</div>
		</div>
		<!--盒子尾-->
		<#include "/common/paging.ftl">
<script>
	$(function(){

		 $(".repeat").click(function(){
			 var  arry=new Array();
			 var type = $(".types").text();
			 $("[name=items]:checkbox").each(function(){
				 
				 if(this.checked){
	    			//获取被选中了的邮件id
				 	var $mailid=$(this).parents("td").siblings(".fullAddress").children("span").text();
				 	var $batchNum=$(this).parents("td").siblings(".batchNum").children("span").text();
    				arry.push($mailid);
    			}
			 })
			 if(arry.length==0){
				 return;
			 }
			 var values=arry.toString();
			 $(".thistable").load("detailedUpdateBlance",{ids:values,type:type}); 
			 
		 });
		 
		 //批量查看
		 $(".looked").click(function(){
			
			 var  arry=new Array();
			 var title=$(".titles").text();
			 $("[name=items]:checkbox").each(function(){
				 if(this.checked){
	    				//获取被选中了的邮件id
					 var $mailid=$(this).parents("td").siblings(".mailid").children("span").text();
	    				arry.push($mailid);
	    				 var $mail=$(this).parents("td").siblings().find(".read").text();
	    				 if($mail!=""){
	    					 parent.changeemail();
	    				 }
	    			}
			 })
			 if(arry.length==0||title=="发件箱"||title=="草稿箱"){
				 return;
			 }
			 var values=arry.toString();
			 $(".thistable").load("watch",{ids:values,title:title});
			 
		 });
		 //批量标星
		 $(".star").click(function(){
			 
			 var  arry=new Array();
			 var title=$(".titles").text();
			 $("[name=items]:checkbox").each(function(){
				 if(this.checked){
	    				//获取被选中了的邮件id
					 var $mailid=$(this).parents("td").siblings(".mailid").children("span").text();
	    				arry.push($mailid);
	    			}
			 })
			 if(arry.length==0){
				 return;
			 }
			 var values=arry.toString();
			 $(".thistable").load("star",{ids:values,title:title});
			 
		 });
		 //查看
		 $('.lab').on('click',function(){
			 var $mailid=$(this).parents("td").siblings(".mailid").children("span").text();
			 var title=$(".titles").text();
			 var $mail=$(this).parents("td").siblings().find(".read").text();
			 if($mail!=""){
				 parent.changeemail();
			 }
			
				$('.set').load('smail',{id:$mailid,title:title});
				/* parent.changeemail(); */
			});
		 //重新编辑
		 $('.edit').on('click',function(){
			 var $mailid=$(this).parents("td").siblings(".mailid").children("span").text();
				$('.set').load('wmail',{id:$mailid});
			});
		 
		 //批量恢复删除
		 $('.reh').on('click',function(){
			 var  arry=new Array();
			 var title=$(".titles").text();
			 $("[name=items]:checkbox").each(function(){
				 if(this.checked){
	    				//获取被选中了的邮件id
					 var $mailid=$(this).parents("td").siblings(".mailid").children("span").text();
	    				arry.push($mailid);
	    			}
			 })
			 if(arry.length==0){
				 return;
			 }
			 var values=arry.toString();
				$('.thistable').load('refresh',{ids:values,title:title});
			});
		 
	});
	
	/* $(".copy")[0].onclick = function(){
	 	 var txt = $('#fullAddress').text();
	     Copy(txt);
	}
	// 复制微信号函数
	function Copy(str) {
		var save = function(e) {
			e.clipboardData.setData('text/plain', str);
			e.preventDefault();
		};
		document.addEventListener('copy', save);
		document.execCommand('copy');
		document.removeEventListener('copy', save);
		console.log('复制成功');
	} */
</script>