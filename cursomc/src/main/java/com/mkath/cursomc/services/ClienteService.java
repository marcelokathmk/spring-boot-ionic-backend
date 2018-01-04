package com.mkath.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Cliente;
import com.mkath.cursomc.repositories.ClienteRepository;
import com.mkath.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	public Cliente buscar(Integer id) {
		Cliente obj = repository.findOne(id);
		if	(obj == null) {
			throw new ObjectNotFoundException("Cliente não encontrado! id: "+ id);
		}
		return obj;
	}
}
