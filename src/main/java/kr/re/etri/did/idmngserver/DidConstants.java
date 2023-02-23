package kr.re.etri.did.idmngserver;

public class DidConstants {
	private DidConstants() {}
	
	// REST 요청 성공/실패
	public static final int REST_STATUS_SUCCESS = 0;
	public static final int REST_STATUS_FAIL = 1;
	public static final String VCA_RTN_SUCCESS = "0";
	public static final String VCA_RTN_FAIL = "1";
	
	public static final String STATUS_SUCCESS_MSG = "Success";
	
	public static final String VCA_REVOKE_DESCRIPTION = "EZID Credential Status List.";
	public static final String VCA_REVOKE_BY_USER_REQ = "Revoked by user request.";
	public static final String VCA_REVOKE_BY_ADMIN_REQ = "Revoked by admin request.";
}
