package com.sb.appWeb.service;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Pedido> findAll() {
		return pedidoRepository.findAll();
	}
	
	
	public String generarNumeroPedido() {
		int numero=0;
		String numeroConcatenado="";
		
		List<Pedido> pedidos=findAll();
		
		List<Integer> numeros=new ArrayList<Integer>();
		
		pedidos.stream().forEach(p -> numeros.add(Integer.parseInt(p.getNumero())));
		
		if (pedidos.isEmpty()) {
			numero=1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if (numero<10) { //0000001000
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="00000000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}
		
		return numeroConcatenado;
	}

}
