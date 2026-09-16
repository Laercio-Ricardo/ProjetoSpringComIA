package com.exemplo.orcamento.config;

import com.exemplo.orcamento.model.Transacao;
import com.exemplo.orcamento.service.TransacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Configuration
public class TransacaoToolsConfig {

    public record SalvarTransacaoRequest(String descricao, BigDecimal valor, String tipo, String data) {}
    public record RespostaTransacao(String mensagem, Long id) {}

    @Bean
    @Description("Salva uma nova transação financeira (receita ou despesa) no banco de dados.")
    public Function<SalvarTransacaoRequest, RespostaTransacao> salvarTransacaoFunction(TransacaoService transacaoService) {
        return request -> {
            LocalDate dataTransacao = (request.data() != null && !request.data().isBlank())
                    ? LocalDate.parse(request.data())
                    : LocalDate.now();

            Transacao transacao = transacaoService.salvarTransacao(
                    request.descricao(),
                    request.valor(),
                    request.tipo(),
                    dataTransacao
            );

            return new RespostaTransacao("Transação cadastrada com sucesso!", transacao.getId());
        };
    }

    @Bean
    @Description("Lista todas as transações financeiras cadastradas no sistema.")
    public Function<Void, List<Transacao>> listarTransacoesFunction(TransacaoService transacaoService) {
        return unused -> transacaoService.listarTransacoes();
    }
}