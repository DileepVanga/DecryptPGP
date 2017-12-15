//import org.bouncycastle.openpgp.*;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
//
//import java.io.*;
//import java.util.Iterator;
//
//
//import java.security.NoSuchProviderException;
//import java.security.Security;
//
//
//
//public class PGPSecurityService {
//
//
//	public static  void main(String[] args){
//		try{
//			Security.addProvider(new BouncyCastleProvider());
//
//			/**
//			 * decrypt file
//			 **/
//			// my private key
//			InputStream privKey = new BufferedInputStream (new FileInputStream("C:\\apps\\private\\secring.gpg"));
//			// file to decrypt
//			InputStream file = new BufferedInputStream (new FileInputStream("C:\\apps\\encrypted.pgp"));
//
//			decryptFile(file, privKey,"mykey".toCharArray());
//		} catch (Exception ex){
//			ex.printStackTrace();
//		}
//	}
//
//	private static PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection  pgpSec, long keyID, char[] pass)
//			throws PGPException, NoSuchProviderException{
//		PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
//		if (pgpSecKey == null){
//			return null;
//		} else {
//			return pgpSecKey.extractPrivateKey(pass, "BC");
//		}
//	}
//
//	private static PGPPublicKey readPublicKeyFromCol(InputStream in)throws Exception {
//		PGPPublicKeyRing pkRing = null;
//		PGPPublicKeyRingCollection pkCol = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(in));
//		//System.out.println("key ring size=" + pkCol.size());
//		Iterator it = pkCol.getKeyRings();
//		while (it.hasNext()) {
//			pkRing = (PGPPublicKeyRing) it.next();
//			Iterator pkIt = pkRing.getPublicKeys();
//			while (pkIt.hasNext()) {
//				PGPPublicKey key = (PGPPublicKey) pkIt.next();
//				//System.out.println("Encryption key = " + key.isEncryptionKey() + ", Master key = " +key.isMasterKey());
//				if (key.isMasterKey())
//					return key;
//			}
//		}
//		return null;
//	}
//
//	private static void decryptFile( InputStream in, InputStream keyIn, char[] passwd)throws Exception{
//		in = PGPUtil.getDecoderStream(in);
//		try{
//			JcaPGPObjectFactory pgpF = new JcaPGPObjectFactory(in);
//
//		//	PGPObjectFactory pgpF = new PGPObjectFactory(in);
//			Object o = pgpF.nextObject();
//			PGPEncryptedDataList enc = o instanceof PGPEncryptedDataList?(PGPEncryptedDataList)o : (PGPEncryptedDataList)pgpF.nextObject();
//
//
//
//			//
//			// the first object might be a PGP marker packet.
//			//
//            /*
//            if (o instanceof PGPEncryptedDataList){
//                enc = (PGPEncryptedDataList)o;
//            }  else {
//                enc = (PGPEncryptedDataList)pgpF.nextObject();
//            }
//            */
//			//
//			// find the secret key
//			//
//			Iterator it = enc.getEncryptedDataObjects();
//			PGPPrivateKey sKey = null;
//			PGPPublicKeyEncryptedData pbe = null;
//			PGPSecretKeyRingCollection  pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn));
//
//			while (sKey == null && it.hasNext()){
//				pbe = (PGPPublicKeyEncryptedData)it.next();
//				sKey = findSecretKey(pgpSec, pbe.getKeyID(), passwd);
//			}
//			if (sKey == null){
//				throw new IllegalArgumentException("secret key for message not found.");
//			}
//
//			InputStream clear = pbe.getDataStream(sKey, "BC");
//			PGPObjectFactory plainFact = new PGPObjectFactory(clear);
//			Object message = plainFact.nextObject();
//			if (message instanceof PGPCompressedData){
//				PGPCompressedData   cData = (PGPCompressedData)message;
//				PGPObjectFactory  pgpFact = new PGPObjectFactory(cData.getDataStream());
//				message = pgpFact.nextObject();
//				if (message instanceof PGPLiteralData){
//					PGPLiteralData      ld = (PGPLiteralData)message;
//					FileOutputStream    fOut = new FileOutputStream("C:\\apps\\private\\outputsigned.txt");
//					InputStream    unc = ld.getInputStream();
//					int    ch;
//					while ((ch = unc.read()) >= 0){
//						fOut.write(ch);
//					}
//				}else if (message instanceof PGPOnePassSignatureList) {
//
//					PGPOnePassSignatureList p1 = (PGPOnePassSignatureList) message;
//					InputStream pubKey = new BufferedInputStream (new FileInputStream("C:\\apps\\private\\pubring.gpg"));
//					PGPPublicKey key =readPublicKeyFromCol(pubKey);
//					if(key!=null){
//						PGPOnePassSignature ops = p1.get(0);
//						System.out.println("ops.getKeyID()="+ops.getKeyID());
//						ops.initVerify(key, "BC");
//						// file output dirctory
//						FileOutputStream out = new FileOutputStream("C:\\apps\\private\\outputsigned.txt");
//						PGPLiteralData  p2 = (PGPLiteralData) pgpFact.nextObject();
//						int ch;
//						InputStream  dIn = p2.getInputStream();
//						while ((ch = dIn.read()) >= 0) {
//							ops.update((byte)ch);
//							out.write(ch);
//						}
//						out.close();
//					} else {
//						throw new PGPException ("unable to find public key for signed file");
//					}
//				} else {
//					throw new PGPException("message is not a simple encrypted file - type unknown.");
//				}
//
//				if (pbe.isIntegrityProtected()){
//					if (!pbe.verify()){
//						System.err.println("message failed integrity check");
//					} else{
//						System.err.println("message integrity check passed");
//					}
//				} else {
//					System.err.println("no message integrity check");
//				}
//			} else {
//				System.out.println("unable to verify message");
//			}
//		}catch (PGPException e){
//			System.err.println(e);
//			if (e.getUnderlyingException() != null){
//				e.getUnderlyingException().printStackTrace();
//			}
//		}
//	}
//
//
//}
