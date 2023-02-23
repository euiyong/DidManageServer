package kr.re.etri.did.idmngserver.test;

import kr.re.etri.did.data.Authentication;
import kr.re.etri.did.data.DidDoc;
import kr.re.etri.did.data.PublicKey;
import kr.re.etri.did.idmngserver.config.ServerProperties;
import kr.re.etri.did.idmngserver.test.vo.MsgVO;
import kr.re.etri.did.utility.CryptoHelper;
import kr.re.etri.did.utility.ext.DidUtility;
import kr.re.etri.did.utility.ext.ExtHelper;
import kr.re.etri.ezid.DID;
import kr.re.etri.ezid.DIDGenerator;
import kr.re.etri.ezid.crypto.AsymmKeySet;
import kr.re.etri.ezid.crypto.KeySetGeneratorEdDSA;
import kr.re.etri.ezid.vc.core.Proof;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyTestController {
    private final ServerProperties serverProperties;
    public MyTestController(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @GetMapping("/test-log-level")
    public void testLogLevel() {
        log.info("========== Logger Level Test ==========");
        String name = "안녕 Spring!";
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info("info log={}", name);
        log.warn("warn log={}", name);
        log.error("error log={}", name);
    }

    @GetMapping("/test-server-pros")
    public void testServerPros() {
        log.info("========== Server Properties ==========");
        log.info("cert-path = {}", serverProperties.getCertPath());
        log.info("ur-server-url = {}", serverProperties.getUrServerUrl());
        log.info("server-priv-seed = {}", serverProperties.getServerPrivSeed());
    }

    @PostMapping(value = "/test-json-v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(value = "/test-json-v1", produces = MediaType.APPLICATION_JSON_VALUE)
    public MsgVO testJsonV1(@RequestBody MsgVO inMsgVO) throws Exception {
        //0. Input Check
        try {
            log.info("========== MsgVO INPUT ==========");
            log.info("inMsgVO Authentication ID = {}", inMsgVO.getAuthentication().getId());
            log.info("inMsgVO Proof ProofPurpose = {}", inMsgVO.getProof().getProofPurpose());
            log.info("========================================");
        }catch (Exception e) {
            e.printStackTrace();
        }

        //1. Make Date Sample
        String dateStr = "1982-02-11";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateStr);

        //2. Make byte[] Sample
        byte[] nonce = CryptoHelper.generateNonce();


        //Make Base data
        AsymmKeySet asymmKeySet = KeySetGeneratorEdDSA.generateEdDSA();
        DID objDid = DIDGenerator.generate("ezid", 30);
        String did = objDid.toString();
        PublicKey publicKey = DidUtility.generateKey("1", did, asymmKeySet, false);

        // 3. Make Authentication Sample
        Authentication authentication = new Authentication();
        authentication.setId(publicKey.getId());
        authentication.setPublicKey(publicKey);

        //4. Make Proof Sample
        String didDocJson = "{\"@context\":[\"https://www.ezid.com/data\"],\"id\":\"did:ezid:pBd57xcmXta5ziUL-KOq\",\"verificationMethod\":[{\"id\":\"did:ezid:pBd57xcmXta5ziUL-KOq#keys-1\",\"type\":\"Ed25519VerificationKey2018\",\"controller\":\"did:ezid:pBd57xcmXta5ziUL-KOq\",\"publicKeyBase58\":\"EAsVbtnkaYpSX9hwyfj9Xjh9RK9Lhe2NpfecwVTRqDvu\",\"isEncrytKey\":false}],\"authentication\":[\"did:ezid:pBd57xcmXta5ziUL-KOq#keys-1\"],\"service\":[{\"id\":\"did:ezid:pBd57xcmXta5ziUL-KOq#openid\",\"type\":\"OpenIdConnectVersion1.0Service\",\"serviceEndpoint\":\"https://openid.example.com/\"}],\"created\":\"2022-08-25T06:02:52Z\"}";
        DidDoc didDoc = ExtHelper.getDidGson().fromJson(didDocJson, DidDoc.class);
        log.info("didDoc.getDid() = {}", didDoc.getDid());
        log.info("didDoc.getCreated() = {}", didDoc.getCreated());
        log.info("didDoc.getProof() = {}", didDoc.getProof());
        Proof proof = DidUtility.generateProof(didDocJson, asymmKeySet, publicKey.getId());


        MsgVO msgVO = new MsgVO();
        msgVO.setBirthDate(date);
        msgVO.setNonce(nonce);
        msgVO.setAuthentication(authentication);
        msgVO.setProof(proof);

        return msgVO;
    }


    @GetMapping("/test-trans")
    @Transactional
    public void testTrans() {
        
    }
}
