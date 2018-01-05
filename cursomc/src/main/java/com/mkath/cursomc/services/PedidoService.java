package com.mkath.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Pedido;
import com.mkath.cursomc.repositories.PedidoRepository;
import com.mkath.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;
	
	public Pedido buscar(Integer id) {
		Pedido obj = repository.findOne(id);
		if	(obj == null) {
			throw new ObjectNotFoundException("Pedido não encontrado! id: "+ id);
		}
		return obj;
	}
}
