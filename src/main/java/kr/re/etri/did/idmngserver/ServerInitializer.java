package kr.re.etri.did.idmngserver;

import kr.re.etri.did.data.DidDoc;
import kr.re.etri.did.idmngserver.util.ServerConfig;
import kr.re.etri.did.utility.ext.DidUtility;
import kr.re.etri.ezid.crypto.AsymmKeySet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Slf4j
@Component
public class ServerInitializer {

    @PostConstruct
    public void init() {
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config/didservice.properties"));
            String rootPath 	= props.getProperty("didmngserver.cert.path");
            String didDocPath = rootPath+"DidManagerServer/didDoc.txt";
            String priKeyPath =rootPath+"DidManagerServer/privKey.txt";

            String privPassSeed = props.getProperty("server.priv.seed.didms");
            AsymmKeySet asymmKeySet = DidUtility.getEdd25519KeyPair(); //1. 키쌍생성

            DidDoc didDoc = DidUtility.getDidDocAndWrite(asymmKeySet, didDocPath); //2. document 생성 후 파일쓰기
            //AsymmKeySet asymmKeySetRSA = DidUtility.getRSAKeyPair();
            //DidDoc didDoc = DidUtility.getDidDocAndWrite(asymmKeySet, didDocPath, asymmKeySetRSA, "http://etri.hotel.re.kr/rental/service");
            if(didDoc == null) {
                throw new Exception(">>>>>>>>>> DidDocument 생성 오류 발생!");
            }

            ServerConfig.setDidDoc(didDoc);

            if (ServerConfig.getDidDoc().getDid() != null || "".equals(ServerConfig.getDidDoc().getDid())) {
                String enPrivkey = DidUtility.getEnPrivKeyAndWrite(asymmKeySet, priKeyPath, privPassSeed); //3. 개인키 추출 및 개인키 파일쓰기
                ServerConfig.setPrivKey(enPrivkey);
            }

            log.info("DidManageServer did : "+ServerConfig.getDidDoc().getDid());

        }catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
