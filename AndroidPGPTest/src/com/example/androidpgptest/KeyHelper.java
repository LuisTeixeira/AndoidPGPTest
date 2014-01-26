package com.example.androidpgptest;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Iterator;

import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPObjectFactory;
import org.spongycastle.openpgp.PGPPrivateKey;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPPublicKeyRing;
import org.spongycastle.openpgp.PGPSecretKey;
import org.spongycastle.openpgp.PGPSecretKeyRing;
import org.spongycastle.openpgp.PGPSecretKeyRingCollection;
import org.spongycastle.openpgp.PGPUtil;
import org.spongycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.spongycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

/**
 * http://fastpicket.com/blog/2012/05/14/easy-pgp-in-java-bouncy-castle/
 * 
 */
public class KeyHelper {
	static {
		Security.insertProviderAt(
				new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
	}

	/**
	 * Decode a PGP public key block and return the keyring it represents.
	 */
	public static PGPPublicKeyRing getPublicKeyring(InputStream keyBlockStream)
			throws IOException {
		// PGPUtil.getDecoderStream() will detect ASCII-armor automatically and
		// decode it,
		// the PGPObject factory then knows how to read all the data in the
		// encoded stream
		PGPObjectFactory factory = new PGPObjectFactory(
				PGPUtil.getDecoderStream(keyBlockStream));

		// these files should really just have one object in them,
		// and that object should be a PGPPublicKeyRing.
		Object o = factory.nextObject();
		if (o instanceof PGPPublicKeyRing) {
			return (PGPPublicKeyRing) o;
		}
		throw new IllegalArgumentException(
				"Input text does not contain a PGP Public Key");
	}

	public static PGPSecretKeyRing getSecretKeyring(InputStream keyBlockStream)
			throws IOException {
		// PGPUtil.getDecoderStream() will detect ASCII-armor automatically and
		// decode it,
		// the PGPObject factory then knows how to read all the data in the
		// encoded stream
		PGPObjectFactory factory = new PGPObjectFactory(
				PGPUtil.getDecoderStream(keyBlockStream));

		Object o = factory.nextObject();
		if (o instanceof PGPSecretKeyRing) {
			return (PGPSecretKeyRing) o;
		}
		throw new IllegalArgumentException(
				"Input text does not contain a PGP Secret Key");
	}

	/**
	 * Get the first encyption key off the given keyring.
	 */
	public static PGPPublicKey getEncryptionKey(PGPPublicKeyRing keyRing) {
		if (keyRing == null)
			return null;

		// iterate over the keys on the ring, look for one
		// which is suitable for encryption.
		Iterator<?> keys = keyRing.getPublicKeys();
		PGPPublicKey key = null;
		while (keys.hasNext()) {
			key = (PGPPublicKey) keys.next();
			if (key.isEncryptionKey()) {
				return key;
			}
		}
		return null;
	}

	public static PGPPrivateKey getDecryptionKey(PGPSecretKeyRing keyRing) throws PGPException{
		if (keyRing == null)
			return null;

		// iterate over the keys on the ring, look for one
		// which is suitable for encryption.
		Iterator<?> keys = keyRing.getSecretKeys();
		PGPSecretKey key = null;
		while (keys.hasNext()) {
			key = (PGPSecretKey) keys.next();
			return findPrivateKey(key, "".toCharArray());
		}
		return null;
	}

	public static PGPPrivateKey findPrivateKey(PGPSecretKey pgpSecKey,
			char[] pass) throws PGPException {
		if (pgpSecKey == null)
			return null;
		PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(
				new BcPGPDigestCalculatorProvider()).build(pass);
		return pgpSecKey.extractPrivateKey(decryptor);
	}

}
