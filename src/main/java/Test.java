//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.NoSuchProviderException;
//import java.security.Security;
//import java.util.Collection;
//import java.util.Iterator;
//
//import org.bouncycastle.jce.provider.BouncyCastleProvider ;
//import org.bouncycastle.openpgp.*;
//
//public class Test {
//	private static File privateKeyFile = new File("private key file");
//
//	private static String privateKeyPassword = "Password";
//
//	private static PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass) throws IOException, PGPException, NoSuchProviderException {
//		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection((Collection<PGPSecretKeyRing>) PGPUtil.getDecoderStream(keyIn));
//		PGPSecretKey secretKey = pgpSec.getSecretKey(keyID);
//		if (secretKey == null) {
//			return null;
//		}
//		return secretKey.extractPrivateKey(pass, "BC");
//
//
//
//
//	}
//
//	public static String decrypt(byte[] encdata) {
//		System.out.println("decrypt(): data length=" + encdata.length);
//
//		try {
//			ByteArrayInputStream bais = new ByteArrayInputStream(encdata);
//			FileInputStream privKey = new FileInputStream(privateKeyFile);
//			return _decrypt(bais, privKey, privateKeyPassword.toCharArray());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private static String _decrypt(InputStream in, InputStream keyIn, char[] passwd) throws Exception {
//		in = PGPUtil.getDecoderStream(in);
//		try {
//			PGPObjectFactory pgpF = new PGPObjectFactory(in);
//			PGPEncryptedDataList enc;
//			Object o = pgpF.nextObject();
//			//
//			// the first object might be a PGP marker packet.
//			//
//			if (o instanceof PGPEncryptedDataList) {
//				enc = (PGPEncryptedDataList) o;
//			} else {
//				enc = (PGPEncryptedDataList) pgpF.nextObject();
//			}
//			//
//			// find the secret key
//			//
//			Iterator iterator = enc.getEncryptedDataObjects();
//			PGPPrivateKey privateKey = null;
//			PGPPublicKeyEncryptedData publicKeyEncryptedData = null;
//			while (privateKey == null && iterator.hasNext()) {
//				publicKeyEncryptedData = (PGPPublicKeyEncryptedData) iterator.next();
//				System.out.println("publicKeyEncryptedData id=" + publicKeyEncryptedData.getKeyID());
//				privateKey = findSecretKey(keyIn, publicKeyEncryptedData.getKeyID(), passwd);
//			}
//			if (privateKey == null) {
//				throw new IllegalArgumentException("secret key for message not found.");
//			}
//			System.out.println("my-alg:" + privateKey.getKey().getAlgorithm());
//			System.out.println("private key" + privateKey.toString());
//			InputStream inputStream = publicKeyEncryptedData.getDataStream(privateKey, "BC", "BC");
//			PGPObjectFactory objectFactory = new PGPObjectFactory(inputStream);
//			Object message = objectFactory.nextObject();
//			if (message instanceof PGPCompressedData) {
//				PGPCompressedData cData = (PGPCompressedData) message;
//				PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream());
//				message = pgpFact.nextObject();
//			}
//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//			if (message instanceof PGPLiteralData) {
//				PGPLiteralData pgpLiteralData = (PGPLiteralData) message;
//				InputStream unc = pgpLiteralData.getInputStream();
//				int ch;
//				while ((ch = unc.read()) >= 0) {
//					byteArrayOutputStream.write(ch);
//				}
//			} else if (message instanceof PGPOnePassSignatureList) {
//				throw new PGPException("encrypted message contains a signed message - not literal data.");
//			} else {
//				throw new PGPException("message is not a simple encrypted file - type unknown.");
//			}
//			if (publicKeyEncryptedData.isIntegrityProtected()) {
//				if (!publicKeyEncryptedData.verify()) {
//					System.err.println("message failed integrity check");
//				} else {
//					System.err.println("message integrity check passed");
//				}
//			} else {
//				System.err.println("no message integrity check");
//			}
//			return byteArrayOutputStream.toString();
//		} catch (PGPException e) {
//			System.err.println(e);
//			if (e.getUnderlyingException() != null) {
//				e.getUnderlyingException().printStackTrace();
//			}
//		}
//		return null;
//	}
//
//	public static void main(String[] args) throws IOException {
//		Security.addProvider(new BouncyCastleProvider());
//
//		byte[] newB = null;
//		try {
//			FileInputStream fileInputStream = new FileInputStream("ecnrypted file");
//			newB = new byte[fileInputStream.available()];
//			System.out.println("read bytes: " + fileInputStream.read(newB));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("byte array" + newB.length);
//		// ----- Decrypt the token that
//		String result = decrypt(newB);
//		FileOutputStream decryptedFile = new FileOutputStream("C:/new.txt");
//
//		while(result.length()!= -1)
//		{
//			decryptedFile.write(result.getBytes());
//		}
//
//		System.out.println("Decrypted: " + result);
//	}
//
//}