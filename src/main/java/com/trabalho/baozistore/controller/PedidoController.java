package com.trabalho.baozistore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trabalho.baozistore.model.Cliente;
import com.trabalho.baozistore.model.Produto;
import com.trabalho.baozistore.model.Pedido;

import com.trabalho.baozistore.repository.ClienteRepository;
import com.trabalho.baozistore.repository.ProdutoRepository;
import com.trabalho.baozistore.repository.PedidoRepository;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public ResponseEntity<Pedido> cadastrar(@RequestBody Pedido pedido) {

        if (pedido.getCliente() == null ||
            pedido.getProduto() == null ||
            pedido.getQuantidade() == null ||
            pedido.getQuantidade() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Long clienteId = pedido.getCliente().getId();
        Long produtoId = pedido.getProduto().getId();

        if (clienteId == null || produtoId == null) {
            return ResponseEntity.badRequest().build();
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElse(null);

        Produto produto = produtoRepository.findById(produtoId)
                .orElse(null);

        if (cliente == null || produto == null) {
            return ResponseEntity.badRequest().build();
        }

        pedido.setId(null);
        pedido.setCliente(cliente);
        pedido.setProduto(produto);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return ResponseEntity.ok(pedidoSalvo);
    }
    
    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}