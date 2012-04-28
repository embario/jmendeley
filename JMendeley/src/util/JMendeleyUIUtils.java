package util;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class JMendeleyUIUtils {

	private static JFrame _verificationCodeFrame = new JFrame();
	public static final String MENDELEY_ICON = "./img/mendeley.png";
	
	
	/**
	 * Pop-up a message dialog given the message and title as a String, and an integer as a message type.
	 * @param message
	 */
	public static void showMessageDialog (String message, String title, int messageType){
		
		JOptionPane.showMessageDialog(_verificationCodeFrame, message, title, messageType);
	}
	
	/**
	 * Pop-up a confirm YES/NO dialog given the message and title as a String.
	 * @param message
	 * @param title
	 * @param messageType
	 * @return
	 */
	public static int showConfirmYesNoDialog (String message, String title){
		
		return JOptionPane.showConfirmDialog(_verificationCodeFrame, message, title, JOptionPane.YES_NO_OPTION);
	}
	
	public static String popupVerificationCodeDialog(String authURL) {
		 
			//Prompt the user for the verification code.
			ImageIcon icon = new ImageIcon (MENDELEY_ICON);
			String verificationCode = (String) JOptionPane.showInputDialog(_verificationCodeFrame, 
					"Go to the URL shown below and enter the verification code in the text field.", 
					"Enter Mendeley Verification Code",
					JOptionPane.PLAIN_MESSAGE,
					icon,
					null,
					authURL);
			
			//User pushed cancel intending to leave the application.
			if (verificationCode == null){
				
				int answer = JOptionPane.showConfirmDialog(_verificationCodeFrame, "Are you sure you want to exit JMendeley?", "Exit JMendeley", JOptionPane.YES_NO_OPTION);
				if (answer != 1)
					return null;
				else
					return popupVerificationCodeDialog(authURL);
			}
			
			else
				return verificationCode;
		 
		 }//end popup verification method
}
