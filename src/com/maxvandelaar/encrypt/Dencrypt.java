package com.maxvandelaar.encrypt;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Dencrypt extends AnAction {
	@Override
	public void actionPerformed(AnActionEvent e) {
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);

		DencryptDialog dencryptDialog = new DencryptDialog(editor);
		dencryptDialog.setTitle("Encrypt/Decrypt Selection");
		dencryptDialog.setVisible(true);

	}

	public static String decrypt(String strEncrypted, String strKey) {
		String strData = strEncrypted;

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(strEncrypted.getBytes()));

			strData = new String(decrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	public static String encrypt(String strClearText, String strKey) {
		String strData = strClearText;

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted = cipher.doFinal(strClearText.getBytes("UTF-8"));
			strData = Base64.encodeBase64String(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}
}
