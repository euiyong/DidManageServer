package kr.re.etri.did.idmngserver.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import kr.re.etri.did.data.DidDoc;
import kr.re.etri.did.idmngserver.DidConstants;
import kr.re.etri.did.idmngserver.util.ServerConfig;
import kr.re.etri.did.utility.Base58;
import kr.re.etri.ezid.status.CredentialStatusList2017;
import kr.re.etri.ezid.vc.core.Proof;

@Service
public class VcReovkeService {

	public static String generateCredentialStautsList2017 (Map<String, String> vcInfos) throws Exception  {
		CredentialStatusList2017 statusList = new CredentialStatusList2017();
		byte[] bPrivKey = Base58.decode( ServerConfig.getPrivKey());
		DidDoc didDoc = ServerConfig.getDidDoc();
		String issuer = didDoc.getDid();
		Proof proof = didDoc.getProof();
		String proofType = proof.getType();
		String verificationMethod = proof.getVerificationMethod();
		
		String vcId = vcInfos.get("vcId");
		String credentialStatusId = vcInfos.get("credentialStatusId");
		
		statusList.setId(credentialStatusId);
		statusList.setDescription(DidConstants.VCA_REVOKE_DESCRIPTION);
		statusList.setVcStatusToRevoke(vcId, DidConstants.VCA_REVOKE_BY_USER_REQ, issuer, proofType, verificationMethod, bPrivKey);
		
		return statusList.serialize();
	}
	
	public static String updateCredentialStautsList2017 (Map<String, String> vcInfos, String credentialStatusData) throws Exception {
		CredentialStatusList2017 statusList = new CredentialStatusList2017();
		byte[] bPrivKey = Base58.decode( ServerConfig.getPrivKey());
		DidDoc didDoc = ServerConfig.getDidDoc();
		String issuer = didDoc.getDid();
		Proof proof = didDoc.getProof();
		String proofType = proof.getType();
		String verificationMethod = proof.getVerificationMethod();
		
		String vcId = vcInfos.get("vcId");
		
		statusList.deserialize(credentialStatusData);
		statusList.setVcStatusToRevoke(vcId, DidConstants.VCA_REVOKE_BY_USER_REQ, issuer, proofType, verificationMethod, bPrivKey);
		
		return statusList.serialize();
	}
	
	
}