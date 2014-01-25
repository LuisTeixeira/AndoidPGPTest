package com.example.androidpgptest;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.Iterator;

import org.spongycastle.openpgp.PGPObjectFactory;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPPublicKeyRing;
import org.spongycastle.openpgp.PGPUtil;

/**
 * http://fastpicket.com/blog/2012/05/14/easy-pgp-in-java-bouncy-castle/
 *
 */
public class KeyHelper {
	static {
	    Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
	}
	
	 
	/**
	 * Decode a PGP public key block and return the keyring it represents.
	 */
	public static PGPPublicKeyRing getKeyring(InputStream keyBlockStream) throws IOException {
	    // PGPUtil.getDecoderStream() will detect ASCII-armor automatically and decode it,
	    // the PGPObject factory then knows how to read all the data in the encoded stream
	    PGPObjectFactory factory = new PGPObjectFactory(PGPUtil.getDecoderStream(keyBlockStream));
	 
	    // these files should really just have one object in them,
	    // and that object should be a PGPPublicKeyRing.
	    Object o = factory.nextObject();
	    if (o instanceof PGPPublicKeyRing) {
	        return (PGPPublicKeyRing)o;
	    }
	    throw new IllegalArgumentException("Input text does not contain a PGP Public Key");
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
	        key = (PGPPublicKey)keys.next();
	        if ( key.isEncryptionKey() ) {
	            return key;
	        }
	    }
	    return null;
	}
}
