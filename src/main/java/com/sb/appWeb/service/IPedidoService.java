package com.sb.appWeb.service;

import java.util.List;
import java.util.Optional;

import com.sb.appWeb.model.Pedido;
import com.sb.appWeb.model.Usuario;

public interface IPedidoService {

	List<Pedido> findAll();

	Optional<Pedido> findById(Integer id);
	
	Pedido save(Pedido pedido);

	String generarNumeroPedido();

	List<Pedido> findByUsuario(Usuario usuario);
}
