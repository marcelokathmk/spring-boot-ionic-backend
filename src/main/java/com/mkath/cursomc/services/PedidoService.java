package com.mkath.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mkath.cursomc.domain.Cliente;
import com.mkath.cursomc.domain.ItemPedido;
import com.mkath.cursomc.domain.PagamentoComBoleto;
import com.mkath.cursomc.domain.Pedido;
import com.mkath.cursomc.domain.enums.EstadoPagamento;
import com.mkath.cursomc.repositories.ClienteRepository;
import com.mkath.cursomc.repositories.ItemPedidoRepository;
import com.mkath.cursomc.repositories.PagamentoRepository;
import com.mkath.cursomc.repositories.PedidoRepository;
import com.mkath.cursomc.repositories.ProdutoRepository;
import com.mkath.cursomc.security.UserSS;
import com.mkath.cursomc.services.exception.AuthorizationException;
import com.mkath.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido buscar(Integer id) {
		Pedido obj = repository.findOne(id);
		if	(obj == null) {
			throw new ObjectNotFoundException("Pedido não encontrado! id: "+ id);
		}
		System.out.println(obj);
		return obj;
	}

	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteRepository.findOne(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		
		if	(pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgto, pedido.getInstante());
		}
		pedido = repository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		
		for	(ItemPedido ip: pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoRepository.findOne(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.save(pedido.getItens());
		emailService.sendOrderConfirmationEmail(pedido);
		return pedido;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.getUserAuthenticated();
		if	(user == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteRepository.findOne(user.getId());
		return repository.findByCliente(cliente, pageRequest);
	}
}
