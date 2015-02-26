package vaov.client.account.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Optional;

import vaov.client.account.model.Password;
import vaov.client.util.Config;
import vaov.remote.services.KeyId;

public class KeystoreService {

	private static final String KEYSTORE_TYPE = "BKS";
	private static final Password PUBLIC_KEY_PASSWORD = new Password("123456".toCharArray());

	private Provider provider;
	private File publicKeysFile;
	private File userKeysFile;

	public KeystoreService() {
		this(Config.getProvider(), Config.getPublicKeysFile(), Config.getKeyStoreFile());
	}

	public KeystoreService(Provider provider, File publicKeysFile, File keystoreFile) {
		this.provider = provider;
		this.publicKeysFile = publicKeysFile;
		this.userKeysFile = keystoreFile;
	}

	public Optional<KeyPair> loadKeyPair(KeyId keyId, Password password) {
		Optional<PublicKey> publicKey = Optional.ofNullable((PublicKey) loadKey(keyId.getPublicAlias(), password,
			userKeysFile));
		PrivateKey privateKey = (PrivateKey) loadKey(keyId.getPrivateAlias(), password, userKeysFile);
		if (!publicKey.isPresent() || privateKey == null) {
			return Optional.empty();
		}
		return Optional.of(new KeyPair(publicKey.get(), privateKey));
	}

	public Optional<PublicKey> loadPublicKey(KeyId keyId) {
		return Optional.ofNullable((PublicKey) loadKey(keyId.getPublicAlias(), PUBLIC_KEY_PASSWORD, publicKeysFile));
	}

	public void storeKeyPair(KeyId keyId, Password password, KeyPair keys) {
		storeKey(keyId.getPublicAlias(), keys.getPublic(), password, userKeysFile, Optional.empty());
		storeKey(keyId.getPrivateAlias(), keys.getPrivate(), password, userKeysFile,
			Optional.of(SelfSignedCertificateGenerator.getCerts(keys)));
	}

	public void storePublicKey(KeyId keyId, PublicKey publicKey) {
		storeKey(keyId.getPublicAlias(), publicKey, PUBLIC_KEY_PASSWORD, publicKeysFile, Optional.empty());
	}

	private Key loadKey(String alias, Password password, File keyStoreFile) {
		Key key;
		try {
			KeyStore ks = loadKeyStore(keyStoreFile, password);
			key = ks.getKey(alias, password.getCharArray());
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("wrong password", e);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new RuntimeException(e);
		}

		return key;
	}

	private void storeKey(String alias, Key key, Password password, File keystoreFile, Optional<Certificate[]> certs) {
		try {
			KeyStore keyStore = loadKeyStore(keystoreFile, password);

			keyStore.setKeyEntry(alias, key, password.getCharArray(), certs.orElse(null));

			if (!keystoreFile.exists()) {
				keystoreFile.getParentFile().mkdirs();
				keystoreFile.createNewFile();
			}

			if (keystoreFile.isDirectory()) {
				throw new RuntimeException("a directory with the name of the keystore exists"); // TODO 23.02.2015 jan error handling
			}

			try (FileOutputStream fileOutputStream = new FileOutputStream(keystoreFile)) {
				keyStore.store(fileOutputStream, password.getCharArray());
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private KeyStore loadKeyStore(File keyStoreFile, Password password) throws NoSuchAlgorithmException,
	CertificateException, IOException {
		if (!keyStoreFile.exists()) {
			return loadNewKeyStore();
		}

		if (keyStoreFile.isDirectory()) {
			throw new RuntimeException("a directory with the name of the keystore exists"); // TODO 23.02.2015 jan error handling
		}

		return loadExistingKeyStore(keyStoreFile, password);
	}

	private KeyStore loadNewKeyStore() throws IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore keystore = getKeyStoreInstance();
		keystore.load(null, null);
		return keystore;
	}

	private KeyStore loadExistingKeyStore(File keyStoreFile, Password password) throws NoSuchAlgorithmException,
		CertificateException {
		try (FileInputStream fileInputStream = new FileInputStream(keyStoreFile)) {
			KeyStore keystore = getKeyStoreInstance();
			keystore.load(fileInputStream, password.getCharArray());
			return keystore;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private KeyStore getKeyStoreInstance() {
		try {
			return KeyStore.getInstance(KEYSTORE_TYPE, provider);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
