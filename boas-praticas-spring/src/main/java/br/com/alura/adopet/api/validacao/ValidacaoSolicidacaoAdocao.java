package br.com.alura.adopet.api.validacao;

import br.com.alura.adopet.api.dto.Adocao.SolicitarAdocaoDTO;

public interface ValidacaoSolicidacaoAdocao {

    void validar(SolicitarAdocaoDTO dto);
}
