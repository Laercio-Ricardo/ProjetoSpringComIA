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

    public Transacao salvar(String descricao, BigDecimal valor, String tipo) {
        TipoTransacao tipoEnum = TipoTransacao.valueOf(tipo.toUpperCase());

        // VALIDAÇÃO DE SALDO: Se for despesa, verifica se há saldo suficiente
        if (tipoEnum == TipoTransacao.DESPESA) {
            BigDecimal saldoAtual = calcularSaldo();
            if (valor.compareTo(saldoAtual) > 0) {
                throw new RuntimeException("Saldo insuficiente! Você não possui saldo disponível para esta operação.");
            }
        }

        Transacao transacao = new Transacao();
        transacao.setDescricao(descricao);
        transacao.setValor(valor);
        transacao.setTipo(tipoEnum);
        transacao.setData(LocalDate.now());
        return repository.save(transacao);
    }

    public List<Transacao> listarTodas() {
        return repository.findAll();
    }

    public BigDecimal calcularSaldo() {
        List<Transacao> transacoes = repository.findAll();
        BigDecimal saldo = BigDecimal.ZERO;
        for (Transacao t : transacoes) {
            if (t.getTipo() == TipoTransacao.RECEITA) {
                saldo = saldo.add(t.getValor());
            } else {
                saldo = saldo.subtract(t.getValor());
            }
        }
        return saldo;
    }
}