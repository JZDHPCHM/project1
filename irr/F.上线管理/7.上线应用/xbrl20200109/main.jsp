<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="org.apache.shiro.subject.Subject" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="net.gbicc.cloud.api.SystemUser" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="../../lib/html5.js"></script>
<script type="text/javascript" src="../../lib/respond.min.js"></script>
<script type="text/javascript" src="../../lib/PIE_IE678.js"></script>
<![endif]-->
<link href="../css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="../css/H-ui.admin.css" rel="stylesheet" type="text/css" />
<link href="../skin/default/skin.css" rel="stylesheet" type="text/css" />
<link href="../../lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
<link href="../css/style.css" rel="stylesheet" type="text/css" />
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->



<!-- Ribbon 界面样式 -->
<!--
<link rel="stylesheet" type="text/css" href="../styles/themes/redmond/jquery-ui-1.9.2.custom.min.css" />
<link rel="stylesheet" type="text/css" href="../styles/css/ribbon/ribbon.css" />
<link rel="stylesheet" type="text/css" href="../styles/css/jqueryDatePickerAddon/jquery-ui-timepicker-addon.css" />
<link rel="stylesheet" type="text/css" href="../styles/css/jquery-ui-selectmenu/jquery.ui.selectmenu.css" /> 
<link rel="stylesheet" type="text/css" href="../styles/css/OverrideCss/overrideCss.css"  />
-->
<link type="text/css" rel="stylesheet" href="../../lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" />
<!-- Ribbon 界面脚本 -->
<script type="text/javascript" src="../../lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="../../lib/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" src="../../lib/jqueryDatepickerAddon/jquery-ui-timepicker-addon.min.js"></script>
<script type="text/javascript" src="../../lib/watermark/jquery.watermark.min.js"></script>
<script type="text/javascript" src="../../lib/ribbon/ribbon.min.js"></script>
<script type="text/javascript" src="../../lib/combox/jquery.ui.selectmenu.min.js"></script>
<script type="text/javascript" src="../../lib/Tab/ui.tabs.paging.min.js"></script>
<script type="text/javascript" src="../../lib/OverrideScripts/overrideScripts.js" ></script>
<title>报告编制</title><%
	Subject currentUser = SecurityUtils.getSubject();
	Session currentUserSession = currentUser.getSession();
	SystemUser user =(SystemUser)currentUserSession.getAttribute("SysUser");
	String orgName =(String)currentUserSession.getAttribute("orgName");
	Integer orgType =(Integer)currentUserSession.getAttribute("orgType");
	String orgCode = (String)currentUserSession.getAttribute("orgCode");
	Boolean isMainAcc =(Boolean)currentUserSession.getAttribute("isMainAcc");

	System.out.println("user: " + user);
	pageContext.setAttribute("userId",user.getId());
	String contextPath = this.getServletConfig().getServletContext().getContextPath();
	boolean isTrustee = false;
	if (orgType == 3){
		orgType = 1;
		isTrustee = true;
	}
	String version = request.getParameter("ver");
	Object oCmpType = currentUserSession.getAttribute("compType");
	String compType = oCmpType == null ? "" : oCmpType.toString(); //1 私募， 2 资管
	
	boolean canDownload = currentUser.hasRole("ROLE_AGENCY_AUDIT");
%>


<!-- General page styles (not critical to the demos) -->

<style type="text/css" media="all">
html, body
{
	margin: 0;
	padding: 0;
	overflow: hidden;	/* Remove scroll bars on browser window */
	font-size: 14px;
	-webkit-font-smoothing: antialiased;
	font-family: "Microsoft Yahei","Hiragino Sans GB","Helvetica Neue",Helvetica,tahoma,arial,"WenQuanYi Micro Hei",Verdana,sans-serif,"\5B8B\4F53";
}

.My-userbar{position:absolute;top: 78px;}
.My-header{line-height:39px;}

