import java.security.Provider;
import java.security.Security;

import org.junit.Test;

import vaov.client.vote.Config;

public class TestProviders {

	@Test
	public void test() {
		Provider[] providers = Security.getProviders();
		System.out.println("found "+providers.length+" provider");
		for(Provider p : providers) {
			System.out.println(p.getName());
			System.out.println(p.getInfo());
			System.out.println();
		}
		
		System.out.println("Manually configured provider:");
		System.out.println(Config.getProvider().getName());
		System.out.println(Config.getProvider().getInfo());
		System.out.println();
	}

}