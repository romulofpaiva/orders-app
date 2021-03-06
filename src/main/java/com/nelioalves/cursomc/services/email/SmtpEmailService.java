package com.nelioalves.cursomc.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger( SmtpEmailService.class );
	
	@Autowired
	private MailSender mailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOGGER.info("Enviando email...");
		LOGGER.info(msg.toString());
		mailSender.send( msg );
		LOGGER.info("Email enviado");
	}

}
