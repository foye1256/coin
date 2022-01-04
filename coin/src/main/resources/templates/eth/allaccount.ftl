
<div class="yuan">
	<div class="bgc-w box box-primary">
		<!--盒子头-->
		<div class="box-header">
			<h3 class="box-title types">${type}</h3>
			<h3 class="box-title titles">${mess}</h3>
			
			<div class="box-tools">
			
				<div class="input-group" style="width: 500px; margin-top: 0px;">
					<!-- <div class="input-group" style="width: 200px; margin-top: 10px; float:left;">
						<select name="short" id="short"
										class="form-control input-sm status">
							<option value="DESC">降序</option>
							<option value="ASC">升序</option>
						</select>
					</div> -->
					
					<div class="input-group" style="width: 200px; left:20px;margin-top: 10px; float:left;">
						<input type="text" class="form-control input-sm cha"
							placeholder="备注查询" />
						<div class="input-group-btn chazhao">
							<a class="btn btn-sm btn-default"><span
								class="glyphicon glyphicon-search"></span></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="thistable">
			<#include "/eth/tokenBody.ftl">
		</div>
	</div>
</div>
<script>
$(function(){
	 $(".chazhao").click(function(){
		   
		   var con=$(".cha").val();
		   var type=$(".types").text();
		   var status= $('.status').val();
		   //var status=$("#status option:selected").val();
		  
		   $(".thistable").load("accountTitle",{val:con,type:type,status:status});
	   });
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
})
</script>