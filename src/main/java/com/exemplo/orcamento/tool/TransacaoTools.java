package com.exemplo.orcamento.tool;

import com.exemplo.orcamento.model.Transacao;
import com.exemplo.orcamento.service.TransacaoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class TransacaoTools {

    private final TransacaoService transacaoService;

    public TransacaoTools(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    public record RespostaTransacao(String mensagem, Long id) {}

    @Tool(description = "Salva uma nova transação financeira (receita ou despesa) no banco de dados.")
    public RespostaTransacao salvarTransacao(
            @ToolParam(description = "Descrição da transação (ex: supermercado, salário)") String descricao,
            @ToolParam(description = "Valor numérico da transação") BigDecimal valor,
            @ToolParam(description = "Tipo da transação: RECEITA ou DESPESA") String tipo,
            @ToolParam(description = "Data da transação no formato YYYY-MM-DD (opcional)") String data) {

        LocalDate dataTransacao = (data != null && !data.isBlank())
                ? LocalDate.parse(data)
                : LocalDate.now();

        Transacao transacao = transacaoService.salvarTransacao(descricao, valor, tipo, dataTransacao);
        return new RespostaTransacao("Transação cadastrada com sucesso!", transacao.getId());
    }

    @Tool(description = "Lista todas as transações financeiras cadastradas no sistema.")
    public List<Transacao> listarTransacoes() {
        return transacaoService.listarTransacoes();
    }
}