package com.mkath.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Categoria;
import com.mkath.cursomc.repositories.CategoriaRepository;
import com.mkath.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public Categoria buscar(Integer id) {
		Categoria obj = repository.findOne(id);
		if	(obj == null) {
			throw new ObjectNotFoundException("Categoria não encontrada! id: "+ id);
		}
		return obj;
	}

	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repository.save(obj);
	}
}
