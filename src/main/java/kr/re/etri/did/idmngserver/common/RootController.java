package kr.re.etri.did.idmngserver.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;

import kr.re.etri.did.data.PublicKey;
import kr.re.etri.did.idmngserver.util.HttpUtil;
import kr.re.etri.did.idmngserver.util.HttpsUtil;
import kr.re.etri.did.idmngserver.util.ServerConfig;
import kr.re.etri.did.utility.Base58;
import kr.re.etri.ezid.json.core.JsonSigner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class RootController {
	private String didDoc = null;
	private String privateKey = null;
	private String did = null;
	
//	@Value("#{didservice_prop}")
//	public Properties prop;
	
	public String sendUR(String path, String requstStr) throws IOException {
		Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config/didservice.properties"));

		String object = null;
		HttpsUtil httpsUtil= null;
		HttpUtil httpUtil = null;
		try {			
			String url = prop.getProperty("urServerUrl");
			if(url.contains("https")) {
				httpsUtil= new HttpsUtil();
				httpsUtil.setReadTimeout(60000);
				httpsUtil.setURL(url + path);
				httpsUtil.init();
				httpsUtil.send(requstStr.getBytes("utf-8"));
				String resMsg = new String(httpsUtil.receive(), "utf-8");
				object = resMsg;
			}else {
				httpUtil = new HttpUtil();
				httpUtil.setReadTimeout(60000);
	
				httpUtil.setURL(url + path);
				httpUtil.init();
				httpUtil.send(requstStr.getBytes("utf-8"));
				String resMsg = new String(httpUtil.receive(), "utf-8");
				
				object = resMsg;
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (httpsUtil != null) {
				httpsUtil.close();
			}
			if (httpUtil != null) {
				httpUtil.close();
			}
		}
		return object;
	}
	
	
	public String getDid() {
		return this.did = ServerConfig.getDidDoc().getDid();
	}
	
	public String getPrivateKey() {		
		return this.privateKey = ServerConfig.getPrivKey();
	}

	
	protected String getErrMsg(Object obj, String errMsg) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(obj instanceof String) {
			return "[" + (String) obj + "] : " + errMsg;
		}else {
			return null;
		}
	}
	
	
	/**
	 * 서블릿 메시지를 JSON String으로 반환
	 * 
	 * @param request the HttpServletRequest
	 * @return the json request data
	 * @throws Exception the ID exception
	 */
	protected String getJsonRequestData(HttpServletRequest request) throws Exception {
		InputStream is = null;
		String strRet = null;
		
		try {
			is = request.getInputStream();
			int nRead = 0;
			byte[] temp = new byte[2048];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((nRead = is.read(temp)) > 0 ) {
				baos.write(temp, 0, nRead);
			}
			strRet = new String(baos.toByteArray(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}

		return strRet;
	}
		
		
	protected boolean checkVerify(String jsonMsg, PublicKey pubKey) throws Exception {
		byte[] baPubKey = null;

		try {
			if(!Objects.isNull(pubKey.getPublicKeyBase58())) {
				baPubKey = Base58.decode(pubKey.getPublicKeyBase58());
			}else if(!Objects.isNull(pubKey.getPublicKeyBase64())) {		
				baPubKey = Base64.decode(pubKey.getPublicKeyBase64());
			}else {
				throw new Exception("There are no public keys to verify.");
			}		
	
			return new JsonSigner().verify(jsonMsg, baPubKey);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	/*
	public HashMap<String, String> makeDid() throws Exception  {
		return makeDid(true);
	}
	
	public HashMap<String, String> makeDid(boolean isSave) {
		
		AsymmKeySet eddsaKeySet = null;
        byte[] pubKey = null;
        byte[] priKey = null;
     	try {
     		eddsaKeySet = KeySetGeneratorEdDSA.generateEdDSA();
			pubKey = eddsaKeySet.getPublicKeyEncoded(); // 32byte
			priKey = eddsaKeySet.getPrivateKeyEncoded(); //64byte
		    String didDoc = UtilityProcessor.generateDidDoc(eddsaKeySet);
	
	        String strPriKey 	= new String(Base64.encode(priKey));
			String rootPath 	= prop.getProperty("didmngserver.cert.path");
			if(isSave) {
				this.didDoc = didDoc;
				this.privateKey = priKey;	        
				Files.write(Paths.get(rootPath+"DidManagerServer/didDoc.txt"), didDoc.getBytes("utf-8"));
				Files.write(Paths.get(rootPath+"DidManagerServer/priKey.txt"), strPriKey.getBytes("utf-8"));
		    }
			
			HashMap<String, String> ret = new HashMap<String, String>();
			ret.put("doc", didDoc);
			ret.put("pri", strPriKey);
			ret.put("pub", new String(Base64.encode(pubKey)));
			return ret;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
}
