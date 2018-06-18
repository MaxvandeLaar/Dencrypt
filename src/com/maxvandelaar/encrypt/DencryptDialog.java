package com.maxvandelaar.encrypt;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

import javax.swing.*;
import java.awt.event.*;

public class DencryptDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonEncrypt;
	private JButton buttonCancel;
	private JButton buttonDecrypt;
	private JPasswordField passwordField;

	public DencryptDialog(Editor editor) {
		setSize(300, 200);
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonEncrypt);

		final Project project = editor.getProject();
		//Access document, caret, and selection
		final Document document = editor.getDocument();
		final SelectionModel selectionModel = editor.getSelectionModel();


		buttonEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onEncrypt(document, project, selectionModel);
			}
		});

		buttonDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onDecrypt(document, project, selectionModel);
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}


	private void onEncrypt(Document document, Project project, SelectionModel selectionModel) {
		final int start = selectionModel.getSelectionStart();
		final int end = selectionModel.getSelectionEnd();
		final String password = new String(passwordField.getPassword());
		String selectedText = document.getText(new TextRange(start, end));
		//Making the replacement
		if (!selectedText.isEmpty()) {
			WriteCommandAction.runWriteCommandAction(project, () ->
					document.replaceString(start, end, com.maxvandelaar.encrypt.Dencrypt.encrypt(selectedText, password))
			);
		}
		selectionModel.removeSelection();

		dispose();
	}

	private void onDecrypt(Document document, Project project, SelectionModel selectionModel) {
		final int start = selectionModel.getSelectionStart();
		final int end = selectionModel.getSelectionEnd();
		final String password = new String(passwordField.getPassword());

		String selectedText = document.getText(new TextRange(start, end));
		if (!selectedText.isEmpty()) {
			WriteCommandAction.runWriteCommandAction(project, () ->
					document.replaceString(start, end, com.maxvandelaar.encrypt.Dencrypt.decrypt(selectedText, password))
			);
		}
		selectionModel.removeSelection();

		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	public static void main(String[] args) {
		DencryptDialog dialog = new DencryptDialog(null);
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
