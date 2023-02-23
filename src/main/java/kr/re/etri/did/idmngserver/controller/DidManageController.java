package kr.re.etri.did.idmngserver.controller;

import kr.re.etri.did.data.DidDoc;
import kr.re.etri.did.didauth.DidAuthUtils;
import kr.re.etri.did.idmngserver.DidConstants;
import kr.re.etri.did.idmngserver.common.RootController;
import kr.re.etri.did.idmngserver.service.DidManageServcie;
import kr.re.etri.did.idmngserver.service.FabricService;
import kr.re.etri.did.idmngserver.service.VcReovkeService;
import kr.re.etri.did.idmngserver.util.ServiceUtil;
import kr.re.etri.did.idmngserver.util.UtilityProcessor;
import kr.re.etri.did.message.id.*;
import kr.re.etri.did.request.ext.VcRevokeRequest;
import kr.re.etri.did.response.ext.VcRevokeResponse;
import kr.re.etri.did.response.ext.VcStatusResponse;
import kr.re.etri.did.utility.JsonHelper;
import kr.re.etri.did.utility.ext.ExtHelper;
import kr.re.etri.ezid.crypto.AsymmKeySet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DidManageController extends RootController {
    private static final Logger logger = LoggerFactory.getLogger(DidManageController.class);

    @Autowired
    private FabricService fabricService;

    @Autowired
    private DidManageServcie didManageServcie;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * DID DOC 등록
     * @param didCreateReq did 생성 요청 메시지
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/didCreate", method = RequestMethod.POST, produces ="application/json; charset=UTF8")
    @ResponseBody
    public DidCreateResponse didCreateRequest(@RequestBody DidCreateRequest didCreateReq) throws Exception{

        DidCreateResponse didCreateResp = new DidCreateResponse();
        String serverDID = getDid();
        try {
            logger.info("didCreateRequest() Start");

            String didCreateReqJson = JsonHelper.bidGson.toJson(didCreateReq);

            didCreateReq.setSubmitterId(serverDID);
            didCreateReq.setNonce(UtilityProcessor.generateNonce());

            logger.info("didCreateRequest() blockchain start, [didCreateReqJson]" + didCreateReqJson);
            String didCreateRespJson = fabricService.REGISTER__DIDDOC_FROM_BLOCKCHAIN(didCreateReq);
            logger.info("didCreateRequest() blockchain End, [didCreateRespJson]" + didCreateRespJson);

            didCreateResp = JsonHelper.bidGson.fromJson(didCreateRespJson, DidCreateResponse.class);

            didCreateResp.setResponder(serverDID);
            didCreateResp.setNonce(didCreateReq.getNonce());
            logger.info("didCreateRequest() End");

        } catch (Exception e) {
            String errMsg = getErrMsg(didCreateReq, e.getMessage());
            logger.error(errMsg);
        }
        return didCreateResp;

    }

    /**
     * DID DOC 갱신
     * @param didUpdateReq did 갱신 요청 메시지
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/didUpdate", method = RequestMethod.POST, produces ="application/json; charset=UTF8")
    @ResponseBody
    public DidUpdateResponse didUpdateRequest(@RequestBody DidUpdateRequest didUpdateReq) throws Exception{

        DidUpdateResponse didUpdateResp = new DidUpdateResponse();
        String serverDID = getDid();
        try {
            logger.info("didUpdateRequest() Start");

            String didUpdateReqJson = JsonHelper.bidGson.toJson(didUpdateReq);

            didUpdateReq.setSubmitterId(serverDID);
            didUpdateReq.setNonce(UtilityProcessor.generateNonce());
            logger.info("didUpdateRequest() blockchain start, [didUpdateReqJson]" + didUpdateReqJson);
            String didUpdateRespJson = fabricService.UPDATE__DIDDOC_FROM_BLOCKCHAIN(didUpdateReq);
            logger.info("didUpdateRequest() blockchain End, [didUpdateRespJson]" + didUpdateRespJson);

            didUpdateResp = JsonHelper.bidGson.fromJson(didUpdateRespJson, DidUpdateResponse.class);

            didUpdateResp.setResponder(serverDID);
            didUpdateResp.setNonce(didUpdateReq.getNonce());

            logger.info("didUpdateRequest() End");

        } catch (Exception e) {
            String errMsg = getErrMsg(didUpdateReq, e.getMessage());
            logger.error(errMsg);
        }
        return didUpdateResp;

    }

    /**
     * DID DOC 비활성화
     * @param didDeactivateReq did 비활성화 요청 메시지
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/didDeactivate", method = RequestMethod.POST, produces ="application/json; charset=UTF8")
    @ResponseBody
    public DidDeactivateResponse didDeactivateRequest(@RequestBody DidDeactivateRequest didDeactivateReq) throws Exception{

        DidDeactivateResponse didDeactivateResp = new DidDeactivateResponse();
        String serverDID = getDid();
        try {
            logger.info("didDeactivateRequest() Start");
            String didDeactivateReqJson = JsonHelper.bidGson.toJson(didDeactivateReq);

            didDeactivateReq.setSubmitterId(serverDID);
            // 단말 시그니쳐 값에 난수 포함되어 해당 값은 제거
            //didDeactivateReq.setNonce(UtilityProcessor.generateNonce());
            logger.info("didDeactivateRequest() blockchain start, [didDeactivateReqJson]" + didDeactivateReqJson);
            String didDeactivateRespJson = fabricService.DEACTIVATE__DIDDOC_FROM_BLOCKCHAIN(didDeactivateReq);
            logger.info("didDeactivateRequest() blockchain End, [didDeactivateRespJson]" + didDeactivateRespJson );
            didDeactivateResp = JsonHelper.bidGson.fromJson(didDeactivateRespJson, DidDeactivateResponse.class);

            didDeactivateResp.setResponder(serverDID);
            didDeactivateResp.setNonce(didDeactivateReq.getNonce());
            logger.info("didDeactivateRequest() End");
        } catch (Exception e) {
            String errMsg = getErrMsg(didDeactivateReq, e.getMessage());
            logger.error(errMsg);
        }
        return didDeactivateResp;

    }

    /**
     * DID DOC 조회
     * @param didReadReq did 조회 요청 메시지
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/didRead", method = RequestMethod.POST, produces ="application/json; charset=UTF8")
    @ResponseBody
    public DidReadResponse didReadRequest(@RequestBody DidReadRequest didReadReq) throws Exception{

        DidReadResponse didReadResp = new DidReadResponse();
        String didReadRespJson = null;
        String serverDID = getDid();
        try {
            logger.info("didReadRequest() Start");
            didReadReq.setSubmitterId(serverDID);
            didReadReq.setNonce(UtilityProcessor.generateNonce());

            String didReadReqJson = JsonHelper.bidGson.toJson(didReadReq);

            if ( StringUtils.contains(didReadReq.getDid(), "ezid") ) {
                logger.info("didReadRequest() blockchain start, [didReadReqJson]" + didReadReqJson);
                didReadRespJson = fabricService.SELECT__DIDDOC_FROM_BLOCKCHAIN(didReadReq);
                logger.info("didReadRequest() blockchain end, [didReadRespJson]" + didReadRespJson);
                didReadResp = JsonHelper.bidGson.fromJson(didReadRespJson, DidReadResponse.class);
            } else {
                logger.info("didReadRequest() DidResolve start, [didReadReqJson]" + didReadReqJson);
                String urResult = sendUR("/DidResolve", JsonHelper.bidGson.toJson(didReadReq));
                logger.info("didReadRequest() DidResolve End, [urResult]" + urResult);
                didReadResp = JsonHelper.bidGson.fromJson(urResult, DidReadResponse.class);
            }
            didReadResp.setResponder(serverDID);
            didReadResp.setNonce(didReadReq.getNonce());
            didReadResp.setOperation(didReadReq.getOperation());
            logger.info("didReadRequest() End");

        } catch (Exception e) {
            String errMsg = getErrMsg(didReadReq, e.getMessage());
            logger.error(errMsg);
        }
        return didReadResp;
    }


    /**
     * VC 상태 정보 조회 요청
     * @param req
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/delegationStatus", method = RequestMethod.GET)
    @ResponseBody
    public VcStatusResponse delegationVcStatusInfos(HttpServletRequest req) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String clientIp = ServiceUtil.getClientIp(req);
        VcStatusResponse vcStatusResp = new VcStatusResponse();
        vcStatusResp.setMsgType(VcStatusResponse.class.getSimpleName());
        String logPrefix = "[" + clientIp + "]-";

        String vcType = "DelegationCredential";
        String serialNum = "123456";

        try {
            String credentialStatusData =  didManageServcie.select__CREDENTIALSTATUSDATA(vcType, serialNum);
            logger.info(logPrefix + "VC 상태 정보 조회 성공");

            vcStatusResp.setResultCode(DidConstants.VCA_RTN_SUCCESS);
            vcStatusResp.setResultMsg(DidConstants.STATUS_SUCCESS_MSG);
            vcStatusResp.setVcStatus(credentialStatusData);
            logger.info(logPrefix + ExtHelper.getDidGson().toJson(vcStatusResp));
        }catch (Exception e) {
            String exceptonMsg = kr.re.etri.did.idmngserver.util.StringUtils.exceptionAsString(e);
            logger.error(exceptonMsg);
            vcStatusResp.setResultCode(DidConstants.VCA_RTN_FAIL);
            vcStatusResp.setResultMsg(exceptonMsg);
        }

        return vcStatusResp;
    }

    /**
     * VC 폐기 요청
     * @param vcRevokeReq
     * @param request
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     * @throws SignatureException
     * @throws ParseException
     * @throws Exception
     */
    @RequestMapping(value = "/delegationRevoke", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public VcRevokeResponse revokeDelegationVc(@RequestBody VcRevokeRequest vcRevokeReq, HttpServletRequest request) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, SignatureException, ParseException, Exception{
        VcRevokeResponse revokeResp = new VcRevokeResponse();
        revokeResp.setMsgType(VcRevokeResponse.class.getSimpleName());
        revokeResp.setOperation(vcRevokeReq.getOperation());
        String logPrefix = "[" + vcRevokeReq.getTid() + "]-";

        try {
            String jsonData = ExtHelper.getDidGson().toJson(vcRevokeReq);
            logger.info(logPrefix + jsonData);
            String requesterId = vcRevokeReq.getDid();

            if(StringUtils.isEmpty(requesterId)) {
                revokeResp.setResultCode(DidConstants.VCA_RTN_FAIL);
                revokeResp.setResultMsg("requesterId is NULL!");
                logger.error(logPrefix + "requesterId is NULL!");
                return revokeResp;
            }
            String vcId = vcRevokeReq.getVid();
            if(StringUtils.isEmpty(vcId)) {
                revokeResp.setResultCode(DidConstants.VCA_RTN_FAIL);
                revokeResp.setResultMsg("vcId is NULL!");
                logger.error(logPrefix + "vcId is NULL!");
                return revokeResp;
            }

            String credentialStatusId = vcRevokeReq.getId();
            if(StringUtils.isEmpty(credentialStatusId)) {
                revokeResp.setResultCode(DidConstants.VCA_RTN_FAIL);
                revokeResp.setResultMsg("credentialStatusId is NULL!");
                logger.error(logPrefix + "credentialStatusId is NULL!");
                return revokeResp;
            }

            byte[] vcRevokeReqSign = vcRevokeReq.getSignature();
            if(vcRevokeReqSign == null) {
                revokeResp.setResultCode(DidConstants.VCA_RTN_FAIL);
                revokeResp.setResultMsg("vcRevokeReqSign is NULL!");
                logger.error(logPrefix + "vcRevokeReqSign is NULL!");
                return revokeResp;
            }


            //1. 요청자 DidDocument 조회
            DidReadRequest didReadRequest = new DidReadRequest();
            didReadRequest.setDid(requesterId);
            String didReadRequestJson = JsonHelper.bidGson.toJson(didReadRequest);
            logger.info(logPrefix + "didReadRequestJson - " + didReadRequestJson);
            DidReadResponse didReadResponse  = didReadRequest(didReadRequest);
            DidDoc didDoc = didReadResponse.getDocument();

            //2. 서명 검증
            String messageTBV = vcId + credentialStatusId + requesterId;
            AsymmKeySet userKeySet = UtilityProcessor.getSigningPublicKeyFromDidDoc(didDoc);
            String userAlgorithm = userKeySet.getAlgorithmParameter();
            logger.info(logPrefix + "VC폐기 서명검증  userAlgorithm:" +  userAlgorithm);
            byte[] clientPublicKeyBytes = userKeySet.getPublicKeyEncoded();
            if(!DidAuthUtils.verify(messageTBV, vcRevokeReqSign, clientPublicKeyBytes, userAlgorithm)) {
                revokeResp.setResultCode(DidConstants.VCA_RTN_FAIL);
                revokeResp.setResultMsg("서명데이터 검증을 실패하였습니다.");
                logger.error(logPrefix + "서명데이터 검증을 실패하였습니다.");
                return revokeResp;
            }


            //3.VC 상태 정보 갱신 및 서명
            Map<String, String> vcInfos = new HashMap<String, String>();
            vcInfos.put("vcId", vcId);
            vcInfos.put("credentialStatusId", credentialStatusId);

            String vcType = "DelegationCredential";
            String serialNum = "123456";
            String credentialStatusData =  didManageServcie.select__CREDENTIALSTATUSDATA(vcType, serialNum);
            logger.info(logPrefix + "VC 폐기 리스트 조회 성공");

            String cedentialStatusList;
            if(StringUtils.isEmpty(credentialStatusData)) {
                cedentialStatusList = VcReovkeService.generateCredentialStautsList2017(vcInfos);
                logger.info(logPrefix + "해당 VC 폐기 리스트에 추가 성공");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("VCSTATNUM", serialNum);
                params.put("VCKIND", vcType);
                params.put("CREDENTIALSTATUSDATA", cedentialStatusList);
                didManageServcie.insert__VCSTATUSDATA_BY_STATLIST(params);
                logger.info(logPrefix + "해당 VC 폐기 리스트에 DB 갱신 성공");
            }else {
                cedentialStatusList = VcReovkeService.updateCredentialStautsList2017(vcInfos, credentialStatusData);
                logger.info(logPrefix + "해당 VC 폐기 리스트에 추가 성공");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("VCSTATNUM", serialNum);
                params.put("VCKIND", vcType);
                params.put("CREDENTIALSTATUSDATA", cedentialStatusList);
                didManageServcie.update__VCSTATUSDATA_BY_STATLIST(params);
                logger.info(logPrefix + "해당 VC 폐기 리스트에 DB 갱신 성공");
            }


            String revokeRespJson = ExtHelper.getDidGson().toJson(revokeResp);
            logger.info(ServiceUtil.generateLogMessage(vcRevokeReq.getTid(), revokeRespJson));
            revokeResp.setResultCode(DidConstants.VCA_RTN_SUCCESS);
            revokeResp.setResultMsg(DidConstants.STATUS_SUCCESS_MSG);
        }catch (Exception e) {
            String exceptonMsg = kr.re.etri.did.idmngserver.util.StringUtils.exceptionAsString(e);
            logger.error(exceptonMsg);
            revokeResp.setResultCode(DidConstants.VCA_RTN_FAIL);
            revokeResp.setResultMsg(exceptonMsg);
        }

        return revokeResp;
    }
}
