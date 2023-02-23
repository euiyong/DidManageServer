package kr.re.etri.did.idmngserver.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

import kr.re.etri.did.data.DidDoc;
import kr.re.etri.did.data.PublicKey;
import kr.re.etri.did.data.PublicKeyJwk;
import kr.re.etri.ezid.DID;
import kr.re.etri.ezid.DIDGenerator;
import kr.re.etri.ezid.crypto.AsymmKeySet;
import kr.re.etri.ezid.diddoc.DidDocument;
import kr.re.etri.ezid.diddoc.pubkey.JsonWebKey;
import kr.re.etri.ezid.diddoc.pubkey.JsonWebKeyPublicKeyJwk;
import kr.re.etri.ezid.diddoc.pubkey.PublicKeyEd25519Signature2018;
import kr.re.etri.ezid.didprops.Challenge;
import kr.re.etri.ezid.didprops.TypeDefinition;
import kr.re.etri.ezid.didprops.proof.Proof;
import kr.re.etri.ezid.didprops.proof.ProofPurpose;
import kr.re.etri.ezid.utils.Base58;
import kr.re.etri.ezid.utils.Base64Url;

public class UtilityProcessor {
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	
	/**
	 * Nonce 생성
	 * 
	 * @return nonce
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("deprecation")
	public static byte[] generateNonce() throws UnsupportedEncodingException {
		
		byte[] nonce = DigestUtils.shaHex(String.valueOf(new Date().getTime())).getBytes("utf-8");
		
		return nonce;
	}
	
	/**
	 * DID 생성
	 * 
	 * @return did
	 * @throws Exception
	 */
	public static String generateDid() throws Exception {
		
		DID did = DIDGenerator.generate("ezid", 30);
		
		return did.toString();
	}
	
	
	/**
	 * 랜덤 숫자 생성
	 * 
	 * @return Random Number(0~99999)
	 * @throws Exception
	 */
	public static int generateRandomNumber() throws Exception {
		int random_num = 0;
		random_num = (int)(Math.random() * 100000);
		
		return random_num;
	}
	
	
	/**
	 * 학생증 번호 생성
	 * 
	 * @return Student ID
	 * @throws Exception
	 */
	public static String generateStudentId() throws Exception {
		
		int first_num = 0;
		int last_num = 0;
		first_num = (int)(Math.random() * 9999) + 1;
		last_num = (int)(Math.random() * 99999) + 1;
		
		return first_num + "-" + last_num;
	}
	
	
	/**
	 * 멤버십 번호 생성
	 * 
	 * @return Membership ID
	 * @throws Exception
	 */
	public static String generateMembershipId() throws Exception {
		
		int first_num = 0;
		int middle_num = 0;
		int last_num = 0;
		first_num = (int)(Math.random() * 9999) + 1;
		middle_num = (int)(Math.random() * 9999) + 1;
		last_num = (int)(Math.random() * 9999) + 1;
		
		return first_num + "-" + middle_num + "-" + last_num;
	}
	
	
	/**
	 * 나이 계산
	 * 
	 * @param date
	 * @return age
	 */
	public static int generateAgeOfOver(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		int birth_year = calendar.get(Calendar.YEAR);
		
		calendar.setTime(new Date());
		int age = calendar.get(Calendar.YEAR) - birth_year;
		
		if(age >= 19)
			age = 18;
		
		return age;
	}
	
	
	/**
	 * 발급일 계산
	 * @param date
	 * @return issueranceDate 
	 */
	public static String generateIssueranceDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return format.format(calendar.getTime());
	}
	
	
	/**
	 * 만료일 계산
	 * @param type String
	 * @param date
	 * @return expirationDate
	 */
	public static String generateExpirationDate(String type, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		if(type.equals("adultvc")) {
			calendar.add(Calendar.YEAR, 10);
		
		} else if(type.equals("cardvc")) {
		calendar.add(Calendar.YEAR, 1);
			calendar.set(
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH) - 1,
					calendar.getActualMaximum(Calendar.HOUR) + 12,
					calendar.getActualMaximum(Calendar.MINUTE),
					calendar.getActualMaximum(Calendar.SECOND)
			);
			
		} else if(type.equals("studentidvc")){
			calendar.add(Calendar.YEAR, 2);
			calendar.add(Calendar.MONTH, 1);			
			calendar.set(
					calendar.get(Calendar.YEAR),
					calendar.getActualMaximum(Calendar.MONTH),
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
					calendar.getActualMaximum(Calendar.HOUR) + 12,
					calendar.getActualMaximum(Calendar.MINUTE),
					calendar.getActualMaximum(Calendar.SECOND)
			);
		} else if(type.equals("membershipvc")){
			calendar.add(Calendar.YEAR, 2);
			calendar.add(Calendar.MONTH, 1);			
			calendar.set(
					calendar.get(Calendar.YEAR),
					calendar.getActualMaximum(Calendar.MONTH),
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
					calendar.getActualMaximum(Calendar.HOUR) + 12,
					calendar.getActualMaximum(Calendar.MINUTE),
					calendar.getActualMaximum(Calendar.SECOND)
			);
		}
		
		return format.format(calendar.getTime());
	}
	
	
	/**
	 * Date 변환(T/Z 문자열 포함된 구조)
	 * @param date Date
	 * @return convertedDate String
	 */
	public static String convertDateToString(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return format.format(calendar.getTime());
	}
	
	
	/**
	 * Date 변환(T/Z 문자열 포함된 구조)
	 * @param date String
	 * @return convertedDate Date
	 * @throws ParseException 
	 */
	public static Date convertStringToDate(String dateStr) throws ParseException {
		Date date = format.parse(dateStr);
		
		return date;
	}
	
	
	/**
	 * 현재 Date 출력(T/Z 문자열 포함된 구조)
	 * @return convertedDate Date
	 * @throws ParseException
	 */
	public static Date generateCurrentDate() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());		
		String curDate = format.format(calendar.getTime());
		
		return format.parse(curDate);
	}
	
	
	/**
	 * 모든 중괄호 제거 (1차원 배열용)
	 * @param input String
	 * @return converted input
	 */
	public static String removeBraces(String input){
		return input.replaceAll("\\{","").replaceAll("\\}","");
	}
	
	
	/**
	 * 모든 대괄호 제거 (1차원 배열용)
	 * @param input String
	 * @return converted input
	 */
	public static String removeBrackets(String input){
		return input.replaceAll("\\[","").replaceAll("\\]","");
	}
	
	
	/**
	 * 바깥 대괄호 제거 (2차원 배열용)
	 * @param input String
	 * @return converted input
	 */
	public static String removeOuterBrackets(String input){
		return input.substring(1, input.length()-1);
	}
	
	
	/**
	 * String문자열을 1차원 배열로 변환
	 * 
	 * @param input
	 * @return
	 */
	public static String[] convertToStringArray(String input) {
		String[] inputArr = removeBrackets(input).split(",");
		return inputArr;
	}
	
	
	/**
	 * String문자열을 2차원 배열로 변환
	 * 
	 * @param input
	 * @return
	 */
	public static String[][] convertTo2DArray(String input) {
		String[] inputArr = removeBrackets(input).split("\\],");
		String[][] input2DArr = new String[][] {};
		
		for(int i=0 ; i<inputArr.length ; i++)
			input2DArr[i] = removeBrackets(inputArr[i]).split(",");
		
		return input2DArr;
	}
	
	
	/**
	 * BioIDList String문자열을 2차원 String 배열로 변환
	 * 
	 * @param input
	 * @return
	 */
	public static String[][] convertBioIdListTo2DStringArray(String input) {
		String[] inputArr = removeOuterBrackets(input).replace("\"],", "\"],,").split(",,");
		String[][] input2DArr = new String[inputArr.length][2];
		
		for(int i=0 ; i<inputArr.length ; i++)
			input2DArr[i] = removeOuterBrackets(inputArr[i]).replace("\",", "\",,").split(",,");
		
		return input2DArr;
	}
	
	
	public static String generateDidDoc(AsymmKeySet eddsaKeySet) throws Exception {		
		DID did = DIDGenerator.generate("ezid", 30);
		
		DidDocument didDoc = new DidDocument(did.toString());
		String publicKeyId = didDoc.getPublicKeyIdCandidate("keys-");

		PublicKeyEd25519Signature2018 didPubKey = new PublicKeyEd25519Signature2018();
		didPubKey.setId(publicKeyId);
		didPubKey.setController(didDoc.getDid());
		didPubKey.setPublicKeyEncoded(eddsaKeySet.getPublicKeyEncoded());
		didDoc.addPublicKey(didPubKey);
		
		didDoc.addAuthentication(publicKeyId);
		
		didDoc.setCreated(new Date());
		didDoc.setUpdated(new Date());
		
		Proof proof = new Proof();
		proof.setType(didPubKey.getType());
		proof.setChallenge(Base58.encode(Challenge.generate(20)));
		proof.setCreated(new Date());
		proof.setProofPurpose(ProofPurpose.AUTHENTICATION);
		proof.setDomain("https://www.dreamsecurity.com");
		proof.setVerificationMethod(didPubKey.getId());
		
		didDoc.sign(proof, eddsaKeySet.getPrivateKeyEncoded());
		
		return didDoc.serialize();
	}
	
	public static AsymmKeySet getSigningPublicKeyFromDidDoc(DidDoc didDoc) throws Exception {

		kr.re.etri.ezid.vc.core.Proof proof = didDoc.getProof();
        if (proof != null) {
            String publicKeyId = proof.getVerificationMethod();
            if (publicKeyId != null) {
                return getPublicKeyFromDidDoc(didDoc, publicKeyId);
            }
        }

        PublicKey[] publicKey = didDoc.getVerificationMethod();
        if (publicKey != null)
            return getPublicKeyFromDidDoc(didDoc, publicKey[0].getId());

        publicKey = didDoc.getPublicKey();
        if (publicKey != null)
            return getPublicKeyFromDidDoc(didDoc, publicKey[0].getId());

        throw new Exception("invalid DidDoc format. publicKey or verificationMethod not found.");
    }
	
	public static AsymmKeySet getPublicKeyFromDidDoc(DidDoc didDoc, String publicKeyId) throws Exception {

		// publicKey를 찾기 위해 새로운 규격인 verificationMethod 부터 확인한다.
		String algorithmParameter = null;
		byte[] publicKeyEncoded = null;

		PublicKey[] publicKeyArray = didDoc.getVerificationMethod();

		if (publicKeyArray == null) {
			// verificationMethod 필드가 null 이면 기존 규격인 publicKey 필드를 확인한다.
			publicKeyArray = didDoc.getPublicKey();
		}

		if (publicKeyArray == null)
			throw new Exception("PublicKey not included. Invalid DidDoc format.");

		PublicKeyJwk publicKeyJwk = null;
		JsonWebKeyPublicKeyJwk jwk = null;

		int findIdx = 0;
		for (findIdx = 0; findIdx < publicKeyArray.length; findIdx++) {
			if (publicKeyArray[findIdx].getId().equals(publicKeyId))
				break;
		}

		if (findIdx == publicKeyArray.length)
			throw new Exception("PublicKey not found. keyId: " + publicKeyId);

		switch (publicKeyArray[findIdx].getType()) {
		case TypeDefinition.TYPE_Ed25519VerificationKey2018:
			publicKeyEncoded = Base58.decode(publicKeyArray[findIdx].getPublicKeyBase58());
			return new AsymmKeySet(AsymmKeySet.CURVE_Ed25519, null, publicKeyEncoded);

		case TypeDefinition.TYPE_EcdsaSecp256k1VerificationKey2019:
			publicKeyJwk = publicKeyArray[findIdx].getPublicKeyJwk();
			jwk = new JsonWebKeyPublicKeyJwk();
			jwk.setCrv(publicKeyJwk.getCrv());
			jwk.setX(publicKeyJwk.getX());
			jwk.setY(publicKeyJwk.getY());
			jwk.setKty(publicKeyJwk.getKty());
			jwk.setKid(publicKeyJwk.getKid());

			publicKeyEncoded = JsonWebKey.getPublicKeyEncoded(jwk);
			algorithmParameter = JsonWebKeyPublicKeyJwk.convertToAlgorithmParameter(jwk.getCrv());
			return new AsymmKeySet(algorithmParameter, null, publicKeyEncoded);

		case TypeDefinition.TYPE_JsonWebKey2020:
			publicKeyJwk = publicKeyArray[findIdx].getPublicKeyJwk();
			jwk = new JsonWebKeyPublicKeyJwk();
			if (publicKeyJwk.getKty().equals("EC")) {
				jwk.setCrv(publicKeyJwk.getCrv());
				jwk.setX(publicKeyJwk.getX());
				jwk.setY(publicKeyJwk.getY());
				jwk.setKty(publicKeyJwk.getKty());
				jwk.setKid(publicKeyJwk.getKid());

				publicKeyEncoded = JsonWebKey.getPublicKeyEncoded(jwk);
				algorithmParameter = JsonWebKeyPublicKeyJwk.convertToAlgorithmParameter(jwk.getCrv());
			} else if (publicKeyJwk.getKty().equals("RSA")) {
				jwk.setN(publicKeyJwk.getN());
				jwk.setUse(publicKeyJwk.getUse());
				jwk.setE(publicKeyJwk.getE());
				jwk.setKty(publicKeyJwk.getKty());
				jwk.setKid(publicKeyJwk.getKid());

				publicKeyEncoded = JsonWebKey.getPublicKeyEncoded(jwk);

				byte[] n = Base64Url.decode(jwk.getN());
				int keyLength = n.length / 128;
				System.out.println("  - key length : " + n.length + " : " + keyLength);
				switch (keyLength) {
				case 2:
					algorithmParameter = AsymmKeySet.Param_RSA2048;
					break;
				default:
					throw new Exception("not supported RSA key length.");
				}
			}

			return new AsymmKeySet(algorithmParameter, null, publicKeyEncoded);

		case TypeDefinition.TYPE_Secp256k1VerificationKey2019:
		case TypeDefinition.TYPE_Secp256k1VerificationKey: // iconloop??
			publicKeyEncoded = kr.re.etri.did.utility.Base64.decode(publicKeyArray[findIdx].getPublicKeyBase64(), kr.re.etri.did.utility.Base64.DEFAULT);
			return new AsymmKeySet(AsymmKeySet.CURVE_Secp256k1, null, publicKeyEncoded);

		default:
			throw new Exception("not supported publicKeyType : " + publicKeyArray[findIdx].getType());
		}
	}
}