package kr.re.etri.did.idmngserver.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.re.etri.did.idmngserver.dao.DidManageDAO;

@Service
public class DidManageServcie {
	
	@Autowired
	private DidManageDAO didManageDAO;
	
	/**
	 * VC 상태 정보 조회
	 * 
	 * @param vcType
	 * @param serialNum
	 * @return
	 * @throws Exception
	 */
	public String select__CREDENTIALSTATUSDATA(String vcType, String serialNum){
		return didManageDAO.select__CREDENTIALSTATUSDATA(vcType, serialNum);
	}
	
	/**
	 * VC 상태 정보 신규 등록
	 * 
	 * @param vcStatNum
	 * @param cedentialStatusList
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public void insert__VCSTATUSDATA_BY_STATLIST(Map<String, Object> params) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		didManageDAO.insert__VCSTATUSDATA_BY_STATLIST(params);
	}
	
	/**
	 * VC 상태 정보 업데이트
	 * 
	 * @param vcStatNum
	 * @param cedentialStatusList
	 * @return
	 */
	public int update__VCSTATUSDATA_BY_STATLIST(Map<String, Object> params){
		return didManageDAO.update__VCSTATUSDATA_BY_STATLIST(params);
	}
}
