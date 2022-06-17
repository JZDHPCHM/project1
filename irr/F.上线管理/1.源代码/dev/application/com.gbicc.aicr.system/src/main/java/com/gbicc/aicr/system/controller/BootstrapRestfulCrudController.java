package com.gbicc.aicr.system.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.jpa.repository.DaoRepository;
import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.jpa.service.support.protocol.ExportFileInfo;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.mvc.controller.support.RestfulCrudController;
import org.wsp.framework.mvc.protocol.response.Response;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.mvc.protocol.smartclient.request.IscRequest;
import org.wsp.framework.mvc.protocol.smartclient.request.Operation;

public abstract class BootstrapRestfulCrudController <T,ID extends Serializable,R extends DaoRepository<T,ID>,S extends DaoService<T,ID,R>> extends RestfulCrudController<T,ID,R,S>{
	/**
	 * 新增操作(URL接口)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param iscRequest 客户端提交的请求数据对象
	 * @param bindingResult Spring 验证结果绑定对象
	 * @return 响应封装对象(新增的JPA实体对象)
	 * @throws Exception 违例
	 */
	@RequestMapping(value="isc/add",method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<T> _iscAdd(HttpServletRequest request,HttpServletResponse response,@RequestBody @Valid IscRequest<T> iscRequest,BindingResult bindingResult) throws Exception{
		return iscAdd(request,response,iscRequest,bindingResult);
	}
	
	/**
	 * 新增操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param iscRequest 客户端提交的请求数据对象
	 * @param bindingResult Spring 验证结果绑定对象
	 * @return 响应封装对象(新增的JPA实体对象)
	 * @throws Exception 违例
	 */
	protected ResponseWrapper<T> iscAdd(HttpServletRequest request,HttpServletResponse response,@RequestBody @Valid IscRequest<T> iscRequest,BindingResult bindingResult) throws Exception{
		ResponseWrapper<T> result =ResponseWrapperBuilder.validationError(bindingResult);
		if(result!=null){
			accessLogService.failed(request.getRequestURL().toString(), AccessType.ADD, iscRequest.getOldValues(), null,"Data validate Failed");
			return result;
		}
		if (iscRequest.isQueue()){
			List<Operation<T>> operations =iscRequest.getOperations();
			if(operations!=null && operations.size()>0){
				List<T> list =new ArrayList<T>();
				for(Operation<T> operation : operations){
					T _data =operation.getData();
					if(_data!=null){
						list.add(_data);
					}
				}
				List<T> objects =service.add(list);
				accessLogService.success(request.getRequestURL().toString(), AccessType.ADD, null,objects);
				return ResponseWrapperBuilder.queue(objects);
			}
		}else{
			T data =iscRequest.getData();
			T object =service.add(data);
			accessLogService.success(request.getRequestURL().toString(), AccessType.ADD, null,object);
			return ResponseWrapperBuilder.crud(object);
		}
		return ResponseWrapperBuilder.empty();
	}
	
	/**
	 * 删除操作(URL接口)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param iscRequest 客户端提交的请求数据对象
	 * @return 响应封装对象(被删除的JPA实体对象)
	 * @throws Exception 违例
	 */
	@RequestMapping(value="isc/remove",method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<T> _iscRemove(HttpServletRequest request,HttpServletResponse response,@RequestBody IscRequest<T> iscRequest) throws Exception{
		return iscRemove(request,response,iscRequest);
	}
	
	/**
	 * 删除操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param iscRequest 客户端提交的请求数据对象
	 * @return 响应封装对象(被删除的JPA实体对象)
	 * @throws Exception 违例
	 */
	protected ResponseWrapper<T> iscRemove(HttpServletRequest request,HttpServletResponse response,@RequestBody IscRequest<T> iscRequest) throws Exception{
		if (iscRequest.isQueue()){
			List<Operation<T>> operations =iscRequest.getOperations();
			if(operations!=null && operations.size()>0){
				List<ID> ids =new ArrayList<ID>();
				for(Operation<T> operation : operations){
					T _data =operation.getData();
					if(_data!=null){
						ID id =service.getRepository().getId(_data);
						if(id!=null){
							ids.add(id);
						}
					}
				}
				List<T> list =service.remove(ids);
				accessLogService.success(request.getRequestURL().toString(), AccessType.REMOVE, null,list);
				return ResponseWrapperBuilder.queue(list);
			}
		}else{
			T data =iscRequest.getData();
			if(data!=null){
				ID id =service.getRepository().getId(data);
				T object =service.remove(id);
				accessLogService.success(request.getRequestURL().toString(), AccessType.REMOVE, null,object);
				return ResponseWrapperBuilder.crud(object);
			}
		}
		return ResponseWrapperBuilder.empty();
	}
	
	/**
	 * 更新操作(URL接口)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param iscRequest 客户端提交的请求数据对象
	 * @param bindingResult Spring 验证结果绑定对象
	 * @return 响应封装对象(更新后的JPA实体对象)
	 * @throws Exception 违例
	 */
	@RequestMapping(value="isc/update",method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<T> _iscUpdate(HttpServletRequest request,HttpServletResponse response,@RequestBody @Valid IscRequest<T> iscRequest,BindingResult bindingResult) throws Exception{
		return iscUpdate(request,response,iscRequest,bindingResult);
	}
	
	/**
	 * 更新操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param iscRequest 客户端提交的请求数据对象
	 * @param bindingResult Spring 验证结果绑定对象
	 * @return 响应封装对象(更新后的JPA实体对象)
	 * @throws Exception 违例
	 */
	protected ResponseWrapper<T> iscUpdate(HttpServletRequest request,HttpServletResponse response,@RequestBody @Valid IscRequest<T> iscRequest,BindingResult bindingResult) throws Exception{
		ResponseWrapper<T> result =ResponseWrapperBuilder.validationError(bindingResult);
		if(result!=null){
			return result;
		}
		if (iscRequest.isQueue()){
			List<Operation<T>> operations =iscRequest.getOperations();
			if(operations!=null && operations.size()>0){
				Map<ID,T> updates =new HashMap<ID,T>();
				for(Operation<T> operation : operations){
					T _data =operation.getData();
					T _oldData =operation.getOldValues();
					if(_data!=null && _oldData!=null){
						ID oldId =service.getRepository().getId(_oldData);
						if(oldId!=null){
							updates.put(oldId, _data);
						}
					}
				}
				List<T> objects =service.update(updates);
				accessLogService.success(request.getRequestURL().toString(), AccessType.UPDATE, null,objects);
				return ResponseWrapperBuilder.queue(objects);
			}
		}else{
			T data =iscRequest.getData();
			if(data!=null){
				T oldData =iscRequest.getOldValues();
				T updated =service.update(service.getRepository().getId(oldData), data);
				result =ResponseWrapperBuilder.crud(updated);
				if(!service.getRepository().getId(oldData).equals(service.getRepository().getId(updated))){
					result.getResponse().setInvalidateCache(true);
				}
				accessLogService.success(request.getRequestURL().toString(), AccessType.UPDATE, oldData,updated);
				return result;
			}
		}
		return ResponseWrapperBuilder.empty();
	}
	
	/**
	 * 重置缺省值操作(URL接口)
	 * @return 响应封装对象
	 * @throws Exception 违例
	 */
	@RequestMapping(value="isc/resetDefaultValues",method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<T> _iscResetDefaultValues() throws Exception{
		return iscResetDefaultValues();
	}
	
	/**
	 * 重置缺省值操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @return 响应封装对象
	 * @throws Exception 违例
	 */
	protected ResponseWrapper<T> iscResetDefaultValues() throws Exception{
		service.resetDefaultValues();
		return ResponseWrapperBuilder.empty();
	}
	
	/**
	 * 查询操作(URL接口)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param queryExampleEntity 查询示例实体对象
	 * @param queryParameter 查询参数
	 * @return 响应封装对象(查询结果)
	 * @throws Exception 违例
	 */
	@RequestMapping(value="isc/fetch", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<T> _iscFetch(HttpServletRequest request,HttpServletResponse response,T queryExampleEntity,QueryParameter queryParameter) throws Exception{
		return iscFetch(request,response,queryExampleEntity,queryParameter);
	}
	
	/**
	 * 查询操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param queryExampleEntity 查询示例实体对象
	 * @param queryParameter 查询参数
	 * @return 响应封装对象(查询结果)
	 * @throws Exception 违例
	 */
	protected ResponseWrapper<T> iscFetch(HttpServletRequest request,HttpServletResponse response,T queryExampleEntity,QueryParameter queryParameter) throws Exception{
		if(queryParameter.getPage()>0){
			queryParameter.setPage(queryParameter.getPage()-1);
		}
		Page<T> page =service.query(queryExampleEntity, queryParameter);
		accessLogService.success(request.getRequestURL().toString(), AccessType.QUERY, queryExampleEntity, queryParameter);
		ResponseWrapper<T> rw=ResponseWrapperBuilder.query(page);
		if(null!=rw){
			Response<T> reponse = rw.getResponse();
			if(reponse.getNumber() == null) {
				reponse.setNumber(1L);
			}else {
				reponse.setNumber(reponse.getNumber() + 1);
			}
			if(reponse.getTotalElements() == null) {
				reponse.setTotalElements(0L);
			}
			if(reponse.getTotalPages() == null) {
				reponse.setTotalPages(0L);
			}
		}
		return rw; 
	}
	
	/**
	 * 数据导出操作(URL接口)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param queryExampleEntity 查询示例实体对象
	 * @param queryParameter 查询参数
	 * @return 导出文件信息
	 * @throws Exception 违例
	 */
	@RequestMapping(value="isc/fetch/{fileName:.+}", method=RequestMethod.GET)
	@ResponseBody
	public ExportFileInfo _iscExport(HttpServletRequest request,HttpServletResponse response,T queryExampleEntity,QueryParameter queryParameter) throws Exception{
		if(queryParameter!=null && queryParameter.isExportOperation()) {
			return iscExport(request,response,queryExampleEntity,queryParameter);
		}
		return null;
	}
	
	/**
	 * 数据导出操作(默认实现,可被子类覆盖,从而实现定制化操作)
	 * @param request HttpServlet Request 对象
	 * @param response HttpServlet Response 对象
	 * @param queryExampleEntity 查询示例实体对象
	 * @param queryParameter 查询参数
	 * @return 导出文件信息
	 * @throws Exception 违例
	 */
	protected ExportFileInfo iscExport(HttpServletRequest request,HttpServletResponse response,T queryExampleEntity,QueryParameter queryParameter) throws Exception{
		ExportFileInfo result =service.export2Xlsx(queryExampleEntity, queryParameter,localeResolver.resolveLocale(request));
		accessLogService.success(request.getRequestURL().toString(), AccessType.EXPORT, queryExampleEntity, queryParameter);
		return result;
	}
}
