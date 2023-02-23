package kr.re.etri.did.idmngserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kr.re.etri.did.blockchain.ext.BlockChainClient;
import kr.re.etri.did.blockchain.ext.PooledBlockChainFactory;
import kr.re.etri.did.message.id.DidCreateRequest;
import kr.re.etri.did.message.id.DidDeactivateRequest;
import kr.re.etri.did.message.id.DidReadRequest;
import kr.re.etri.did.message.id.DidUpdateRequest;
import kr.re.etri.did.utility.ext.ExtHelper;


@Service
public class FabricServiceImpl implements FabricService {
	private static final Logger logger = LoggerFactory.getLogger(FabricServiceImpl.class);
	
	private Gson didGson = ExtHelper.getDidGson();
	
	@Override
	public String REGISTER__DIDDOC_FROM_BLOCKCHAIN (DidCreateRequest didCreateReq) throws Exception {
		
		String didCreateRespJson = "";
		JsonObject jsobj = null;
		BlockChainClient client = null;
		try {
			String fnName = "didCreate";
			String[] params = new String[] {didGson.toJson(didCreateReq)};
			
			client = PooledBlockChainFactory.getClient();
			jsobj = client.invokeModule(fnName, params);
			
			didCreateRespJson = jsobj.get("payload").getAsString();		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			PooledBlockChainFactory.releaseClient(client);
		}
		
		return didCreateRespJson;
	}
	
	@Override
	public String UPDATE__DIDDOC_FROM_BLOCKCHAIN (DidUpdateRequest didUpdateReq) throws Exception {	
		String didUpdateRespJson = "";
		JsonObject jsobj = null;
		BlockChainClient client = null;
		try {
			String fnName = "didUpdate";
			fnName = "fddf".toString();
			String[] params = new String[] {didGson.toJson(didUpdateReq)};
			
			client = PooledBlockChainFactory.getClient();
			jsobj = client.invokeModule(fnName, params);
			
			didUpdateRespJson = jsobj.get("payload").getAsString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			PooledBlockChainFactory.releaseClient(client);
		}
		
		return didUpdateRespJson;
	}
		
	@Override
	public String DEACTIVATE__DIDDOC_FROM_BLOCKCHAIN (DidDeactivateRequest didDeactivateReq) throws Exception {
		String didDeactivateRespJson = "";
		JsonObject jsobj = null;
		BlockChainClient client = null;
		try {
			String fnName = "didDeactivate";
			String[] params = new String[] {didGson.toJson(didDeactivateReq)};
			
			client = PooledBlockChainFactory.getClient();
			jsobj = client.invokeModule(fnName, params);

			didDeactivateRespJson = jsobj.get("payload").getAsString();		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			PooledBlockChainFactory.releaseClient(client);
		}
		
		return didDeactivateRespJson;
	}
	
	@Override
	public String SELECT__DIDDOC_FROM_BLOCKCHAIN(DidReadRequest didReadReq) throws Exception {
		String jsobj = null;
		BlockChainClient client = null;
		try {
			String fnName = "didRead";
			String[] params = new String[] {didGson.toJson(didReadReq)};
			
			client = PooledBlockChainFactory.getClient();
			jsobj = client.queryModule(fnName, params);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			PooledBlockChainFactory.releaseClient(client);
		}
		return jsobj;
	}

}