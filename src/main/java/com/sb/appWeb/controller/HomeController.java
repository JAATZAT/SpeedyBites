package com.sb.appWeb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.sb.appWeb.service.IDetallePedidoService;
import com.sb.appWeb.service.IPedidoService;
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
	
	@Autowired
	private IPedidoService pedidoService;
	
	@Autowired
	private IDetallePedidoService detallePedidoService;
	
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
	
	//guardar el pedido
	@GetMapping("/savePedido")
	public String savePedido() {
		Date fechaCreacion = new Date();
		pedido.setFechaCreacion(fechaCreacion);
		pedido.setNumero(pedidoService.generarNumeroPedido());
		
		//usuario
		Usuario usuario = usuarioService.findById(1).get();
		
		pedido.setUsuario(usuario);
		pedidoService.save(pedido);
		
		//guardar detalles
		for (DetallePedido dt:detalle) {
			dt.setPedido(pedido);
			detallePedidoService.save(dt);
		}
		// limpiar lista y pedido
		pedido = new Pedido();
		detalle.clear();
		
		return "redirect:/";
	}
	
	@PostMapping("/search")
	public String searchProducto(@RequestParam String nombre, Model model) {
		log.info("Nombre del producto: {}",nombre);
		List<Producto> productos = productoService.findAll().stream().filter(p ->p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		
		return "usuario/home";
	}
}
