package kr.re.etri.did.idmngserver;

import kr.re.etri.did.data.DidDoc;
import kr.re.etri.did.utility.ext.ExtHelper;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtil {

    @Test
    void convertDate() throws ParseException {
        String dateStr = "1982-02-11";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateStr);
        System.out.println(date); // Prints: Thu Feb 11 00:00:00 GMT 1982

    }

    @Test
    void serialDidDoc() {
        String didDocJson = "{\"@context\":[\"https://www.ezid.com/data\"],\"id\":\"did:ezid:pBd57xcmXta5ziUL-KOq\",\"verificationMethod\":[{\"id\":\"did:ezid:pBd57xcmXta5ziUL-KOq#keys-1\",\"type\":\"Ed25519VerificationKey2018\",\"controller\":\"did:ezid:pBd57xcmXta5ziUL-KOq\",\"publicKeyBase58\":\"EAsVbtnkaYpSX9hwyfj9Xjh9RK9Lhe2NpfecwVTRqDvu\",\"isEncrytKey\":false}],\"authentication\":[\"did:ezid:pBd57xcmXta5ziUL-KOq#keys-1\"],\"service\":[{\"id\":\"did:ezid:pBd57xcmXta5ziUL-KOq#openid\",\"type\":\"OpenIdConnectVersion1.0Service\",\"serviceEndpoint\":\"https://openid.example.com/\"}],\"created\":\"2022-08-25T06:02:52Z\"}";
        DidDoc didDoc = ExtHelper.getDidGson().fromJson(didDocJson, DidDoc.class);
        System.out.println("didDoc = " + didDoc);
        System.out.println("didDoc.getDid() = " + didDoc.getDid());
        System.out.println("didDoc.getCreated() = " + didDoc.getCreated());
        System.out.println("didDoc.getProof() = " + didDoc.getProof());
    }
}
