	<!--盒子身体-->
		<div class="box-body no-padding">
			<div style="padding: 5px;">
				<a class="btn btn-sm btn-default bac chec" href="##" title="全选/反选"><span
					class="glyphicon glyphicon-unchecked"></span></a>
				<!-- <div class="btn-group">
					<a class="btn btn-sm btn-default bac sdelete" title="删除">
						<span class="glyphicon glyphicon-trash"></span></a>
					<a class="btn btn-sm btn-default bac looked" title="标为已读">
						<span class="glyphicon glyphicon-eye-open"></span></a> 
					<a class="btn btn-sm btn-default bac star"  title="星标">
						<span class="glyphicon glyphicon-star"></span></a>
					<#if mess="垃圾箱">
						<a class="btn btn-sm btn-default bac reh"  title="恢复删除">
						<span class="glyphicon glyphicon-retweet"></span></a>
					<#else>
					</#if>
				</div> -->
				<a class="btn btn-sm btn-default bac" href="" title="刷新"><span
					class="glyphicon glyphicon-refresh"></span></a>
					
				<a class="btn btn-sm btn-default bac repeat"  title="更新数量">
						<span class="glyphicon glyphicon-repeat"></span></a>
				</div>
				
			<div class="table-responsive">
				<table class="table table-hover table-striped">
					<tr>
						<th scope="col">选择</th>
						<th scope="col">&nbsp;</th>
						<th scope="col">类型</th>
						<th scope="col">地址</th>
						<th scope="col" class="commen co">${(type)!''}<span class="block"></span></th>
						<th scope="col" class="commen co">Token数量<span class="block"></span></th>
						<th scope="col" class="commen co">时间<span class="block"></span></th>
						<th scope="col">备注</th>
						
					</tr>
					<#if (alist?size gte 0) >
					<#list alist as account>
					<tr>
						<td >
						<span class="labels"><label><input name="items" type="checkbox"><i>✓</i></label></span>
						</td>
						<td><span></span></td>
						<td><span>${(account.typename)!''}</span></td>
						<td><span >${(account.address)!''}</span>
						<!-- <button class="copy" id="copyId">复制</button> -->
						</td>
						<td><span>${(account.eth)!''}</span></td>
						<td><span>${(account.usdt)!''}</span></td>
						<td><span>${(account.creattime)!''}</span></td>
						<td><span>${(account.remake)!''}</span></td>
						<td class="accountid" style="display:none;"><span>${account.accountid}</span></td>
						<td class="fullAddress" style="display:none;"><span id="fullAddress" >${account.fullAddress}</span></td>
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

		$(".commen").click(function(){
			var $sort=$(this).text();
			var type = $(".types").text();
			var page = ${(page.number)};
			$(".thistable").load("accoutPaixu",{sort:$sort,type:type,page:page});
		
		});
		 $(".repeat").click(function(){
			 var  arry=new Array();
			 var type = $(".types").text();
			 $("[name=items]:checkbox").each(function(){
				 if(this.checked){
	    				//获取被选中了的邮件id
					 var $mailid=$(this).parents("td").siblings(".fullAddress").children("span").text();
	    				arry.push($mailid);
	    			}
			 })
			 if(arry.length==0){
				 return;
			 }
			 var values=arry.toString();
			 $(".thistable").load("updateBlance",{ids:values,type:type}); 
			 
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