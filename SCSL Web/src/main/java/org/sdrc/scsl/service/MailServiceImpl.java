package org.sdrc.scsl.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.sdrc.scsl.model.web.MailModel;
import org.sdrc.scsl.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 *This class contains method which will be used for sending mails
 */
@Service
public class MailServiceImpl implements MailService {
	
	
	@Autowired
	private ResourceBundleMessageSource notification;

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.MailService#sendMail(org.sdrc.scsl.model.web.MailModel)
	 * This method will send an email
	 */
	@Override
	public String sendMail(MailModel mail) throws Exception {
		Properties props = new Properties();
		props.put(
				notification.getMessage(Constants.Web.SMTP_HOST_KEY, null, null),
				notification.getMessage(Constants.Web.SMTP_HOST, null, null));
		props.put(notification.getMessage(Constants.Web.SOCKETFACTORY_PORT_KEY,
				null, null), notification.getMessage(
				Constants.Web.SOCKETFACTORY_PORT, null, null));
		props.put(notification.getMessage(Constants.Web.SOCKETFACTORY_CLASS_KEY,
				null, null), notification.getMessage(
				Constants.Web.SOCKETFACTORY_CLASS, null, null));
		props.put(
				notification.getMessage(Constants.Web.SMTP_AUTH_KEY, null, null),
				notification.getMessage(Constants.Web.SMTP_AUTH, null, null));
		props.put(
				notification.getMessage(Constants.Web.SMTP_PORT_KEY, null, null),
				notification.getMessage(Constants.Web.SMTP_PORT, null, null));

		javax.mail.Session session = javax.mail.Session.getDefaultInstance(
				props, new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(notification
								.getMessage(Constants.Web.AUTHENTICATION_USERID,
										null, null), notification.getMessage(
								Constants.Web.AUTHENTICATION_PASSWORD, null, null));
					}
				});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(notification.getMessage(
				Constants.Web.AUTHENTICATION_USERID, null, null)));

		// adding "to"
		List<String> toList = mail.getToEmailIds();
		String toAddress = "";

		for (String to : toList) {
			toAddress += to;
			if (toList.size() > 1) {
				toAddress += ",";
			}
		}

		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toAddress));

		// adding "cc"
		List<String> ccList = mail.getCcEmailIds();
		if (null != ccList && !ccList.isEmpty()) {
			String ccAddress = "";

			for (String cc : ccList) {
				ccAddress += cc;
				if (ccList.size() > 1) {
					ccAddress += ",";
				}
			}

			message.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(ccAddress));
		}

		

		message.setSubject(mail.getSubject());

		//set mail message
		
		String mailMessageBody = null != mail.getMessage() ? mail.getMessage() : "";

		String msg = "<html>"
				+ "<body><b style=\"\r\n" + 
				"    font-weight: normal;\r\n" + 
				"\">Dear " + mail.getToUserName() + ",<br><br>"
		// + "NOTIFICATION DETAILS:" + "\n" + "Message : " + mail.getMsg()
				+ mailMessageBody + "<br><br>" + "Regards," + "<br>" + mail.getFromUserName()+"</b>"
						+ "	</body>"
						+ "</html>";
		
		// for attaching files and send it through email	
		if(mail.getAttachments()==null)
		{
			message.setContent(msg,"text/html");
		}
		
		else if(mail.getAttachments().size() > 0) {
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msg,"text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			
			Iterator<Entry<String, String>> it = mail.getAttachments().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, String> pair = it.next();
		        
		        String path =pair.getValue();
				String name =pair.getKey();
				
				messageBodyPart = new MimeBodyPart();
				String filename = path +  name;
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(name);
				multipart.addBodyPart(messageBodyPart);
		        
		    }
			
			message.setContent(multipart);
			
		} 
		
		Transport.send(message);
		return "Done";
	}

}
