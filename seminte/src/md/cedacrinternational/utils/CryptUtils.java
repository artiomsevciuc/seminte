package md.cedacrinternational.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptUtils {
	public static String encryptPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] cryptedBytes = digest.digest(charArrayToByteArray(password.toCharArray()));
		return byteArrayToHex(cryptedBytes);

	}

	private final static byte[] charArrayToByteArray(char[] characters) {
		byte[] bytes = new byte[characters.length];
		for (int i = 0; i < characters.length; i++) {
			bytes[i] = (byte) characters[i];
		}
		return bytes;
	}

	private final static String byteArrayToHex(byte[] bytes) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			buffer.append(String.format("%02x", 0xFF & bytes[i]));
		}
		return buffer.toString();
	}
}
