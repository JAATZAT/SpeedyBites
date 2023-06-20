package com.sb.appWeb.service;

import java.util.List;

import com.sb.appWeb.model.Pedido;

public interface IPedidoService {

	List<Pedido> findAll();
	
	Pedido save(Pedido pedido);
	
	String generarNumeroPedido();
}
