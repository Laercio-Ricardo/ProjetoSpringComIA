package com.exemplo.orcamento.service.ai;

import com.exemplo.orcamento.tool.TransacaoTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OrcamentoAiService {

    private final ChatClient chatClient;
    private final TransacaoTools transacaoTools;

    public OrcamentoAiService(ChatClient.Builder chatClientBuilder, TransacaoTools transacaoTools) {
        this.chatClient = chatClientBuilder.defaultSystem(
                "Você é um assistente financeiro inteligente e prestativo. " +
                        "Sempre que o usuário pedir para cadastrar, registrar ou salvar uma receita ou despesa, " +
                        "você DEVE chamar a ferramenta de transação disponível para salvá-la no banco de dados."
        ).build();
        this.transacaoTools = transacaoTools;
    }

    public String processarComando(String mensagemUsuario) {
        return chatClient.prompt()
                .user(mensagemUsuario)
                .tools(transacaoTools)
                .call()
                .content();
    }
}