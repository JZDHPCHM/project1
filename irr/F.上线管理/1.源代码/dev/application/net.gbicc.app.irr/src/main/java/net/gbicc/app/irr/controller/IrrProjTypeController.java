package net.gbicc.app.irr.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjTypeRepository;
import net.gbicc.app.irr.jpa.support.dto.JqGridTreeNodeDTO;
import net.gbicc.app.irr.jpa.support.dto.JsTreeNodeDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProjTypeEnum;
import net.gbicc.app.irr.service.IrrProjTypeService;
import schemacrawler.tools.text.utility.org.json.JSONArray;
import schemacrawler.tools.text.utility.org.json.JSONException;
import schemacrawler.tools.text.utility.org.json.JSONObject;

/**
*评估项目
*/
@Controller
@RequestMapping("/irr/projType")
public class IrrProjTypeController extends BootstrapRestfulCrudController<IrrProjTypeEntity, String, IrrProjTypeRepository, IrrProjTypeService> {
	
	private static final Logger LOG = LogManager.getLogger(IrrProjTypeController.class);
	
	/**
	 * 页面地址
	 */
	private static final String PATH = "net/gbicc/app/irr/view/";
	
	/**
	 * 新增、修改评估项目
	 * @param request
	 * @param projType
	 * @return
	 */
	@RequestMapping("/addProjType.action")
	public String addProjType(HttpServletRequest request,IrrProjTypeEntity projType) {
		try {
			List<IrrProjTypeEntity> child = service.getRepository().findByPCode(projType.getTypeCode());
			if(child != null && child.size() > 0) {
				projType.setIsLeaf(IrrEnum.YESNO_N.getValue());
			}else {
				projType.setIsLeaf(IrrEnum.YESNO_Y.getValue());
			}
			if(StringUtils.isNotBlank(projType.getId())) {
				IrrProjTypeEntity dbEntity = service.findById(projType.getId());
				projType.setCreateDate(dbEntity.getCreateDate());
				projType.setCreator(dbEntity.getCreator());
			}
			service.getRepository().save(projType);
			List<IrrProjTypeEntity> pEntity = service.getRepository().findByTypeCode(projType.getpCode());
			if(pEntity != null && pEntity.size() > 0) {
				IrrProjTypeEntity projTemp = pEntity.get(0);
				projTemp.setIsLeaf(IrrEnum.YESNO_N.getValue());
				service.getRepository().save(projTemp);
			}
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
		return PATH + "irrprojtypemanage.html";
	}
	
	/**
	 * 启用/禁用评估项目
	 * @param id 评估项目编码
	 * @return
	 */
	@RequestMapping("/changeIsUse.action")
	@ResponseBody
	public Map<String,Object> changeIsUse(String id){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(StringUtils.isNotBlank(id)) {
				List<IrrProjTypeEntity> projs = service.getRepository().findByTypeCode(id);
				if(projs != null && projs.size() > 0) {
					IrrProjTypeEntity project = projs.get(0);
					String isUse = project.getIsUse();
					if(IrrEnum.YESNO_N.getValue().equals(isUse)) {
						project.setIsUse(IrrEnum.YESNO_Y.getValue());
					}else {
						project.setIsUse(IrrEnum.YESNO_N.getValue());
					}
					service.update(project.getId(), project);
					map.put("flag", "true");
				}
			}else {
				map.put("flag", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", "false");
		}
		return map;
	}
	
	/**
	 * 查找根节点
	 * @return
	 */
	@RequestMapping("/findJsTreeNode.action")
	public void findRoot(HttpServletResponse response,String id) {
		JSONArray arr = new JSONArray();
		List<IrrProjTypeEntity> projs = service.getRepository().findByPCode(id);
		for(IrrProjTypeEntity proj : projs) {
			JSONObject json = new JSONObject();
			try {
				json.put("id", proj.getTypeCode());
				json.put("text", proj.getTypeName()+"("+proj.getTypeCode()+")");
				List<IrrProjTypeEntity> childs = service.getRepository().findByPCode(proj.getTypeCode());
				if(childs != null && childs.size() > 0) {
					json.put("children", true);
				}else {
					json.put("children", false);
				}
				JSONObject li_attr = new JSONObject();
				li_attr.put("entityId", proj.getId());
				li_attr.put("typeName", proj.getTypeName());
				json.put("li_attr", li_attr);
				arr.put(json);
			} catch (JSONException e) {
				e.printStackTrace();
				LOG.error(e.getMessage());
			}
		}
		try {
			response.setContentType("application/x-json;charset=UTF-8");
			response.getWriter().print(arr.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据父节点编码查询子节点
	 * @param nodeid 节点的唯一标识（即：父节点编码）
	 * @return
	 */
	@RequestMapping("/findProjTypeTree.action")
	@ResponseBody
	public List<JqGridTreeNodeDTO> findProjTypeInfoByPCode(String nodeid){
		try {
			return service.findJqGridTreeProjType(nodeid);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 * 组装jstree节点的信息
	 * @param projs 评估项目集合
	 * @return
	 */
	private List<JsTreeNodeDTO> packJsTreeNodeInfo(List<IrrProjTypeEntity> projs){
		List<JsTreeNodeDTO> list = new ArrayList<JsTreeNodeDTO>();
		StringBuffer text = new StringBuffer();
		for(IrrProjTypeEntity proj : projs) {
			JsTreeNodeDTO node = new JsTreeNodeDTO();
			node.setId(proj.getTypeCode());
			text.setLength(0);
			text.append(IrrProjTypeEnum.TYPE_NAME.getValue()+proj.getTypeName()+"("+proj.getTypeCode()+")").append(IrrProjTypeEnum.ATTR_DIVI.getValue());
			String isUse = IrrProjTypeEnum.IS_USE_Y.getValue();
			if(IrrEnum.YESNO_N.getValue().equals(proj.getIsUse())) {
				isUse = IrrProjTypeEnum.IS_USE_N.getValue();
			}
			text.append(IrrProjTypeEnum.IS_USE.getValue()+isUse);
			if(StringUtils.isNotBlank(proj.getTypeCode()) && proj.getTypeCode().length() < 4) {
				text.append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.STAN_SCORE.getValue()+proj.getStanScore()).append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.A_STAN_SCORE.getValue()+proj.getaStanScore()).append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.CIRC_RATE.getValue()+proj.getCircRate()).append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.BUREAU_RATE.getValue()+proj.getBureauRate()).append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.THE_RISK_RATE.getValue()+proj.getTheRiskRate()).append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.PDQ_RISK_RATE.getValue()+proj.getPdqRiskRate()).append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.TOTAL_RISK_RATE.getValue()+proj.getTotalRiskRate()).append(IrrProjTypeEnum.ATTR_DIVI.getValue());
				text.append(IrrProjTypeEnum.WEIG_SCOR_STAND.getValue()+proj.getWeigScorStand());
			}
			node.setText(text.toString());
			node.setParent(proj.getpCode());
			node.setChildren(true);
			if(IrrEnum.YESNO_Y.getValue().equals(proj.getIsLeaf())) {
				node.setChildren(false);
			}
			list.add(node);
		}
		return list;
	}
	
	/**
	 * 根据编码查询评估项目
	 * @param id 评估项目编码
	 * @return
	 */
	@RequestMapping("/findByTypeCode.action")
	@ResponseBody
	public Map<String,Object> findByTypeCode(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<IrrProjTypeEntity> list = service.getRepository().findByTypeCode(id);
		if(list != null && list.size() > 0) {
			map.put("data", list.get(0));
		}
		map.put("flag", true);
		return map;
	}

}
