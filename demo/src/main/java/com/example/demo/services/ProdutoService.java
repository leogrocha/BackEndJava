package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.model.Produto;
import com.example.demo.model.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.shared.ProdutoDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    
    /**
     * Método para retornar uma lista de produtos
     * @return Lista de produtos.
     */
    public List<ProdutoDTO> obterTodos(){
        // Colocar regra de négocio aqui caso tenha.
        
        // Retorna uma lista de Produto model.
        List<Produto> produtos = produtoRepository.findAll();
        
        return produtos.stream()
        .map(produto -> new ModelMapper()
        .map(produto, ProdutoDTO.class))
        .collect(Collectors.toList());
    }

    /**
     * Método que retorna o produto encontrado pelo seu ID.
     * @param id do produto que será localizado.
     * @return Retorna um produto caso ele seja encontrado.
     */
    public Optional<ProdutoDTO> obterPorId(Integer id){
            
        // Obterndo optional de produto pelo id
        Optional<Produto> produto = produtoRepository.findById(id);

        // se não encontrar, lança exception
        if(produto.isEmpty())
            throw new ResourceNotFoundException("Produto com id: " + id + " não encontrado.");

        // convertendo o meu optional de produto em um produtoDTO
        ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);
        
        // criando e retornando um optional de produtoDTO
        return Optional.of(dto);
    }

    public ProdutoDTO adicionar(ProdutoDTO produtoDto){
        
        // Removendo o id para conseguir fazer o cadastro.
        produtoDto.setId(null);

        // Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // Converter o nosso produtoDto em um Produto
        Produto produto = mapper.map(produtoDto, Produto.class);

        // Salvar o Produto do banco
        produto = produtoRepository.save(produto);

        produtoDto.setId(produto.getId());

        // Retornar o ProdutoDto atualizado.
        return produtoDto;
    }

    public void deletar(Integer id){
        
        // Verificar se o produto existe.
        Optional<Produto> produto = produtoRepository.findById(id);

        // Se não existir lança uma exception
        if(produto.isEmpty()){
            throw new ResourceNotFoundException("Não foi possível deletar o produto com o id: " + id + 
            " - Produto não existe.");
        }
        
        produtoRepository.deleteById(id);
    }

    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDto){
        
       // Passar o id para o produtoDto
        produtoDto.setId(id);

       // Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

       // Converter o DTO em um Produto.
        Produto produto = mapper.map(produtoDto, Produto.class);

       // Atualizar o produto no banco de dados.
       produtoRepository.save(produto);

       // Retornar o produto Dto atualizado.
       return produtoDto;
    }

}
