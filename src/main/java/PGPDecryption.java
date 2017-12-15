//import com.sun.tools.javac.code.Attribute;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.security.Security;
//import java.util.Properties;
//
//public class PGPDecryption {
//
//	/**
//	 *
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//
//		Properties prop=new Properties();
//		try {
//			prop.load(new FileInputStream("config.prop"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		FileInputStream keyOut=null;
//		FileOutputStream out =null;
//		Security.addProvider(new BouncyCastleProvider());
//		try {
//			System.out.println(prop.getProperty(Constant.PRIVATE_KEY));
//			keyOut = new FileInputStream(prop.getProperty(Constant.PRIVATE_KEY));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			KeyBasedFileProcessorUtil.decryptFile(new  FileInputStream(prop.getProperty(Constant.ENCRYPT_FILE_PATH)), keyOut, prop.getProperty(Constant.PRIVATE_FILE_PASS).toCharArray(), prop.getProperty(Constant.DECRYPT_FILE_OUTPUT_PATH));
//			System.out.println("Decrypted File created with name of "+prop.getProperty(Constant.DECRYPT_FILE_OUTPUT_PATH));
//		} catch (NoSuchProviderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//	}
//
//}