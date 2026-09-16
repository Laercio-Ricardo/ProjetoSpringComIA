package com.exemplo.orcamento.controller;

import com.exemplo.orcamento.service.ai.OrcamentoAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orcamento")
public class OrcamentoController {

    private final OrcamentoAiService aiService;

    public OrcamentoController(OrcamentoAiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/comando")
    public String enviarComando(@RequestParam String mensagem) {
        try {
            return aiService.processarComando(mensagem);
        } catch (Exception e) {
            Throwable causa = e.getCause() != null ? e.getCause() : e;
            return "ERRO RAIZ DO GOOGLE: " + causa.getClass().getName() + " -> " + causa.getMessage();
        }
    }
}