package com.sb.appWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.appWeb.model.Pedido;
import com.sb.appWeb.repository.IPedidoRepository;

@Service
public class PedidoServiceImpl implements IPedidoService{

	@Autowired
	private IPedidoRepository pedidoRepository;
	
	@Override
	public Pedido save(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

}
