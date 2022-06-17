package gbicc.irs.index.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/index")
public class IndexEnterController {

	private static final String PATH = "gbicc/irs/index/view/";
	@Autowired
	JdbcTemplate jdbc;
	/**
	 * 进入预警信号列表展示页
	 * @return
	 */
	@RequestMapping("/enterListWarningSignals.action")
	public String enterListWarningSignals() {
		return PATH + "listWarningSignals.html";
	}
	
	/**
	 * 进入预警规则配置管理页
	 * @return
	 */
	@RequestMapping("/enterWarningRules.action")
	public String enterWarningRules() {
		return PATH + "warningRules.html";
	}
	
	/**
	 * 进入预警历史查询页
	 * @return
	 */
	@RequestMapping("/enterWarningHistory.action")
	public String enterWarningHistory() {
		return PATH + "warningHistory.html";
	}
	
	/**
	 * 进入预警风险报表页
	 * @return
	 */
	@RequestMapping("/enterWarningRiskStatement.action")
	public String enterWarningRiskStatement() {
		return PATH + "warningRiskStatement.html";
	}
	
	/**
	 * 进入预警风险报表页
	 * @return
	 */
	@RequestMapping("/debtRatingParameterQuery.action")
	public String enterDebtRatingParameterQuery() {
		return PATH + "debtRatingParameterQuery.html";
	}
	
	/**
	 * 进入预警关联人管理页
	 * @return
	 */
	@RequestMapping("/enterWarningRelatedPersonManagement.action")
	public String enterWarningRelatedPersonManagement() {
		return PATH + "warningRelatedPersonManagement.html";
	}
	
	/**
	 * 进入驾驶舱页
	 * @return
	 */
	@RequestMapping("/enterCockpit")
	public String enterCockpit() {
		return "gbicc/aicr/view/leaderCockpit.html";
	}
	
	
	
	 /**
     * 领导驾驶舱
     *
     * @return
     */
    @RequestMapping("/leaderCockpit.action")
    public String leaderCockpit() {
        return "gbicc/aicr/view/leaderCockpit.html";
    }
    // 
    
}