#MySplitter {
	/* Height is set to match window size in $().ready() below */
	border: 3px solid #eee;
	min-width:  600px;
	min-height: 100px;
}
.SplitterPane {
	background: #eef;
	overflow: auto;
}
.vsplitbar {
	width: 6px;
	background: #ddd url(img/vgrabber.gif) no-repeat center;
}
.vsplitbar.active, .vsplitbar:hover {
	background-color: #888;
	width: 6px;
	opacity: 0.5;
	filter: alpha(opacity=50); /* IE */
}

.sp-state-default { background-color: #ddd }
.sp-state-hover { background-color: #888; opacity: 0.5; filter: alpha(opacity=50); /* IE */}
.sp-state-highlight { background-color: #888; opacity: 0.5; filter: alpha(opacity=50); /* IE */ }
.sp-state-error { background-color: #abc0e7 }

.splitter-pane {
	overflow: auto;
}
.splitter-bar-vertical  {
	width: 6px;
	background-image: url(img/vgrabber.gif);
	background-repeat: no-repeat;
	background-position: center;
}
.splitter-bar-vertical-docked {
	width: 6px;
	background-image: url(img/vgrabber.gif);
	background-repeat: no-repeat;
	background-position: center;
}
.splitter-bar.ui-state-highlight {
	opacity: 0.7;
}
.splitter-iframe-hide {
	#visibility: hidden;
}
.toolBar-btn{padding:5px; color:#333333;background-color:transparent;background-image:none;border-color:#f5f4f4}
.toolBar-btn:hover,.toolBar-btn:focus,
.toolBar-btn:active,
.toolBar-btn.active{color:#000000;background-color:#E4E4E4;border-color:#f5f4f4;}
.toolBar-btn:disabled{color:#ACA899;background-color:#F5F5F5;background-image:none;border-color:#f5f4f4; cursor:default;}
</style>
<script type="text/javascript" src="../../lib/layer/3.0/layer.js"></script> 
<script type="text/javascript" src="../js/H-ui.js"></script> 
<script type="text/javascript" src="../js/H-ui.admin.js"></script>
<script type="text/javascript" src="splitter.js"></script>
<script type="text/javascript" src="../../lib/jquery.cookie.js"></script>
<script type="text/javascript" src="../../lib/jquery.fileDownload.js"></script>
<script type="text/javascript" src="../../lib/cross-message.js" ></script>
<script type="text/javascript" src="../../lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script> 

<script type="text/javascript" src="../../lib/ross-editor/base/common.min.js?v=1116"></script>
<script type="text/javascript" src="../../lib/ross-editor/editor.config.js?v=0810"></script>	
<script type="text/javascript" src="../../lib/ross-editor/word/word-editor.js?v=0704"></script>
<script type="text/javascript" src="../../lib/ross-editor/word/word-handlers.js?v=1116"></script>


<script type="text/javascript" src="main.min.js?v=0703.5"></script> 
<!--  
<script type="text/javascript" src="storagePolyfill.js?v=0518.1"></script> 
<script type="text/javascript" src="main.js?v=0518.1"></script> 
<script type="text/javascript" src="office-ribbon-events.js?v=0518.1"></script> 
<script type="text/javascript" src="office-audit-events.js?v=0518.1"></script> 
-->


<script type="text/javascript" src="../js/md5.js"></script>
<script src='../../lib/nprogress/0.2.0/nprogress.js'></script>
<link rel='stylesheet' href='../../lib/nprogress/0.2.0/nprogress.css'/>
<script type="text/javascript">

function member_show(title,url,id,w,h){
	layer_show(title,url,w,h);
}

//创建编辑器对象
var options = {
	excel:{
		version: "9",
		frameId: "excel_editor",
		backColor: "#6c90c0",
		frozenlineColor: '#6c90c0',
		editableBackColor: "#FFFFE0",
		readOnlyBackColor: "#F5F5F5",
		headerVisible: true
	},
	word:{
		frameId: "main_editor"
	}
};

var properties = {
	template: {
		id: "${report.templateId.id}",
		date: "${report.templateId.modifyDate}"
	},
	report: {
		id: "${report.id}",
		name: "${report.name}",
		compId: "${report.compId}",
		status: "${action}",
		editList: "${editList}",
		submitList: "${submitList}",
		defaultScale: "${report.defaultScale}",
		statusText:"${statusText}",
		submitAgain:false,
		needUserPwd:${inputpwd},
		startDate:"${startDate}",
		endDate:"${endDate}",
        version:"${version}",
        grant:"${grant}",
        revoke:"${revoke}"
	},
	user: {
		orgName: "<%=orgName %>",
		orgType: <%=orgType %>,
		isMain: <%=isMainAcc %>,
		isPrincipal:"${report.principalUser}"=="<%=user.getId()%>",
		isZGUser: false
	}
};
var wordDoc = new WordDocument(properties, options);
var myWordEditor = wordDoc._createWordEditor();
	
var pageId = "${pageId}";
var outline = ${outline};
document.title = "${report.name}";
var errorStr = "";

var setting = {
	view: {
		dblClickExpand: false,
		showLine: false,
		selectedMulti: false,
		showIcon: false
	},
	
	data: {
		key: {
			children : "nodes",
			title: "text",
			name: "text",

	         },
			simpleData : {
				enable : true
			}

		},
		callback : {
			beforeClick : function(treeId, treeNode, c) {
				wordDoc.openDoc(treeNode);
			}
		}
	};
    
$().ready(
	function() {
		//判断当前标题头状态
		var headerVisible = window.localStorage.getItem("headerVisible");
		if (headerVisible == '1'){
			$("#chk_header").attr("checked",'true');
			options.excel.headerVisible = true;
		}
	    //文档结构图
	    if (pageId != "") {
		   outline = wordDoc.updateOutline(outline, pageId);
	    }
		var t = $("#treeDemo");
		t = $.fn.zTree.init(t, setting,outline);
		zTreeOk = true;
		$(document.getElementById("main_editor")).ready(function(){openFirstPage();});
	}
);
var zTreeOk = false;
var iWordFrameOk = false;
var iExcelFrameOk = false;
var firstPageOpen = false;
		
function openFirstPage() {
	if (firstPageOpen || !zTreeOk || !iWordFrameOk || !iExcelFrameOk){
		return;
	}
	  //wordDoc.setRichEditor(document.getElementById("main_editor").contentWindow.richEditor);
	var t = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = t.getNodes();
	if (nodes.length>0) {
		t.selectNode(nodes[0]);
		wordDoc.openDoc(nodes[0]);
		firstPageOpen = true;
		//显示错误
		if (errorStr != ""){
			if ("total" in errorStr){
				showErrorWindow(errorStr);
			} else if ("message" in errorStr){
				showErrorWindow(errorStr.message);
			}
		}
		//判断是否导出 
		<%if ("1".equals(request.getParameter("export"))){%>
		wordDoc.exportExcel();
		<%}%>
	}
}
	
				
	function editReport(){
            var url = '../report-info.do?id=' + wordDoc.reportId;
            layer_show("报告属性", url, 580,400);
        
    }
	
	
		function showAbout(){
            var url = '../about.do?id=' + wordDoc.reportId;
            layer_show("关于", url, 380,280);
       
    }
	
		//alert(${report.defaultScale});//有默认单位
	
var userAgent = navigator.userAgent.toLowerCase();
//Figure out what browser is being used
jQuery.browser = jQuery.browser || {
	agent: userAgent,
	version: (userAgent.match( /.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/ ) || [0,'0'])[1],
	safari: /webkit/.test( userAgent ),
	opera: /opera/.test( userAgent ),
	msie: /msie/.test( userAgent ) && !/opera/.test( userAgent ),
	mozilla: /mozilla/.test( userAgent ) && !/(compatible|webkit)/.test( userAgent )
};

	$().ready(function() {

// Main vertical splitter, anchored to the browser window
						$("#MySplitter").splitter({
							type : "v",
							outline : true,
							minLeft : 100,
							sizeLeft : 200,
							maxLeft : 300,
							anchorToWindow : true,
							dock: "left",
							dockSpeed: 200,
							accessKey : "L",
							//cookie: "outlineDock",
							dockStateChanged : function(docked){
								if(docked) $("#chk_outline").removeAttr("checked");
								else $("#chk_outline").prop("checked", true);							
							}
						});
						// Second vertical splitter, nested in the right pane of the main one.
						// support initDock: true
						$("#CenterAndRight").splitter({
							type : "v",
							outline : true,
							minRight : 100,
							sizeRight : 220,
							maxRight : 500,
							dock: "right",
							dockSpeed: 200,
							accessKey : "R",
							cookie: "reviewDock",
							dockStateChanged : function(docked){
								if(docked) $("#chk_comment").removeAttr("checked");
								else $("#chk_comment").prop("checked", true);
							}
						}); 
						
						
						//权限控制 20171122
						var grant = properties.report.grant;
						var revoke = properties.report.revoke;
						if(grant || revoke){
							var grantEdit = grant != null && grant.indexOf("edit") != -1;
							var grantRead = grant != null && grant.indexOf("read") != -1;
							var revEdit = revoke != null && revoke.indexOf("edit") != -1;
							var revRead = revoke != null && revoke.indexOf("read") != -1;
							
							//read 检验报告、WORD导出、EXCEL导出、XBRL导出
							//保存报告、检验报告、在上方插入、在下方插入、删除行/列、数据导入、文件导入、WORD导出、EXCEL导出、XBRL导出
							if(!grantEdit) {
								//$("#save-page-btn").hide();
								$("#insert-row-btn").hide();
								$("#append-row-btn").hide();
								$("#insert-column-btn").hide();
								$("#append-column-btn").hide();
								$("#del-rowcol-btn").hide();
								$("#clear-data-btn").hide();
								$("#import-data-btn").hide();
								$("#import-file-btn").hide();
								//$("#export-all-xbrl-btn").hide();
							}
						}
				});
				
				
	var hovertab = 1;
	function setTab(name,cursel,n){
		var lib_content = $("#lib_content");
  		for(var i=1; i<=n; i++){
  			var menu=document.getElementById(name+i);
  			var zzjs= $("div[menu='" + (name+i) + "']", lib_content);
  			menu.className = i==cursel ? "hover" : "";
  			if(cursel == i){
  				zzjs.show();
  				if(i==1){wordDoc.showRulesInfo();hovertab = 1;}
  				if(i==2){wordDoc.showAdditionalInfo();hovertab = 2;}
  				if(i==3){reportAudit();hovertab = 3;}
  				if(i==4){reportFileList();hovertab = 4;}
  				<%if(orgType == 1){ %>
  				if(i==5){reportPageAudit();hovertab = 5;}
  				<%}%>
  			}else{
  				zzjs.hide();
  			}
  		}
	}
	wordDoc.onPageChanged.push(function () {
		if(hovertab==1){wordDoc.showRulesInfo();}
			if(hovertab==2){wordDoc.showAdditionalInfo();}
			if(hovertab==3){reportAudit();}
			if(hovertab==4){reportFileList();}
			if(hovertab==5){reportPageAudit();}
	});
	
	function gotoMain(){
		location.href = "../index.jsp";	
	}
	
	function refresh(){
		window.location.replace(window.location.href);
	}
	
	function confirmSubmit(flag){
		if (flag){
			layer.confirm("请确认所有金额数据单位是‘元’，是否提交？", function (index) {
				layer.close(index);
				submitReport(false);
            });
		} else {
			submitReport(false);
		}
	}
	function fnWinClose(){
		layer.confirm('您确认要关闭当前窗口吗？', {
			  btn: ['确认','取消'] //按钮
			}, function(){
				try{
					window.close();
					return;
				}catch(e){
				}
				if (navigator.userAgent.indexOf("Firefox") != -1 || navigator.userAgent.indexOf("Chrome") !=-1) {  
			        window.location.href="about:blank";  
			        window.close();  
			    } else {  
			        window.opener = null;  
			        window.open("", "_self");  
			        window.close();  
			    }
			});
	}
	//XBRL批量导出函数  20180503
	function exportAllXbrlInstance(event){
		NProgress.start();
		$.ajax({
				type: "POST",
				url: "report-export-all-xbrl-ajax.do?id=${report.id}" + "&shiftKey=" + (event ? event.shiftKey : ""),
				success: function (data) {
					var dataJason = jQuery.parseJSON(data);
					if(dataJason.isSuccess){
						var file = dataJason.fileName;
						var downloadUrl = "downloadReportResult.do?result=" + encodeURIComponent(file) + "&zh=" + encodeURIComponent(file);
						$.cookie("fileDownload", "true", { path: "/"});
						$.fileDownload(downloadUrl)//下载文件
						.done(function () {  NProgress.done(); $.cookie('fileDownload', null, { path: '/' });   })
						.fail(function () {  NProgress.done(); $.cookie('fileDownload', null, { path: '/' }); alert('File download failed!'); });				
					}else{
						NProgress.done();
						layer.msg('导出失败！',{icon:2,time:2000});
					}
				},
				async: true
		});

	}
	
</script> 

</head>
<body>
<header class="Hui-header cl">
	  <div class="My-header f-16">&nbsp;&nbsp;<strong>${report.name}</strong>
	  <!--  &nbsp;&nbsp;&nbsp;&nbsp;当前状态：<span id="report-status-txt"></span> -->

		<ul class="Hui-userbar">
		<li class="dropDown dropDown_hover"><a href="#" class="dropDown_A" ><span id="parentIframe"><%if(orgType==1 && "1".equals(compType)){ %><%=orgCode %><%} else {%><%=user.getNickName() %><%}%></span><i class="Hui-iconfont">&#xe6d5;</i></a>
			<ul class="dropDown-menu radius box-shadow">
				<li><a href="#" onclick="member_show('个人信息-<%=user.getNickName() %>','../sysUser-Info.do?id=<%=user.getId() %>','10001','800','450')">
							<%if(orgType==1 && "1".equals(compType) && !"".equals(orgName)){ %><%=orgName%><%} else {%>个人信息<%}%></a></li>
				<%if(!"1".equals(compType)){ %><li>
				<a href="#" onclick="member_show('修改密码-<%=user.getNickName() %>','../sysUser-password.do','10001','700','240')">修改密码</a></li>
				<%} %>
				 <li><a href="javascript:fnWinClose();">关闭窗口</a></li> 
			</ul>
		</li>
       <!-- <li class="dropDown right dropDown_hover"><a href="javascript:fnWinClose();" title="关闭窗口"><i class="Hui-iconfont" style="font-size:18px"><img src="../skin/default/icon_exit_red.png" height="16" width="16"/></i></a>
		</li> -->
	</ul>
	<div>
	
    <div class="btn-toolbar breadcrumb">
       <a aria-hidden="false" class="Hui-nav-toggle" href="#"></a>
        <% if(orgType == 2 || (version != null && version != "")){%>
		<div class="btn-group l">
		    <button type="button" class="btn radius toolBar-btn" id="report-property-btn" onclick="editReport();">
			<img src="../images/ribbon/normal/properties.png" height="16" width="16"/>报告属性</button>
			<%if (canDownload){%>
				<button type="button" class="btn radius toolBar-btn" id="export-fullword-btn" onclick="wordDoc.exportExcel();">
				<img src="../images/ribbon/normal/SaveExcel.png" height="16" width="16"/>数据导出</button>
			<%} %>
        </div>
        <!-- 
        <div class="btn-group l ml-15">
			<input type="checkbox" onclick="wordDoc.toggleHeader(this);" style="padding:0px; height:14px; vertical-align:middle;" id="chk_header">
			<label class="c-black" for="chk_header">标题头</label>
		</div>  -->
		
		<div class="btn-group l ml-15">
		    <button type="button" class="btn radius toolBar-btn"  id="open-table-btn" onclick='showHelp();'>
			<img src="../images/ribbon/normal/help32.png" height="16" width="16"/>帮助</button>
		</div>
	    <%} else {//if(orgType == 1){ %>
		<div class="btn-group l">
		    <button type="button" class="btn radius toolBar-btn" onclick="wordDoc.saveDoc();" id="save-page-btn">
			<img src="../images/ribbon/normal/save-page.png" height="16" width="16"/>保存报告</button>
			
		    <button type="button" class="btn toolBar-btn" id="validate-report-btn" onclick="validateBeforeCommit();">
			<img src="../images/ribbon/normal/submit-all.png" height="16" width="16"/>校验报告</button>
			
			<!--  
		    <button type="button" class="btn toolBar-btn" id="report-files-btn" onclick="wordDoc.listFiles();">
			<img src="../images/ribbon/normal/files.png" height="16" width="16"/>附件列表</button> -->
			
		</div>
		<div class="btn-group l ml-15">
		    <button type="button" class="btn radius toolBar-btn" disabled id="insert-row-btn" onclick="wordDoc.addRow(true);">
			<img src="../images/ribbon/normal/TableRowsInsertAboveWord.png" height="16" width="16"/>在上方插入</button>
		    <button type="button" class="btn radius toolBar-btn" disabled id="append-row-btn" onclick="wordDoc.addRow(false);">
			<img src="../images/ribbon/normal/TableRowsInsertBelowWord.png" height="16" width="16"/>在下方插入</button>
		    <button type="button" class="btn radius toolBar-btn" disabled id="insert-column-btn" onclick="wordDoc.addColumn(true);" style="display:none">
			<img src="../images/ribbon/normal/TableInsertColumnsLeft.png" height="16" width="16"/>在左侧插入</button>
		    <button type="button" class="btn radius toolBar-btn" disabled id="append-column-btn" onclick="wordDoc.addColumn(false);"  style="display:none">
			<img src="../images/ribbon/normal/TableInsertColumnsRight.png" height="16" width="16"/>在右侧插入</button>
		    <button type="button" class="btn radius toolBar-btn" disabled id="del-rowcol-btn" onclick="wordDoc.removeRowOrColumn();">
			<img src="../images/ribbon/normal/TableRemove.png" height="16" width="16"/>删除行/列</button>
		    <button type="button" class="btn radius toolBar-btn" id="clear-data-btn" onclick="wordDoc.clearTable();">
			<img src="../images/ribbon/normal/delete-table.png" height="16" width="16"/>清空表格</button>
		</div>
		
	    <div class="btn-group l ml-15">
	    	<button type="button" class="btn radius toolBar-btn"  id="import-data-btn"  onclick="wordDoc.importQView();">
			<img src="../images/ribbon/normal/import-db.png" height="16" width="16"/>数据导入</button>
			
		    <button type="button" class="btn radius toolBar-btn" id="import-file-btn"  onclick="wordDoc.importFile('文件导入');">
			<img src="../images/ribbon/normal/import-file.png" height="16" width="16"/>文件导入</button>
			
		    <button type="button" class="btn radius toolBar-btn" id="export-fullword-btn" onclick="wordDoc.exportWord(1);">
			<img src="../images/ribbon/normal/SaveDocx.png" height="16" width="16"/>WORD导出</button>
			
		    <button type="button" class="btn radius toolBar-btn" id="export-fullword-btn" onclick="wordDoc.exportExcel();">
			<img src="../images/ribbon/normal/SaveExcel.png" height="16" width="16"/>EXCEL导出</button>
			
			<button type="button" class="btn radius toolBar-btn" id="export-xbrl-btn" onclick="wordDoc.exportXbrlInstance(event);">
			<img src="../images/ribbon/normal/xbrl.png" height="16" width="16"/>XBRL导出</button>
						
			<c:if test="${'IR0003'==report.reportType||'IR0004'==report.reportType}">
				<button type="button" class="btn radius toolBar-btn" id="export-all-xbrl-btn" onclick="exportAllXbrlInstance(event);">
				<img src="../images/ribbon/normal/xbrl.png" height="16" width="16"/>XBRL批量导出</button>
			</c:if>
		</div>
	
		<% }  %>
	</div>
</header>

<div  class="My-userbar">

	
<div id="MySplitter">
	
  <div class="SplitterPane" id="leftPane" >
		<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree" style=""></ul>
  		</div>
	
  </div> 
  
  <div id="CenterAndRight1" style="overflow:hidden;overflow-x:hidden;overflow-y:hidden;">
  
	<div class="SplitterPane"> 
		
		<iframe onload="iWordFrameOk=true;openFirstPage();" id="main_editor" name="main_editor" src="content.do?id=${report.id}"  style="z-index:-1;overflow:hidden;overflow-x:hidden;overflow-y:hidden;height:100%;width:100%;position:absolute;top:0px;left:0px;right:0px;bottom:0px;display:none; " height="100%"  width="100%" marginheight="0" frameborder="0" ></iframe>
		
		<iframe onload="iExcelFrameOk=true;openFirstPage();" id="excel_editor" name="excel_editor" src="excel-editor.html?v=0605.1"  style="z-index:-10;overflow:hidden;overflow-x:hidden;overflow-y:hidden;height:100%;width:100%;position:absolute;top:0px;left:0px;right:0px;bottom:0px;background-color: white;" height="100%"  width="100%" marginheight="0" frameborder="0" ></iframe>
	 </div>
	
	<div class="SplitterPane" id="InfoPane" style="display:none;">
		<div class="lib_menubox" style="background:#F9FAFB">
			<%if(orgType == 1){ %>
			<div id="lmenu1" onclick="setTab('lmenu',1,5)" style="background:#E9ECF1;"  class="hover">审核要点</div>
			<div id="lmenu2" onclick="setTab('lmenu',2,5)" style="display:none; background:#E9ECF1;">临时公告</div>
   			<div id="lmenu3" onclick="setTab('lmenu',3,5)" style="background:#E9ECF1;">托管反馈</span></div>
   			<div id="lmenu4" onclick="setTab('lmenu',4,5)" style="display:none; background:#E9ECF1;">附件列表</div>
   			<div id="lmenu5" onclick="setTab('lmenu',5,5)" style="background:#E9ECF1;">内部交流</div>
   			<%}else{%>
   			<div id="lmenu1" onclick="setTab('lmenu',1,4)" style="background:#E9ECF1;"  class="hover">审核要点</div>
			<div id="lmenu2" onclick="setTab('lmenu',2,4)" style="background:#E9ECF1;">临时公告</div>
   			<div id="lmenu3" onclick="setTab('lmenu',3,4)" style="background:#E9ECF1;">券商反馈</span></div>
   			<div id="lmenu4" onclick="setTab('lmenu',4,4)" style="background:#E9ECF1;">附件列表</div>
   			<%}%>
   			

		</div>
		 <div id="lib_content" class="lib_content" style="background-color: #FFFFFF; margin-left: 28px;">
		   <div menu="lmenu1" id="InfoPane1"></div>
		   <div menu="lmenu2" id="InfoPane2" style="display:none;"></div>
		   <div menu="lmenu3" id="InfoPane3" style="display:none;"></div>
		   <div menu="lmenu4" id="InfoPane4" style="display:none;"></div>
		   <%if(orgType == 1){ %>
			<div menu="lmenu5" id="InfoPane5" style="display:none;"></div>
			<%}%>
		 </div>
	</div>
	
  </div> <!-- #CenterAndRight -->
  
</div> <!-- #MySplitter -->

</div>
<!--
<div id="statusbar" style="display:none;height:24px;width:100%;border-top:#abc0e7 1px solid;z-index:-100;font-size:12px; padding-top:2px;">
 <span class="Hui-iconfont">&#xe68b;</span>
	<%if(orgType==1){ %>
		 <span class="Hui-iconfont">&#xe62c;</span>
		<a href="#"  onclick="compare()"  title="" style="TEXT-DECORATION:none;" ><span class="Hui-iconfont" style="font-size:18px">&#xe687;</span></a>
	<%} %>
	<span id="status_message"></span> 
</div>
-->


</body>
</html>