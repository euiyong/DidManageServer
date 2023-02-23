package kr.re.etri.did.idmngserver.test.vo;

import kr.re.etri.did.data.Authentication;
import kr.re.etri.ezid.vc.core.Proof;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class MsgVO {
    private Date birthDate;
    private byte[] nonce;
    private Proof proof;
    private Authentication authentication;
}
