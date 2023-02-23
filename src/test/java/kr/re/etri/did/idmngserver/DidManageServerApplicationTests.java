package kr.re.etri.did.idmngserver;

import kr.re.etri.did.idmngserver.config.ServerProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class DidManageServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testServerPros() {
		ServerProperties serverProperties = new ServerProperties();

		System.out.println("========== Server Properties ==========");
		System.out.println("cert-path = " + serverProperties.getCertPath());
		System.out.println("ur-server-url = " + serverProperties.getUrServerUrl());
		System.out.println("server-priv-seed = " + serverProperties.getServerPrivSeed());
	}

}
