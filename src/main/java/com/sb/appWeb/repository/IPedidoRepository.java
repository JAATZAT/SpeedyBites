package com.sb.appWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.appWeb.model.Pedido;
import com.sb.appWeb.model.Usuario;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Integer>{

	List<Pedido> findByUsuario (Usuario usuario);
}
