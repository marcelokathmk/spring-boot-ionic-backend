package com.mkath.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Categoria;
import com.mkath.cursomc.repositories.CategoriaRepository;
import com.mkath.cursomc.services.exception.DataIntegretyException;
import com.mkath.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public Categoria find(Integer id) {
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

	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repository.save(obj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repository.delete(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegretyException("Não é possível excluir uma categoria que possui produtos!");
		}
		
	}

	public List<Categoria> findAll() {
		return repository.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
}
