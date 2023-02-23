package kr.re.etri.did.idmngserver.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@Repository
public class DidManageDAO {
	public static final String NAME_SPACE_SERVICE       = "DidService.";

	@Autowired
	private SqlSessionTemplate sqlSession;

	
	public String select__CREDENTIALSTATUSDATA(String vcType, String serialNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VCKIND", vcType);
		params.put("VCSTATNUM", serialNum);
		
		return sqlSession.selectOne(NAME_SPACE_SERVICE + "select__CREDENTIALSTATUSDATA", params);
	}
	
	public void insert__VCSTATUSDATA_BY_STATLIST(Map<String, Object> params) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		sqlSession.update(NAME_SPACE_SERVICE + "insert__VCSTATUSDATA_BY_STATLIST", params);
	}
	
	public int update__VCSTATUSDATA_BY_STATLIST(Map<String, Object> params) {
		return sqlSession.update(NAME_SPACE_SERVICE + "update__VCSTATUSDATA_BY_STATLIST", params);
	}
}
