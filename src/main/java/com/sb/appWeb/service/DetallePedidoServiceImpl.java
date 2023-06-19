package com.sb.appWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.appWeb.model.DetallePedido;
import com.sb.appWeb.repository.IDetallePedidoRepository;

@Service
public class DetallePedidoServiceImpl implements IDetallePedidoService{

	@Autowired
	private IDetallePedidoRepository detallePedidoRepository;
	
	@Override
	public DetallePedido save(DetallePedido detallePedido) {
		return detallePedidoRepository.save(detallePedido);
	}

}
