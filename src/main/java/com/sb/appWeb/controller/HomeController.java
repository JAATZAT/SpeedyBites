package com.sb.appWeb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sb.appWeb.model.DetallePedido;
import com.sb.appWeb.model.Pedido;
import com.sb.appWeb.model.Producto;
import com.sb.appWeb.model.Usuario;
import com.sb.appWeb.service.IUsuarioService;
import com.sb.appWeb.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log= LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;
	
	//para almacenar detalles del pedido
	List<DetallePedido> detalle = new ArrayList<DetallePedido>();
	
	//datos del pedido
	Pedido pedido = new Pedido();
	
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parámetro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetallePedido detallePedido = new DetallePedido();
		Producto producto = new Producto();
		double sumaTotal =0;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		producto=optionalProducto.get();
		
		detallePedido.setCantidad(cantidad);
		detallePedido.setPrecio(producto.getPrecio());
		detallePedido.setNombre(producto.getNombre());
		detallePedido.setTotal(producto.getPrecio()* cantidad);
		detallePedido.setProducto(producto);
		
		//validar que el producto no se añada 2 veces
		Integer idProducto = producto.getId();
		boolean ingresado = detalle.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if(!ingresado) {
			detalle.add(detallePedido);
		}
		
		sumaTotal=detalle.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		pedido.setTotal(sumaTotal);
		model.addAttribute("cart", detalle);
		model.addAttribute("pedido", pedido);
		
		
		return "usuario/carrito";
	}
	
	//quitar productos del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		
		//lista nueva de productos
		List<DetallePedido> nuevoPedido = new ArrayList<DetallePedido>();
		
		for(DetallePedido detallePedido: detalle) {
			if(detallePedido.getProducto().getId()!=id) {
				nuevoPedido.add(detallePedido);
			}
		}
		
		//poner la nueva lista con los productos restantes
		detalle=nuevoPedido;
		
		double sumaTotal=0;
		sumaTotal=detalle.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		pedido.setTotal(sumaTotal);
		model.addAttribute("cart", detalle);
		model.addAttribute("pedido", pedido);
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		
		model.addAttribute("cart", detalle);
		model.addAttribute("pedido", pedido);
		return "/usuario/carrito";
	}
	
	@GetMapping("/pedido")
	public String pedido(Model model ) {
		
		Usuario usuario= usuarioService.findById(1).get();
		
		model.addAttribute("cart", detalle);
		model.addAttribute("pedido", pedido);
		model.addAttribute("usuario", usuario);
		return "usuario/resumenpedido";
	}
	
}
