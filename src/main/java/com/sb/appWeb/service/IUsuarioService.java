package com.sb.appWeb.service;

import java.util.Optional;

import com.sb.appWeb.model.Usuario;

public interface IUsuarioService {

	Optional<Usuario> findById(Integer id);
	Usuario save(Usuario usuario);
	Optional<Usuario> findByEmail(String email);
	
}
