package com.exemplo.orcamento.service;

import com.exemplo.orcamento.model.TipoTransacao;
import com.exemplo.orcamento.model.Transacao;
import com.exemplo.orcamento.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    // Método para salvar uma nova transação
    public Transacao salvarTransacao(String descricao, BigDecimal valor, String tipo, LocalDate data) {
        TipoTransacao tipoEnum = TipoTransacao.valueOf(tipo.toUpperCase());
        LocalDate dataTransacao = (data != null) ? data : LocalDate.now();

        Transacao transacao = new Transacao(descricao, valor, tipoEnum, dataTransacao);
        return repository.save(transacao);
    }

    // Método para listar todas as transações cadastradas
    public List<Transacao> listarTransacoes() {
        return repository.findAll();
    }
}