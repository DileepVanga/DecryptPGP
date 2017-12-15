import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.examples.ByteArrayHandler;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchProviderException;
import java.security.Security;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.KeySpec;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;


public class Symmetric {


	private static final String FOLDER = "/Users/dileepvanga/Desktop/encrypted/";
	private static final String PASS = "Our admissions management and enrollment marketing automation";

	public static void main(String[] args) throws IOException, PGPException, NoSuchProviderException {

		if (!isRestrictedCryptography()) {
			System.out.println("Cryptography restrictions removal not needed");
			return;
		}

		try {
                /*
                 * Do the following, but with reflection to bypass access checks:
                 *
                 * JceSecurity.isRestricted = false;
                 * JceSecurity.defaultPolicy.perms.clear();
                 * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
                 */
			final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
			final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
			final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

			final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
			isRestrictedField.setAccessible(true);
			final Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(isRestrictedField, isRestrictedField.getModifiers() & ~Modifier.FINAL);
			isRestrictedField.set(null, false);

			final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
			defaultPolicyField.setAccessible(true);
			final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

			final Field perms = cryptoPermissions.getDeclaredField("perms");
			perms.setAccessible(true);
			((Map<?, ?>) perms.get(defaultPolicy)).clear();

			final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
			instance.setAccessible(true);
			defaultPolicy.add((Permission) instance.get(null));

			System.out.println("Successfully removed cryptography restrictions");
		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}


		System.out.println("================Decryption Started=================");

		Security.addProvider(new BouncyCastleProvider());

		File encryptedFile = new File(FOLDER + "eapxap2015testfile.txt.pgp");
		byte[]  encryptedByteArray = FileUtils.readFileToByteArray(encryptedFile);

		byte[] decryptedByteArray = ByteArrayHandler.decrypt(encryptedByteArray, PASS.toCharArray());
		String decryptedString = new String(decryptedByteArray);

		System.out.println(decryptedString);

		System.out.println("================Decryption Finished=================");

//		byte[] encryptedAgain = ByteArrayHandler.encrypt(decryptedByteArray, PASS.toCharArray(), "foobar.txt", SymmetricKeyAlgorithmTags.AES_256, true);
//		String encryptedAgainString = new String(encryptedAgain);
//		System.out.println(encryptedAgainString);
//
//
//
//
//
//		byte[] decryptedAgainByteArray = ByteArrayHandler.decrypt(encryptedAgain, PASS.toCharArray());
//		String decrypteAgaindString = new String(decryptedAgainByteArray);
//		System.out.println(decrypteAgaindString);


	}

	private static boolean isRestrictedCryptography() {
		// This simply matches the Oracle JRE, but not OpenJDK.
		return "Java(TM) SE Runtime Environment".equals(System.getProperty("java.runtime.name"));
	}

}