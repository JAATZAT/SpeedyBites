package com.sb.appWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.appWeb.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

	
}
