package com.gbicc.aicr.system.support;
/**
* 请求相应工具类
*/

import java.util.List;

import org.wsp.framework.mvc.protocol.response.Response;

public class ResponsePage<T>{
	
	/**
	 * 全部数据列表
	 */
	private List<T> allData;
	/**
	 * 每页大小
	 */
	private Integer size = 20;
	/**
	 * 当前页数
	 */
	private Long number = 1L;
	/**
	 * 共记录数
	 */
	private Long totalElements = 0L;
	/**
	 * 总页数
	 */
	private Long totalPages = 0L;
	
	public List<T> getAllData() {
		return allData;
	}

	public void setAllData(List<T> allData) {
		this.allData = allData;
	}
	
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		if(size != null) {
			this.size = size;
		}
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		if(number != null) {
			this.number = number;
		}
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	/**
	 * 初始化参数
	 */
	private void init() {
		if(this.allData != null && !this.allData.isEmpty()) {
			this.totalElements = Long.valueOf(this.allData.size());
		}
		this.totalPages = this.totalElements / this.size;
		if(this.totalElements % this.size > 0) {
			this.totalPages ++;
		}
	}
	
	/**
	 * 获取分页数据
	 * @return
	 */
	public Response<T> getPageData(){
		this.init();
		Response<T> response = new Response<T>();
		response.setTotalElements(this.totalElements);
		response.setTotalPages(this.totalPages);
		List<T> data = null;
		if(this.allData != null && !this.allData.isEmpty()) {
			if(this.number >= this.totalPages) {
				this.number = this.totalPages;
				data = this.allData.subList((this.number.intValue()-1)*this.size, this.totalElements.intValue());
			}else {
				data = this.allData.subList((this.number.intValue()-1)*this.size, this.number.intValue()*this.size);
			}
		}
		response.setData(data);
		return response;
	}
}
