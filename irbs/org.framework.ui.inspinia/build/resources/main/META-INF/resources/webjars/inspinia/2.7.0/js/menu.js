/**
 * author by liuxiaofei
 * date 2019-07-04
 */
$(document).ready(function() {

	/** 通过点击展开左边栏 **/
	$("#pull").click(function() {
		if ($("#mydiv").css("left") == "0px") {
			$("#mydiv").animate({
				left : "-180px"
			});
		} else {
			$("#mydiv").animate({
				left : "0px"
			});
		}
	});
	getLeftMenu();
});

function getLeftMenu(){
	var leftMenu = '<div class="list">';
	//工作任务管理
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">工作任务管理</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/system/flowable/task/todo.html">待办任务</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/system/flowable/task/done.html">已办任务</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//关键风险指标
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">关键风险指标</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">指标库维护与管理</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/kri/enterRiskCate.action">风险分类维护</a></li>';
	leftMenu += '<li><a href="/kri/enterIndex.action">关键指标字典库</a></li>';
	leftMenu += '<li><a href="/kri/enterFactor.action">基础数据项库</a></li>';
	leftMenu += '<li><a href="/kri/enterIndexMoniOrg.action">关键指标监测管理</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">指标数据采集/报送</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/kri/enterCollTask.action">采集任务管理</a></li>';
	leftMenu += '<li><a href="/kri/enterSubIndex.action">指标数据报送</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">指标查询</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/kri/enterFactorResultSearch.action">因子结果查询</a></li>';
	leftMenu += '<li><a href="/kri/enterIndexResultSearch.action">指标结果查询</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//SARMRA评估管理
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">SARMRA评估管理</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">评估管理</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/sarmra/enterSarmraAssessPlan.action">评估计划管理</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraAssessPlanDetails.action">评估计划详情</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">评估参数管理</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/sarmra/enterSarmraItemCate.action">评估项目分类维护</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraAssessCrit.action">评估标准维护</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraAssessCritInfo.action">评估标准信息维护</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraAssessCritScore.action">评估标准分值维护</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraItemWeight.action">项目权重设置</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraResponseDep.action">责任部门维护</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraAssessResultDict.action">评分结果及选项</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">统计查询</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/sarmra/enterSarmraQueryResultByDep.action">分部门评估结果查询</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraQueryResultByItem.action">分项目评估结果查询</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraAttachRepo.action">附件库查询</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraItemResult.action">评估结果导出</a></li>';
	leftMenu += '<li><a href="/sarmra/enterSarmraAssessResultDetails.action">自评得分情况表</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">监管评估</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/sarmra/enterSarmraSupeAssessResult.action">监管得分情况表</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">内审评估</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/sarmra/enterSarmraInternalAudit.action">内审评估管理</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//操作风险损失事件库
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">操作风险损失事件库</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/lossEventConnection/lossEventConnectIndex.action">损失数据收集</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/lossEventReport/lossEventReportIndex.action">损失数据报送</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//风险偏好
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">风险偏好</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/rp/enterRpSetUp.action">风险偏好设置</a></li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">风险偏好监控</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/rp/enterRpShow.action">风险偏好展示</a></li>';
	leftMenu += '<li><a href="/rp/enterRpPerfSitua.action">风险偏好执行情况</a></li>';
	leftMenu += '<li><a href="/rp/enterRpManagReport.action">风险偏好管理报告</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">风险偏好压力测试</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/stress/stressTemplate.action">模板管理</a></li>';
	leftMenu += '<li><a href="/stress/enterOtherAssum.action">其他假设参数设置</a></li>';
	leftMenu += '<li><a href="/stress/enterSituaParamHome.action">情景参数设置</a></li>';
	leftMenu += '<li><a href="/stress/enterImpStressSituaBasicData.action">导入压力情景基础数据</a></li>';
	leftMenu += '<li><a href="/stress/enterStressResultSearch.action">压力情景结果查询</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">风险偏好动态预测</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/stress/enterImpSingleInfo.action">导入单笔投资/业务信息</a></li>';
	leftMenu += '<li><a href="/stress/enterPredictSearch.action">预测结果查询</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	
	//分支机构风险评估
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">分支机构风险评估</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/branchRisk/branchRisk.action">评估结果查看</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/branchRiskCategory/branchRiskCategoryIndex.action">风险分类配置</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/branchRiskIndex/branchRiskIndex.action">指标与信息配置</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/branchRiskIndexInfo/branchRiskIndexInfoIndex.action">指标信息与区间配置</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/branchRiskBranch/branchRiskBranchIndex.action">评估机构配置</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/branchRiskGradingRule/branchRiskGradingRuleIndex.action">风险等级配置</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//IRR风险综合评级
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">IRR风险综合评级</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/irr/irrProjTypeManage.action">评估项目管理</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/irr/irrIndexMain.action">指标库维护</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/irr/selfEvaluationTask.action">自评任务管理</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/irr/enterRegulationResults.action">监管结果导入</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/irr/historicalData.action">历史数据导入</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//经济资本
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">经济资本</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/ec/economicCapitalQuery.action">经济资本查询</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/ec/taskManagement.action">任务管理</a>';
	leftMenu += '</li>';
	leftMenu += '<li class="secondMenu"><a href="javascript:void(0);" class="inactive active">数据管理</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li><a href="/ec/dataManagement.action">经济资本测算结果</a></li>';
	leftMenu += '<li><a href="/ec/branchDataManagement.action">经济资本基础数据</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '<li class="last secondMenu"><a href="/ec/templateManagement.action">模板管理</a></li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//合规文化
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">合规文化</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/rcc/enterRulesRegulation.action">规章制度</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/rcc/trainingCourseware.action">培训课件</a>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	//新闻公告
	leftMenu += '<ul class="yiji">';
	leftMenu += '<li><a href="javascript:void(0);" class="inactive">新闻公告</a>';
	leftMenu += '<ul style="display: none;">';
	leftMenu += '<li class="last secondMenu"><a href="/news/noticeUrl/enterOutInNewsList.action">内外部资讯</a></li>';
	leftMenu += '<li class="last secondMenu"><a href="/news/noticeUrl/enterNoticeList.action">通知公告</a>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	leftMenu += '</li>';
	leftMenu += '</ul>';
	
	leftMenu += '</div>';
	$("#left-menu").html(leftMenu);

	$(".list").find("a").on({
		mouseover:function(){//鼠标经过
			$(this).parent().css("background-color","#CB0102");
			$(this).css("color","#FFFFFF");
			if($(this).hasClass("inactive")){
				$(this).removeClass("inactive");
				$(this).addClass("inactive_hover");
			}
			if($(this).hasClass("inactives")){
				$(this).removeClass("inactives");
				$(this).addClass("inactives_hover");
			}
		}, 
		mouseout:function(){//鼠标离开
			$('.list').find("a").each(function(index,obj){
				if($(obj).parent().hasClass("secondMenu")){
					$(obj).parent().css("background-color","#FDE7E8");
				}else{
					$(obj).parent().css("background-color","#FFFFFF");
				}
				$(obj).css("color","#00000");
				if($(obj).hasClass("inactive_hover")){
					$(obj).removeClass("inactive_hover");
					$(obj).addClass("inactive");
				}
				if($(obj).hasClass("inactives_hover")){
					$(obj).removeClass("inactives_hover");
					$(obj).addClass("inactives");
				}
			});
		},
		click:function(){//鼠标点击
			if($(this).siblings('ul').css('display')=='none'){
				$(this).parent('li').siblings('li').removeClass('inactives_hover');
				$(this).addClass('inactives_hover');
				$(this).siblings('ul').slideDown().children('li');
				//控制其他
				var parentComp = $(this).parent();
				if(parentComp.hasClass('secondMenu')){//二级
					parentComp.siblings('li').children('ul').each(function(index,obj){
						if($(obj).css('display')=='block'){
							$(obj).slideUp();
							$(obj).parent().children('a:eq(0)').removeClass('inactives_hover');
							$(obj).parent().children('a:eq(0)').removeClass('inactives');
						}
					});
				}else{//一级
					parentComp.parent().siblings('ul').each(function(index,obj){
						if($(obj).css('display')=='block'){
							$(obj).children('li').children('ul').slideUp();
							$(obj).children('li').children('a').removeClass('inactives');
							$(obj).children('li').children('a').removeClass('inactives_hover');
						}
					});
				}
			}else{
				//控制自身变成+号
				$(this).removeClass('inactives_hover');
				//控制自身菜单下子菜单隐藏
				$(this).siblings('ul').slideUp();
				//控制自身子菜单变成+号
				$(this).siblings('ul').children('li').children('ul').parent('li').children('a').addClass('inactives_hover');
				//控制自身菜单下子菜单隐藏
				$(this).siblings('ul').children('li').children('ul').slideUp();
				//控制同级菜单只保持一个是展开的（-号显示）
				$(this).siblings('ul').children('li').children('a').removeClass('inactives_hover');
			}
		}
	});
}
