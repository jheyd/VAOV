package vaov.client.account.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.util.Optional;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.x509.X509V1CertificateGenerator;

import vaov.client.account.model.Password;
import vaov.client.util.Config;
import vaov.remote.services.KeyId;

public class KeystoreService {

	public Optional<KeyPair> loadKeyPair(KeyId keyId, Password password) {
		Optional<PublicKey> publicKey = Optional.ofNullable((PublicKey) loadKey(keyId.getPublicAlias(), password,
			Config.getKeyStore()));
		PrivateKey privateKey = (PrivateKey) loadKey(keyId.getPrivateAlias(), password, Config.getKeyStore());
		if (!publicKey.isPresent() || privateKey == null) {
			return Optional.empty();
		}
		return Optional.of(new KeyPair(publicKey.get(), privateKey));
	}

	public Optional<PublicKey> loadPublicKey(KeyId keyId) {
		return Optional.ofNullable((PublicKey) loadKey(keyId.getPublicAlias(), Config.getPublicKeyPassword(),
			Config.getPublicKeysFile()));
	}

	public void storeKeyPair(KeyId keyId, Password password, KeyPair keys) {
		storeKey(keyId.getPublicAlias(), keys.getPublic(), password, Config.getKeyStore(), Optional.empty());
		storeKey(keyId.getPrivateAlias(), keys.getPrivate(), password, Config.getKeyStore(),
			Optional.of(getCerts(keys)));
	}

	public void storePublicKey(KeyId keyId, PublicKey key) {
		storeKey(keyId.getPublicAlias(), key, Config.getPublicKeyPassword(), Config.getPublicKeysFile(),
			Optional.empty());
	}

	private Certificate[] getCerts(KeyPair keys) {
		/* We need a stupid certificate to store the key, so just create a
		 * self-signed one. */
		Date startDate = Date.valueOf("2000-01-01");
		Date expiryDate = Date.valueOf("3000-01-01");
		BigInteger serialNumber = new BigInteger("42");
		X500Name x500Name = new X500Name(new X500Principal("CN=Stupid CA Certificate").getName());

		// method is deprecated, for production use, we may need to include
		// the CertificateBuilder of the full bouncy-castle lib

		try {
			X509CertificateHolder certificateHolder = getCertNew(keys, startDate, expiryDate, serialNumber, x500Name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

		X509Certificate cert = getCertOld(keys, startDate, expiryDate, serialNumber);

		return new Certificate[] { cert };
	}

	// TODO 16.02.2015 jan implement correctly
	private X509CertificateHolder getCertNew(KeyPair keys, Date startDate, Date expiryDate, BigInteger serialNumber,
		X500Name x500Name) throws IOException {

		X500Name issuer = x500Name;
		BigInteger serial = serialNumber;
		java.util.Date notBefore = startDate;
		java.util.Date notAfter = expiryDate;
		X500Name subject = x500Name;
		SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfoFactory
			.createSubjectPublicKeyInfo(new AsymmetricKeyParameter(false));
		X509v1CertificateBuilder certificateBuilder = new X509v1CertificateBuilder(issuer, serial, notBefore, notAfter,
			subject, publicKeyInfo);
		ContentSigner signer = new ContentSigner() {

			@Override
			public byte[] getSignature() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public OutputStream getOutputStream() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AlgorithmIdentifier getAlgorithmIdentifier() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		X509CertificateHolder certificateHolder = certificateBuilder.build(signer);
		return certificateHolder;
	}

	private X509Certificate getCertOld(KeyPair keys, Date startDate, Date expiryDate, BigInteger serialNumber) {
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();

		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(new X500Principal("CN=Stupid CA Certificate"));
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(new X500Principal("CN=Stupid CA Certificate")); // note: same as issuer
		certGen.setPublicKey(keys.getPublic());
		certGen.setSignatureAlgorithm("MD2withRSA");

		X509Certificate cert;
		try {
			cert = certGen.generate(keys.getPrivate(), "BC");
		} catch (InvalidKeyException | IllegalStateException | NoSuchProviderException | SignatureException
			| CertificateEncodingException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return cert;
	}

	private static Key loadKey(String alias, Password password, File keyStoreFile) {
		Key key;
		try (FileInputStream fileInputStream = new FileInputStream(keyStoreFile)) {
			KeyStore ks = getKeyStoreInstance();
			char[] passwordChars = password.getCharArray();
			ks.load(fileInputStream, passwordChars);
			key = ks.getKey(alias, passwordChars);
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("wrong password", e);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new RuntimeException(e);
		}

		return key;
	}

	private static void storeKey(String alias, Key key, Password password, File keyStoreFile,
		Optional<Certificate[]> certs) {
		try {
			KeyStore keyStore = loadKeyStore(keyStoreFile, password);

			keyStore.setKeyEntry(alias, key, password.getCharArray(), certs.orElse(null));

			try (FileOutputStream fileOutputStream = new FileOutputStream(Config.getKeyStore());) {
				keyStore.store(fileOutputStream, password.getCharArray());
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private static KeyStore loadKeyStore(File keyStoreFile, Password password) throws KeyStoreException,
	NoSuchAlgorithmException, CertificateException, IOException {
		if (!keyStoreFile.exists()) {
			return loadNewKeyStore(keyStoreFile);
		}
		return loadExistingKeyStore(keyStoreFile, password);
	}

	private static KeyStore loadNewKeyStore(File keyStoreFile) throws KeyStoreException, IOException,
	NoSuchAlgorithmException, CertificateException {
		KeyStore ks = getKeyStoreInstance();
		ks.load(null, null);
		keyStoreFile.getParentFile().mkdirs();
		keyStoreFile.createNewFile();
		return ks;
	}

	private static KeyStore loadExistingKeyStore(File keyStoreFile, Password password) throws KeyStoreException,
	NoSuchAlgorithmException, CertificateException {
		if (keyStoreFile.isFile()) {
			try (FileInputStream fileInputStream = new FileInputStream(Config.getKeyStore());) {
				KeyStore ks = getKeyStoreInstance();
				ks.load(fileInputStream, password.getCharArray());
				return ks;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("a directory with the name of the keystore exists");
		}
	}

	private static KeyStore getKeyStoreInstance() throws KeyStoreException {
		return KeyStore.getInstance(Config.getKeyStoreType(), Config.getProvider());
	}

}
