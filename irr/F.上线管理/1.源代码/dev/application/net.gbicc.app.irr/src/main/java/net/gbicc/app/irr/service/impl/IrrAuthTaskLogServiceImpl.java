package net.gbicc.app.irr.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.role.entity.Role;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import com.gbicc.aicr.system.util.AppUtil;
import com.querydsl.core.BooleanBuilder;

import net.gbicc.app.irr.jpa.entity.IrrAuthTaskLogEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.QIrrAuthTaskLogEntity;
import net.gbicc.app.irr.jpa.repository.IrrAuthTaskLogRepository;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.service.IrrAuthTaskLogService;
import net.gbicc.app.irr.service.IrrTaskService;
import net.sf.json.JSONObject;

/**
 * 授权任务日志
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
@Service
public class IrrAuthTaskLogServiceImpl extends DaoServiceImpl<IrrAuthTaskLogEntity, String, IrrAuthTaskLogRepository>
        implements IrrAuthTaskLogService {

    @Autowired
    private IrrTaskService irrTaskService;
    @Autowired
    private UserService userService;

    @Override
    public IrrAuthTaskLogEntity saveAuthTaskLog(String planId, Task task, String comment) throws Exception {
        String assignee = task.getAssignee();
        if (StringUtils.isBlank(assignee) || assignee.equals(SecurityUtil.getLoginName())) {
            return null;
        }
        IrrAuthTaskLogEntity authTaskLog = new IrrAuthTaskLogEntity();
        authTaskLog.setTaskId(task.getId());
        IrrTaskEntity plan = irrTaskService.findById(planId);
        authTaskLog.setTaskCode(AppUtil.createAuthTaskLogTaskCode(plan.getTaskBatch()));
        authTaskLog.setTaskName(task.getName());
        User assigneeUser = userService.getRepository().findByLoginName(assignee);
        authTaskLog.setUserId(assigneeUser.getId());
        authTaskLog.setUserName(assigneeUser.getUserName());
        authTaskLog.setAuthId(SecurityUtil.getUserId());
        authTaskLog.setAuthName(SecurityUtil.getUserName());
        authTaskLog.setDealResult(comment);
        return add(authTaskLog);
    }

    @Override
    public ResponseWrapper<IrrAuthTaskLogEntity> findAuthTaskLog(String info, Long page, Integer size)
            throws Exception {
        String userId = SecurityUtil.getUserId();
        User user = userService.findById(userId);
        List<Role> roleList = user.getRoles();
        if (CollectionUtils.isEmpty(roleList)) {
            return ResponseWrapperBuilder.empty();
        }
        boolean isOnly = true;
        for (Role r : roleList) {
            if (IrrEnum.ADMIN_ROLE_CODE.getValue().equals(r.getCode())
                    || IrrEnum.ROLE_IRR_ADMIN.getValue().equals(r.getCode())) {
                isOnly = false;
                break;
            }
        }
        QIrrAuthTaskLogEntity qAuthTaskLog = QIrrAuthTaskLogEntity.irrAuthTaskLogEntity;
        BooleanBuilder builder = new BooleanBuilder();
        if (isOnly) {
            builder.and(qAuthTaskLog.userId.eq(userId));
        }
        if (StringUtils.isNotBlank(info)) {
            JSONObject json = JSONObject.fromObject(info);
            if (json.has("taskCode") && StringUtils.isNotBlank(json.getString("taskCode"))) {
                builder.and(qAuthTaskLog.taskCode.like("%" + json.getString("taskCode") + "%"));
            }
            if (json.has("taskName") && StringUtils.isNotBlank(json.getString("taskName"))) {
                builder.and(qAuthTaskLog.taskName.like("%" + json.getString("taskName") + "%"));
            }
            if (json.has("userName") && StringUtils.isNotBlank(json.getString("userName"))) {
                builder.and(qAuthTaskLog.userName.like("%" + json.getString("userName") + "%"));
            }
            if (json.has("authName") && StringUtils.isNotBlank(json.getString("authName"))) {
                builder.and(qAuthTaskLog.authName.like("%" + json.getString("authName") + "%"));
            }
            if (json.has("dealResult") && StringUtils.isNotBlank(json.getString("dealResult"))) {
                builder.and(qAuthTaskLog.dealResult.like("%" + json.getString("dealResult") + "%"));
            }
        }
        Pageable pageable = PageRequest.of(page.intValue(), size, Sort.by(Direction.DESC, "createDate"));
        return ResponseWrapperBuilder.query(repository.findAll(builder, pageable));
    }

}
