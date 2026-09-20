package com.exemplo.orcamento.controller;

import com.exemplo.orcamento.model.Transacao;
import com.exemplo.orcamento.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/orcamento")
@CrossOrigin(origins = "*")
public class OrcamentoController {

    private final TransacaoService service;

    public OrcamentoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping("/comando")
    public ResponseEntity<Transacao> processarComando(@RequestBody(required = false) Object rawBody) {
        String texto = "";

        if (rawBody instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) rawBody;
            String[] chavesPossiveis = {"texto", "mensagem", "message", "prompt", "comando"};
            for (String chave : chavesPossiveis) {
                if (map.containsKey(chave) && map.get(chave) != null) {
                    texto = String.valueOf(map.get(chave));
                    break;
                }
            }
            if (texto.isEmpty() && !map.isEmpty()) {
                for (Object val : map.values()) {
                    if (val != null) {
                        texto = String.valueOf(val);
                        break;
                    }
                }
            }
        } else if (rawBody instanceof String) {
            texto = (String) rawBody;
        }

        if (texto == null || texto.trim().isEmpty()) {
            texto = "Transação bancária";
        }

        String textoLower = texto.toLowerCase();

        // Identifica se é despesa (saída) ou receita (entrada)
        String tipo = "RECEITA";
        if (textoLower.contains("despesa") || textoLower.contains("supermercado") || textoLower.contains("gastei") ||
                textoLower.contains("compra") || textoLower.contains("pagar") || textoLower.contains("paguei") ||
                textoLower.contains("enviado") || textoLower.contains("transferência") || textoLower.contains("pix enviado")) {
            tipo = "DESPESA";
        }

        BigDecimal valor = BigDecimal.ZERO;
        Matcher matcher = Pattern.compile("(\\d+([.,]\\d+)?)").matcher(texto);
        if (matcher.find()) {
            String numStr = matcher.group(1).replace(".", "").replace(",", ".");
            valor = new BigDecimal(numStr);
        }

        Transacao nova = service.salvar(texto, valor, tipo);
        return ResponseEntity.ok(nova);
    }

    @PostMapping("/transacao")
    public ResponseEntity<Transacao> criarTransacao(@RequestBody TransacaoRequest request) {
        Transacao nova = service.salvar(request.descricao(), request.valor(), request.tipo());
        return ResponseEntity.ok(nova);
    }

    @GetMapping("/transacoes")
    public ResponseEntity<List<Transacao>> listarTransacoes() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> obterSaldo() {
        return ResponseEntity.ok(service.calcularSaldo());
    }

    // Tratamento de exceção para saldo insuficiente (retorna erro HTTP 400)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    public record TransacaoRequest(String descricao, BigDecimal valor, String tipo) {}
}