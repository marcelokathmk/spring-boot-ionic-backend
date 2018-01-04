package com.mkath.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Categoria;
import com.mkath.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public Categoria buscar(Integer id) {
		Categoria obj = repository.findOne(id);
		return obj;
	}
}
