package vaov.client.account.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.janheyd.javalibs.password.Password;
import vaov.client.util.Config;
import vaov.remote.services.KeyId;

public class KeystoreServiceTest {

	private static final Provider provider = Config.getProvider();

	private static final KeyId KEY_ID = new KeyId("alias");
	private static final Password PASSWORD = new Password("password".toCharArray());
	private static final KeyPair keyPair = getKeyPair();

	private File dir;
	private KeystoreService keystoreService;

	private static KeyPair getKeyPair() {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA", Config.getProvider());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		kpg.initialize(512);
		return kpg.genKeyPair();
	}

	@Before
	public void setUp() throws Exception {
		dir = Files.createTempDirectory("vaov").toFile();

		File publicKeysFile = new File(dir, "user.keystore");
		File keystoreFile = new File(dir, "public.keystore");

		keystoreService = new KeystoreService(provider, publicKeysFile, keystoreFile);
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteDirectory(dir);
	}

	@Test
	public void testLoadUnknownKeyPair() throws Exception {

		Optional<KeyPair> keyPair = keystoreService.loadKeyPair(KEY_ID, PASSWORD);

		assertThat(keyPair.isPresent(), is(false));
	}

	@Test
	public void testLoadUnknownPublicKey() throws Exception {

		Optional<PublicKey> publicKey = keystoreService.loadPublicKey(KEY_ID);

		assertThat(publicKey.isPresent(), is(false));
	}

	@Test
	public void testStoreAndLoadKeyPair() throws Exception {
		keystoreService.storeKeyPair(KEY_ID, PASSWORD, keyPair);

		Optional<KeyPair> loadKeyPair = keystoreService.loadKeyPair(KEY_ID, PASSWORD);

		assertThat(loadKeyPair.get().getPublic(), is(keyPair.getPublic()));
		assertThat(loadKeyPair.get().getPrivate(), is(keyPair.getPrivate()));
	}

	@Test
	public void testStoreAndLoadPublicKey() throws Exception {
		keystoreService.storePublicKey(KEY_ID, keyPair.getPublic());

		Optional<PublicKey> publicKey = keystoreService.loadPublicKey(KEY_ID);

		assertThat(publicKey.get(), is(keyPair.getPublic()));
	}

}
