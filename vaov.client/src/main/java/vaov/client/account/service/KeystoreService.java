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

import vaov.client.util.Config;
import vaov.remote.services.KeyId;
import vaov.util.password.Password;

public class KeystoreService {

	private static final String KEYSTORE_TYPE = "BKS";
	private static final Password PUBLIC_KEY_PASSWORD = new Password("123456".toCharArray());

	private Provider provider;
	private File publicKeysFile;
	private File userKeysFile;

	public static KeystoreService createKeystoreService() {
		return new KeystoreService(Config.getProvider(), Config.getPublicKeysFile(), Config.getKeyStoreFile());
	}

	public KeystoreService(Provider provider, File publicKeysFile, File keystoreFile) {
		this.provider = provider;
		this.publicKeysFile = publicKeysFile;
		this.userKeysFile = keystoreFile;
	}

	public Optional<KeyPair> loadKeyPair(KeyId keyId, Password password) throws UnrecoverableKeyException {

		Optional<PublicKey> loadPublicKey = loadPublicKey(keyId, password);
		Optional<PrivateKey> loadPrivateKey = loadPrivateKey(keyId, password);

		if (!loadPublicKey.isPresent() && !loadPrivateKey.isPresent())
			return Optional.empty();

		if (!loadPublicKey.isPresent() && loadPrivateKey.isPresent())
			throw new RuntimeException("publicKey not present but privateKey present");
		if (loadPublicKey.isPresent() && !loadPrivateKey.isPresent())
			throw new RuntimeException("privateKey not present but publicKey present");

		return Optional.of(new KeyPair(loadPublicKey.get(), loadPrivateKey.get()));
	}

	public Optional<PublicKey> loadPublicKey(KeyId keyId) {
		Optional<Key> keyOpt;
		try {
			keyOpt = loadKey(keyId.getPublicAlias(), PUBLIC_KEY_PASSWORD, publicKeysFile);
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException(e);
		}

		if (!keyOpt.isPresent())
			return Optional.empty();

		Key key = keyOpt.get();
		if (!(key instanceof PublicKey))
			throw new RuntimeException("key is not a PublicKey");

		return Optional.of((PublicKey) key);
	}

	public void storeKeyPair(KeyId keyId, Password password, KeyPair keys) throws UnrecoverableKeyException {
		storeKey(keyId.getPublicAlias(), keys.getPublic(), password, userKeysFile, Optional.empty());
		storeKey(keyId.getPrivateAlias(), keys.getPrivate(), password, userKeysFile,
			Optional.of(SelfSignedCertificateGenerator.getCerts(keys)));
	}

	public void storePublicKey(KeyId keyId, PublicKey publicKey) {
		try {
			storeKey(keyId.getPublicAlias(), publicKey, PUBLIC_KEY_PASSWORD, publicKeysFile, Optional.empty());
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException(e);
		}
	}

	private KeyStore getKeyStoreInstance() {
		try {
			return KeyStore.getInstance(KEYSTORE_TYPE, provider);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		}
	}

	private KeyStore loadExistingKeyStore(File keyStoreFile, Password password) throws NoSuchAlgorithmException,
		CertificateException, UnrecoverableKeyException {
		try (FileInputStream fileInputStream = new FileInputStream(keyStoreFile)) {
			KeyStore keystore = getKeyStoreInstance();
			keystore.load(fileInputStream, password.getCharArray());
			return keystore;
		} catch (IOException e) {
			Throwable cause = e.getCause();
			if (cause instanceof UnrecoverableKeyException)
				throw (UnrecoverableKeyException) cause;
			throw new RuntimeException(e);
		}
	}

	private Optional<Key> loadKey(String alias, Password password, File keyStoreFile) throws UnrecoverableKeyException {
		try {
			KeyStore ks = loadKeyStore(keyStoreFile, password);
			return Optional.ofNullable(ks.getKey(alias, password.getCharArray()));
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private KeyStore loadKeyStore(File keyStoreFile, Password password) throws NoSuchAlgorithmException,
	CertificateException, IOException, UnrecoverableKeyException {
		if (!keyStoreFile.exists())
			return loadNewKeyStore();

		if (keyStoreFile.isDirectory())
			throw new RuntimeException("a directory with the name of the keystore exists"); // TODO 23.02.2015 jan error handling

		return loadExistingKeyStore(keyStoreFile, password);
	}

	private KeyStore loadKeyStore(Password password, File keystoreFile) throws UnrecoverableKeyException {
		KeyStore keyStore;
		try {
			keyStore = loadKeyStore(keystoreFile, password);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new RuntimeException(e);
		}
		return keyStore;
	}

	private KeyStore loadNewKeyStore() throws IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore keystore = getKeyStoreInstance();
		keystore.load(null, null);
		return keystore;
	}

	private Optional<PrivateKey> loadPrivateKey(KeyId keyId, Password password) throws UnrecoverableKeyException {
		Optional<Key> keyOpt = loadKey(keyId.getPrivateAlias(), password, userKeysFile);

		if (!keyOpt.isPresent())
			return Optional.empty();

		Key key = keyOpt.get();
		if (!(key instanceof PrivateKey))
			throw new RuntimeException("key is not a PrivateKey");

		return Optional.of((PrivateKey) key);
	}

	private Optional<PublicKey> loadPublicKey(KeyId keyId, Password password) throws UnrecoverableKeyException {
		Optional<Key> keyOpt = loadKey(keyId.getPublicAlias(), password, userKeysFile);
		if (!keyOpt.isPresent())
			return Optional.empty();
		Key key = keyOpt.get();
		if (!(key instanceof PublicKey))
			throw new RuntimeException("key is not a PublicKey");
		PublicKey publicKey = (PublicKey) key;
		Optional<PublicKey> publicKeyOpt = Optional.of(publicKey);
		return publicKeyOpt;
	}

	private void setKeystoreEntry(KeyStore keyStore, String alias, Key key, Password password,
		Optional<Certificate[]> certs) {
		try {
			keyStore.setKeyEntry(alias, key, password.getCharArray(), certs.orElse(null));
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		}
	}

	private void storeKey(String alias, Key key, Password password, File keystoreFile, Optional<Certificate[]> certs)
		throws UnrecoverableKeyException {
		KeyStore keyStore = loadKeyStore(password, keystoreFile);

		setKeystoreEntry(keyStore, alias, key, password, certs);

		storeKeyStore(keyStore, keystoreFile, password);
	}

	private void storeKeyStore(KeyStore keyStore, File keystoreFile, Password password) {
		if (!keystoreFile.exists()) {
			keystoreFile.getParentFile().mkdirs();
		}

		try (FileOutputStream fileOutputStream = new FileOutputStream(keystoreFile)) {
			keyStore.store(fileOutputStream, password.getCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new RuntimeException(e);
		}
	}

}
