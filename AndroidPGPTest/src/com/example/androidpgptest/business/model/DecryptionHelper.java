package com.example.androidpgptest.business.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Iterator;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openpgp.PGPCompressedData;
import org.spongycastle.openpgp.PGPEncryptedDataList;
import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPLiteralData;
import org.spongycastle.openpgp.PGPObjectFactory;
import org.spongycastle.openpgp.PGPOnePassSignatureList;
import org.spongycastle.openpgp.PGPPrivateKey;
import org.spongycastle.openpgp.PGPPublicKeyEncryptedData;
import org.spongycastle.openpgp.PGPSecretKey;
import org.spongycastle.openpgp.PGPSecretKeyRingCollection;
import org.spongycastle.openpgp.PGPUtil;
import org.spongycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.spongycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.spongycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;

public class DecryptionHelper {

	public DecryptionHelper(InputStream in, OutputStream out, InputStream keyIn)
			throws Exception {

		char[] passwd = "".toCharArray();

		Security.addProvider(new BouncyCastleProvider());

		in = org.spongycastle.openpgp.PGPUtil.getDecoderStream(in);

		PGPObjectFactory pgpF = new PGPObjectFactory(in);
		PGPEncryptedDataList enc;

		Object o = pgpF.nextObject();
		//
		// the first object might be a PGP marker packet.
		//
		if (o instanceof PGPEncryptedDataList) {
			enc = (PGPEncryptedDataList) o;
		} else {
			enc = (PGPEncryptedDataList) pgpF.nextObject();
		}

		//
		// find the secret key
		//
		Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects();
		PGPPrivateKey sKey = null;
		PGPPublicKeyEncryptedData pbe = null;

		while (sKey == null && it.hasNext()) {
			pbe = it.next();

			sKey = findPrivateKey(keyIn, pbe.getKeyID(), passwd);
		}

		if (sKey == null) {
			throw new IllegalArgumentException(
					"Secret key for message not found.");
		}

		InputStream clear = pbe
				.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));

		PGPObjectFactory plainFact = new PGPObjectFactory(clear);

		Object message = plainFact.nextObject();

		if (message instanceof PGPCompressedData) {
			PGPCompressedData cData = (PGPCompressedData) message;
			PGPObjectFactory pgpFact = new PGPObjectFactory(
					cData.getDataStream());

			message = pgpFact.nextObject();
		}

		if (message instanceof PGPLiteralData) {
			PGPLiteralData ld = (PGPLiteralData) message;

			InputStream unc = ld.getInputStream();
			int ch;

			while ((ch = unc.read()) >= 0) {
				out.write(ch);
			}
		} else if (message instanceof PGPOnePassSignatureList) {
			throw new PGPException(
					"Encrypted message contains a signed message - not literal data.");
		} else {
			throw new PGPException(
					"Message is not a simple encrypted file - type unknown.");
		}

		if (pbe.isIntegrityProtected()) {
			if (!pbe.verify()) {
				throw new PGPException("Message failed integrity check");
			}
		}
	}

	public static PGPPrivateKey findPrivateKey(InputStream keyIn, long keyID,
			char[] pass) throws IOException, PGPException,
			NoSuchProviderException {
		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
				PGPUtil.getDecoderStream(keyIn));
		return findPrivateKey(pgpSec.getSecretKey(keyID), pass);

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