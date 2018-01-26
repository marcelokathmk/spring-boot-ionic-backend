package com.mkath.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Estado;
import com.mkath.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repository;
	
	public List<Estado> findAll(){
		return repository.findAllByOrderByNome();
	}
}
