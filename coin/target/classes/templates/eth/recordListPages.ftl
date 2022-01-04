<div class="bgc-w box box-primary">
	<!--盒子头-->
	<div class="box-header">
		<h3 class="box-title">
			<a href="" class="label label-success" style="padding: 5px;margin-left:5px;">
				<span class="glyphicon glyphicon-refresh"></span> 刷新
			</a>
		</h3>
		<div class="box-tools">
			<div class="input-group" style="width: 150px;">
				<input type="text" class="form-control input-sm baseKey" placeholder="按标题查找"  value="${(baseKey)!''}"/>
				<div class="input-group-btn">
					<a class="btn btn-sm btn-default baseKetsubmit"><span
						class="glyphicon glyphicon-search"></span></a>
				</div>
			</div>
		</div>
	</div>
	<!--盒子身体-->
	<div class="box-body no-padding">
		<div class="table-responsive">
			<table class="table table-hover">
				<tr>
					<th scope="col">批次号</th>
					<th scope="col">标题</th>
					<th scope="col">创建时间</th>
					<th scope="col">操作</th>
				</tr>
				<#list blist as this>
				<tr>
					
					<td><span>${this.batchNum}</span></td>
					<td><span>${this.batchContent}</span></td>
					<td><span>${this.creattime}</span></td>
					<td>
						<a href="batchDetailedShow?batchNum=${this.batchNum}"
						class="label xiugai chakan"><span class="glyphicon glyphicon-search"></span>
							详情</a> 
					</td>
				</tr>
				</#list>
			</table>
		</div>
	</div>
	<!--盒子尾-->
	<#include "/common/paging1.ftl">
</div>
<script>
	$(function(){
		$(".chakan").click(function(){
			var $information=$(this).parents("td").siblings(".c").find("span").text();
			if( $information!=""){
				parent.changeinformation();
			}
		});
	});
</script>