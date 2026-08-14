package br.com.unipds.javify.historicoreproducao.domain;

import java.time.LocalDateTime;

public record HistoricoReproducao (
        Long usuarioId,
        LocalDateTime dataReproducao,
        String faixaId,
        String nomeFaixa) { }