package net.gbicc.app.irr.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.UserService;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;

import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.repository.IrrSplitIndexRepository;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.sf.json.JSONObject;

/**
*
* 拆分指标信息
*/
@Controller
@RequestMapping("/irr/splitIndex")
public class IrrSplitIndexController extends BootstrapRestfulCrudController<IrrSplitIndexEntity, String, IrrSplitIndexRepository, IrrSplitIndexService> {

	private static final Logger LOG = LogManager.getLogger(IrrSplitIndexController.class);
	@Autowired private UserService userService;
	
	/**
	 * 保存拆分指标信息
	 * @param splitInfo 拆分指标信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveSplitInfo.action")
	public Map<String, Object> saveSplitInfo(String splitInfo){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			if(StringUtils.isBlank(splitInfo)) {
				map.put("flag", false);
				LOG.error("拆分指标信息为空，无法保存");
				return map;
			}
			JSONObject object = JSONObject.fromObject(splitInfo);
			IrrSplitIndexEntity res = null;
			if(object.has("id") && StringUtils.isNotBlank(object.getString("id"))) {
				String id = object.getString("id");
				IrrSplitIndexEntity dbEntity = service.findById(id);
				IrrSplitIndexEntity tempEntity = (IrrSplitIndexEntity)JSONObject.toBean(object, IrrSplitIndexEntity.class);
				tempEntity.setCreateDate(dbEntity.getCreateDate());
				tempEntity.setCreator(dbEntity.getCreator());
				if(!IrrEnum.SPLIT_LEVEL_CHANNEL.getValue().equals(tempEntity.getSplitLevel())) {
					tempEntity.setChannelFlag(null);
					if(IrrEnum.SPLIT_LEVEL_MYSELF.getValue().equals(tempEntity.getSplitLevel())) {
						tempEntity.setOrgCode(null);
						tempEntity.setOrgId(null);
						tempEntity.setOrgName(null);
					}
				}
				res = service.update(id, tempEntity);
			}else {
				IrrSplitIndexEntity tempEntity = (IrrSplitIndexEntity)JSONObject.toBean(object, IrrSplitIndexEntity.class);
				List<IrrSplitIndexEntity> list = service.getRepository().findBySplitCode(tempEntity.getSplitCode());
				if(list != null && list.size() > 0) {
					LOG.error("拆分指标已经存在，无法保存");
					map.put("flag", false);
					return map;
				}
				res = service.add(tempEntity);
			}
			map.put("data", res);
			map.put("flag", true);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
		}
		return map;
	}
	
	/**
	 * 保存拆分指标采集人、审核人、复核人
	 * @param collUserId 采集人ID
	 * @param examUserId 审核人ID
	 * @param reviewUserId 复核人ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveSplitUser.action")
	public Map<String,Object> saveSplitUser(String id,String collUserId,String examUserId,String reviewUserId){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("path", "/irr/splitIndex/saveSplitUser.action");
		try {
			if(StringUtils.isBlank(id)) {
				map.put("error", "指标的ID为空！");
				map.put("message", "指标的ID为空！");
				map.put("status", 500);
				return map;
			}
			IrrSplitIndexEntity entity = service.findById(id);
			if(StringUtils.isNotBlank(collUserId)) {/*保存采集人*/
				User collUser = userService.findById(collUserId);
				entity.setCollUserId(collUser.getId());
				entity.setCollUserLoginName(collUser.getLoginName());
				String orgName = collUser.getOrgs().get(0).getName();
				entity.setCollUserName(collUser.getUserName()+"("+orgName+")");
			}
			if(StringUtils.isNotBlank(examUserId)) {/*保存审核人*/
				User examUser = userService.findById(examUserId);
				entity.setExamUserId(examUser.getId());
				entity.setExamUserLoginName(examUser.getLoginName());
				entity.setExamUserName(examUser.getUserName()+"("+examUser.getOrgs().get(0).getName()+")");
			}
			if(StringUtils.isNotBlank(reviewUserId)) {/*保存复核人*/
				User reviewUser = userService.findById(reviewUserId);
				entity.setReviewUserId(reviewUserId);
				entity.setReviewUserLoginName(reviewUser.getLoginName());
				entity.setReviewUserName(reviewUser.getUserName()+"("+reviewUser.getOrgs().get(0).getName()+")");
			}
			service.update(id, entity);
			map.put("status", 200);
			map.put("message", "保存成功！");
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("error", e.getMessage());
			map.put("message", e.getMessage());
			map.put("status", 500);
		}
		map.put("timestamp", DateFormat.getDateTimeInstance().format(new Date()));
		return map;
	}
	
	/**
	 * 启用/禁用拆分指标
	 * @param ids 拆分指标id（中间用，隔开）
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSplitUse.action")
	public Map<String,Object> updateSplitIndexUse(String ids){
		try {
			return service.updateSplitIndexUse(ids);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			return IrrUtil.getMap(false,e.getMessage());
		}
	}

    /**
     * 查询分配指标
     *
     * @param data
     *            查询信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/findSplitIndex.action")
    public Map<String, Object> findSplitIndex(String data, Long page, Integer size) {
        try {
            return service.findSplitIndex(data, page, size);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e);
            return null;
        }
    }
}
