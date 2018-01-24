package com.mkath.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.mkath.cursomc.domain.Cliente;
import com.mkath.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{

	@Value("${default.sender}")
	private String sender;
	
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage msg = prepaeSimpleMailMessageFromPedido(obj);
		sendEmail(msg);
	}

	protected SimpleMailMessage prepaeSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(obj.getCliente().getEmail());
		msg.setFrom(sender);
		msg.setSubject("Pedido Confirmado! Código: "+ obj.getId());
		msg.setSentDate(new Date(System.currentTimeMillis()));
		msg.setText(obj.toString());
		return msg;
	}
	
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage msg = prepareNewPasswordEmail(cliente, newPass);
		sendEmail(msg);
	}

	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(cliente.getEmail());
		msg.setFrom(sender);
		msg.setSubject("Solicitação de nova senha");
		msg.setSentDate(new Date(System.currentTimeMillis()));
		msg.setText("Nova senha: "+ newPass);
		return msg;
	}
}
