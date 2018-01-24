package com.mkath.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.mkath.cursomc.domain.Cliente;
import com.mkath.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
