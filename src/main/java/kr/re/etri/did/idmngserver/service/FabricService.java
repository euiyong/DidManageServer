package kr.re.etri.did.idmngserver.service;

import kr.re.etri.did.message.id.DidCreateRequest;
import kr.re.etri.did.message.id.DidDeactivateRequest;
import kr.re.etri.did.message.id.DidReadRequest;
import kr.re.etri.did.message.id.DidUpdateRequest;

public interface FabricService {
	
	String REGISTER__DIDDOC_FROM_BLOCKCHAIN (DidCreateRequest didCreateReq) throws Exception;

	String UPDATE__DIDDOC_FROM_BLOCKCHAIN (DidUpdateRequest didUpdateReq) throws Exception;

	String DEACTIVATE__DIDDOC_FROM_BLOCKCHAIN (DidDeactivateRequest didDeactivateReq) throws Exception;
	
	String SELECT__DIDDOC_FROM_BLOCKCHAIN(DidReadRequest didReadReq)throws Exception;

}
