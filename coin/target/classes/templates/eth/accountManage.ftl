<#include "/common/commoncss.ftl">

<style type="text/css">
a {
	color: black;
}

a:hover {
	text-decoration: none;
}

.bgc-w {
	background-color: #fff;
}
.block{
	display: inline-block;
	width: 20px;
}
.co{
				color: blue;
			}
			.bl{
				color: black;
			}
			.commen{
				cursor: pointer;
			}
</style>
<div class="row" style="padding-top: 10px;">
	<!--錯誤信息提示  -->
	<div class="alert alert-danger alert-dismissable" style="display:none;" role="alert">
		错误信息:<button class="thisclose close" type="button">&times;</button>
		<span class="error-mess"></span>
	</div>
	
	<div class="col-md-2">
		<h1 style="font-size: 24px; margin: 0;" class="">钱包管理</h1>
	</div>
	<div class="col-md-10 text-right">
		<a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
			disabled="disabled">钱包管理</a>
	</div>
</div>
<div class="row" style="padding-top: 15px;">
	<div class="col-md-12">
		
		<div class="bgc-w box box-primary thistable">
			<#include "/eth/accountTable.ftl">
		</div>
	</div>
</div>


