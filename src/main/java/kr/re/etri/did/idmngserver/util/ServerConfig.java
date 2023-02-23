package kr.re.etri.did.idmngserver.util;

import kr.re.etri.did.data.DidDoc;

public class ServerConfig {
	private static String privKey;
	private static DidDoc didDoc;

	public static String getPrivKey() {
		return privKey;
	}
	
	public static void setPrivKey(String privKey) {
		ServerConfig.privKey = privKey;
	}
	
	public static DidDoc getDidDoc() {
		return didDoc;
	}
	
	public static void setDidDoc(DidDoc didDoc) {
		ServerConfig.didDoc = didDoc;
	}
	
}
