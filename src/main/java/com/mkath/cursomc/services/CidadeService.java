package com.mkath.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Cidade;
import com.mkath.cursomc.repositories.CidadeRespository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRespository repository;
	
	public List<Cidade> findByEstado(Integer estadoId){
		return repository.findCidades(estadoId); 
	}
}
