package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domains.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria cat = service.find(id);

		return ResponseEntity.ok(cat);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll( ) {
		List<CategoriaDTO> list = service.findAll( ).stream().map( cat -> new CategoriaDTO( cat ) ).collect(Collectors.toList());
		
		return ResponseEntity.ok( list );
	}
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction ) {
		Page<CategoriaDTO> list = service.findPage(page, linesPerPage, orderBy, direction).map( cat -> new CategoriaDTO( cat ));
		
		return ResponseEntity.ok( list );
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> insert( @Valid @RequestBody CategoriaDTO dto ) {
		Categoria cat = service.fromDTO( dto );
		cat = service.insert( cat );
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand( cat.getId() ).toUri();
		
		return ResponseEntity.created( uri ).build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Categoria> update( @PathVariable Integer id, @Valid @RequestBody CategoriaDTO dto ) {
		Categoria cat = service.find(id);
		cat.setNome( dto.getNome() );
		
		cat = service.update( cat );
		
		return ResponseEntity.ok( cat );
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete( @PathVariable Integer id ) {
		service.delete( id );
		return ResponseEntity.ok( ).build();
	}
}
