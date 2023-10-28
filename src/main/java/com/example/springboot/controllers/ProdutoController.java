package com.example.springboot.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.dtos.ProdutoRecordDto;
import com.example.springboot.models.ProdutoModel;
import com.example.springboot.repositories.ProdutoRepository;

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
}
