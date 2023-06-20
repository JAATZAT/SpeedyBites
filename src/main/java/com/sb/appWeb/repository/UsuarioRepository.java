package com.sb.appWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.appWeb.model.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	
}
