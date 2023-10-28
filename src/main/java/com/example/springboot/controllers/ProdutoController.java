package com.example.springboot.controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dtos.ProdutoRecordDto;
import com.example.springboot.models.ProdutoModel;
import com.example.springboot.repositories.ProdutoRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.validation.Valid;


@RestController
public class ProdutoController {

	@Autowired
	ProdutoRepository produtoRepository;
	
	@PostMapping("/produtos")
	public ResponseEntity<ProdutoModel> Novo(@RequestBody @Valid ProdutoRecordDto dto) {
		
		var produtoModel = new ProdutoModel();
		BeanUtils.copyProperties(dto, produtoModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produtoModel));
		
	}
	
	@GetMapping("/produtos")
	public ResponseEntity<List<ProdutoModel>> ListarTodos() {
		
		List<ProdutoModel> produtos = produtoRepository.findAll();
		
		if (!produtos.isEmpty()) {
			for (var produto : produtos) {
				UUID id = produto.getIdProduto();
				produto.add(linkTo(methodOn(ProdutoController.class).Buscar(id)).withSelfRel());
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(produtos);
	}
	
	@GetMapping("/produto/{id}")
	public ResponseEntity<Object> Buscar(@PathVariable(value="id") UUID id) {
		
		Optional<ProdutoModel> produto = produtoRepository.findById(id);
		
		if (produto.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
		
		return ResponseEntity.status(HttpStatus.OK).body(produto.get());
	}
	
	@PutMapping("produto/{id}")
	public ResponseEntity<Object> Atualizar(@PathVariable(value="id") UUID id, @RequestBody @Valid ProdutoRecordDto dto) {
		
		Optional<ProdutoModel> produtoO = produtoRepository.findById(id);
		
		if (produtoO.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
		
		var produtoModel = produtoO.get();
		
		BeanUtils.copyProperties(dto, produtoModel);		
		
		return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produtoModel));
	}
	
	@DeleteMapping("produto/{id}") 
	public ResponseEntity<Object> Excluir(@PathVariable(value="id") UUID id) {
		
		Optional<ProdutoModel>  produtoO = produtoRepository.findById(id);
		
		if (produtoO.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
		
		produtoRepository.delete(produtoO.get());	
		
		return ResponseEntity.status(HttpStatus.OK).body("Produto excluido com sucesso");
	}
	
	
}










