package com.mkath.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.mkath.cursomc.domain.Cliente;
import com.mkath.cursomc.dto.ClienteDTO;
import com.mkath.cursomc.repositories.ClienteRepository;
import com.mkath.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO>{

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public void initialize(ClienteUpdate arg0) {
	}

	@Override
	public boolean isValid(ClienteDTO arg0, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<FieldMessage>();
		
		Cliente cli = clienteRepository.findByEmail(arg0.getEmail());
		if	(cli != null && !cli.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for	(FieldMessage fe: list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fe.getMessage()).addPropertyNode(fe.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
	
	

	
}
