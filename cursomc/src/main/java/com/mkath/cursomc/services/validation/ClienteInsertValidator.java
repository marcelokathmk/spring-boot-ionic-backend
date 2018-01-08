package com.mkath.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.mkath.cursomc.domain.enums.TipoCliente;
import com.mkath.cursomc.dto.ClienteNewDTO;
import com.mkath.cursomc.resources.exception.FieldMessage;
import com.mkath.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO>{

	@Override
	public void initialize(ClienteInsert arg0) {		
	}

	@Override
	public boolean isValid(ClienteNewDTO arg0, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<FieldMessage>();
		
		if	(arg0.getTipo().equals(TipoCliente.PESSOA_FISICA.getCod()) &&
				!BR.isValidCPF(arg0.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if	(arg0.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCod()) &&
				!BR.isValidCNPJ(arg0.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		for	(FieldMessage fe: list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fe.getMessage()).addPropertyNode(fe.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}

	
}
