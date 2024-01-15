package com.javatechie.chatgptbot.config;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class FlugEncryptor {
	public static String paswEncry(String planText) {
		return Base64.getEncoder().encodeToString(FlugEncryptor.paswDataEncry(planText));
	}

	public static String paswEncryUrlencode(String planText) {
		return FlugEncryptor.strBase64UrlEncodeBytes(FlugEncryptor.paswDataEncry(planText));
	}

	public static byte[] paswDataEncry(String planText) {
		try {
			String salt = System.getProperty("jasypt.encryptor.sha");
			MessageDigest md = MessageDigest.getInstance(salt);
			md.update(planText.getBytes("utf-8"));
			byte byteData[] = md.digest();
			return byteData;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// Raw Position
	public static String paswRawDataEncry(String planText) {
		try {
			String salt = System.getProperty("jasypt.encryptor.sha");
			MessageDigest md = MessageDigest.getInstance(salt);
			md.update(planText.getBytes("utf-8"));
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return byteData.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static String strEncryptor(String value) {
		String salt = System.getProperty("jasypt.encryptor.password");
		String algo = System.getProperty("jasypt.encryptor.algorithm");
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(salt);
		config.setAlgorithm(algo);
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		// return encryptor.encrypt(value);

		return Base64.getUrlEncoder().encodeToString(encryptor.encrypt(value).getBytes());
	}

	public static String strDecryptor(String value) {
		String basevalue = new String(Base64.getUrlDecoder().decode(value));
		String salt = System.getProperty("jasypt.encryptor.password");
		String algo = System.getProperty("jasypt.encryptor.algorithm");
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(salt);
		config.setAlgorithm(algo);
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);

		return encryptor.decrypt(basevalue);
	}

	public static String strBase64UrlEncode(String value) {
		return Base64.getUrlEncoder().encodeToString(value.getBytes());
	}

	public static String strBase64UrlEncodeBytes(byte[] value) {
		return Base64.getUrlEncoder().encodeToString(value);
	}

	public static String strBase64UrlDecode(String value) {
		return new String(Base64.getUrlDecoder().decode(value));
	}

	public static String strFlugEncryptor(String value) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String salt = System.getProperty("jasypt.encryptor.password");
		String shaKey = getShaKey(salt);

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(shaKey.getBytes());
		byte[] byteKey = md.digest();

		String deiv = "a0VubWdRTXhTQzhaaE9QSTBjVkJhQms0dGhTR1ZUYU5Wa2NLK3Q1TjlqbXFNM20vaS9WVVB3PT0=";
		deiv = FlugEncryptor.strDecryptor(deiv);

		IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(deiv));
		SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] original = cipher.doFinal(value.getBytes());

		return new String(Base64.getEncoder().encode(original));
	}

	public static String strFlugDecryptor(String value) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String basevalue = new String(Base64.getDecoder().decode(value));
		String spreg = ":";

		String[] splited = basevalue.split(spreg);
		if (splited.length != 2) {
			return "";
		}

		String detext = splited[0];
		String deiv = splited[1];
		deiv = FlugEncryptor.strDecryptor(deiv);

		IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(deiv));
		String salt = System.getProperty("jasypt.encryptor.password");

		String shaKey = getShaKey(salt);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(shaKey.getBytes());
		byte[] byteKey = md.digest();

		SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] original = cipher.doFinal(Base64.getDecoder().decode(detext));
		String spsp = new String(original);
		splited = spsp.split(spreg);

		return new String(splited[1]);
	}

	private static String getShaKey(String text) throws NoSuchAlgorithmException {
		String ago = System.getProperty("jasypt.encryptor.sha");
		MessageDigest md = MessageDigest.getInstance(ago);
		md.update(text.getBytes());
		return bytesToHex(md.digest());
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
}