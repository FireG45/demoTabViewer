package ru.fireg45.demotabviewer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.util.Tuple;

import java.util.List;

@Component
@Repository
public class TabulatureSearchRepositoryImpl implements TabulatureSearchRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Tuple<List<Tabulature>, Long> search(String query) throws InterruptedException {
        SearchSession searchSession = Search.session(entityManager);

        searchSession.massIndexer(Tabulature.class).startAndWait();

        SearchResult<Tabulature> result = searchSession.search(Tabulature.class)
                .where(f -> f.match()
                        .fields("title", "author")
                        .matching(query))
                .fetchAll();

        return new Tuple<>(result.hits(), result.total().hitCount());
    }
}
