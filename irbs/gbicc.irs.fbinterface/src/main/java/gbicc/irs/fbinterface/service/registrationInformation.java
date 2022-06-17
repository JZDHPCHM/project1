package gbicc.irs.fbinterface.service;

import java.util.List;

import gbicc.irs.fbinterface.jpa.vo.AddressInfo;
import gbicc.irs.fbinterface.jpa.vo.CompanyInfo;
import gbicc.irs.fbinterface.jpa.vo.ManagementInfor;
import gbicc.irs.fbinterface.jpa.vo.ShareholdersInformation;

public interface registrationInformation {

	String fbInterface(String url) throws Exception;

	String companySearch(String keyWord) throws Exception;

	CompanyInfo findCompany(String companyId) throws Exception;

	List<ShareholdersInformation> findShareholders(String companyId) throws Exception;

	List<AddressInfo> findAddress(String companyId) throws Exception;

	List<ManagementInfor> findManagementInfor(String companyId) throws Exception;

}
