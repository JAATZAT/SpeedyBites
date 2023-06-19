package com.sb.appWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.appWeb.model.Pedido;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Integer>{

}
