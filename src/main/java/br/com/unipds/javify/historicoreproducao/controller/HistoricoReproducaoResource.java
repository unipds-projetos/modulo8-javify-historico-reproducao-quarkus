package br.com.unipds.javify.historicoreproducao.controller;

import br.com.unipds.javify.historicoreproducao.domain.HistoricoReproducao;
import br.com.unipds.javify.historicoreproducao.repository.HistoricoReproducaoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@Path("/api/v1/analytics/historico")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistoricoReproducaoResource {

    @Inject
    HistoricoReproducaoRepository historicoRepository;

    @POST
    @Path("/{usuarioId}/tocar/{faixaId}")
    public Response registrarPlay(
            @PathParam("usuarioId") Long usuarioId,
            @PathParam("faixaId") String faixaId,
            @QueryParam("nomeFaixa") String nomeFaixa) {

        HistoricoReproducao registro = new HistoricoReproducao(
                usuarioId,
                LocalDateTime.now(),
                faixaId,
                nomeFaixa
        );

        historicoRepository.save(registro);

        return Response.accepted().build();
    }

    @GET
    @Path("/{usuarioId}")
    public List<HistoricoReproducao> obterHistoricoUsuario(@PathParam("usuarioId") Long usuarioId) {
        return historicoRepository.findByUsuarioId(usuarioId);
    }
}