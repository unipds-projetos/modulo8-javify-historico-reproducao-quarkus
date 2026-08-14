package br.com.unipds.javify.historicoreproducao.repository;

import br.com.unipds.javify.historicoreproducao.domain.HistoricoReproducao;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class HistoricoReproducaoRepository {

    @Inject
    CqlSession cqlSession;

    private PreparedStatement insertStmt;
    private PreparedStatement findByUsuarioIdStmt;

    @PostConstruct
    void init() {
        insertStmt = cqlSession.prepare("""
            INSERT INTO historico_reproducao
            (usuario_id, data_reproducao, faixa_id, nome_faixa)
            VALUES (?, ?, ?, ?)
        """);

        findByUsuarioIdStmt = cqlSession.prepare("""
            SELECT usuario_id,data_reproducao,faixa_id,nome_faixa
            FROM historico_reproducao
            WHERE usuario_id = ?
        """);
    }

    public void save(HistoricoReproducao h) {
        cqlSession.execute(
                insertStmt.bind(
                        h.usuarioId(),
                        h.dataReproducao().atZone(ZoneId.systemDefault()).toInstant(),
                        h.faixaId(),
                        h.nomeFaixa()
                )
        );
    }

    public List<HistoricoReproducao> findByUsuarioId(Long usuarioId) {
        return cqlSession.execute(findByUsuarioIdStmt.bind(usuarioId))
                .all().stream()
                .map(row -> new HistoricoReproducao(
                        row.getLong("usuario_id"),
                        Optional.ofNullable(row.getInstant("data_reproducao"))
                                .map(instant -> instant.atZone(ZoneId.systemDefault())
                                        .toLocalDateTime())
                                .orElse(null),
                        row.getString("faixa_id"),
                        row.getString("nome_faixa")
                ))
                .toList();
    }
}
