package com.mkath.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.mkath.cursomc.domain.Cliente;
import com.mkath.cursomc.dto.ClienteDTO;
import com.mkath.cursomc.repositories.ClienteRepository;
import com.mkath.cursomc.services.exception.DataIntegretyException;
import com.mkath.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	public Cliente find(Integer id) {
		Cliente obj = repository.findOne(id);
		if	(obj == null) {
			throw new ObjectNotFoundException("Cliente não encontrado! id: "+ id);
		}
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repository.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			repository.delete(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegretyException("Não é possível excluir um cliente que possui pedidos!");
		}
		
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
}
