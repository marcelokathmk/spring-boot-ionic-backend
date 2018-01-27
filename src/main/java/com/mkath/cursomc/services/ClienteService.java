package com.mkath.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mkath.cursomc.domain.Cidade;
import com.mkath.cursomc.domain.Cliente;
import com.mkath.cursomc.domain.Endereco;
import com.mkath.cursomc.domain.enums.Perfil;
import com.mkath.cursomc.domain.enums.TipoCliente;
import com.mkath.cursomc.dto.ClienteDTO;
import com.mkath.cursomc.dto.ClienteNewDTO;
import com.mkath.cursomc.repositories.CidadeRespository;
import com.mkath.cursomc.repositories.ClienteRepository;
import com.mkath.cursomc.repositories.EnderecoRepository;
import com.mkath.cursomc.security.UserSS;
import com.mkath.cursomc.services.exception.AuthorizationException;
import com.mkath.cursomc.services.exception.DataIntegretyException;
import com.mkath.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private CidadeRespository cidadeRespository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pEncoder;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	public Cliente find(Integer id) {
		UserSS user = UserService.getUserAuthenticated();
		if	(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado!");
		}
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
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.getUserAuthenticated();
		if	(user == null || !user.hasRole(Perfil.ADMIN) || !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente obj = repository.findOne(user.getId());
		if	(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: "+ user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		
		return obj;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), pEncoder.encode(objDTO.getSenha()));
		Cidade cid = cidadeRespository.findOne(objDTO.getCidadeId());
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if	(objDTO.getTelefone2() != null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		if	(objDTO.getTelefone3() != null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		return cli;
	}

	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repository.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}
	
	public URI uploadProfilePicture(MultipartFile multi) {
		UserSS user = UserService.getUserAuthenticated();
		if	(user == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		BufferedImage jpgImage = imageService.getJPGImageFromFile(multi);
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image"); 
	}
	
	
}
