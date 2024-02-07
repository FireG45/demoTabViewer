package ru.fireg45.demotabviewer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.util.Tuple;

import java.util.List;

@Repository
public class TabulatureSearchRepositoryImpl implements TabulatureSearchRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Tuple<List<Tabulature>, Long> search(String query, int page, int pageSize, int pageCount) throws InterruptedException {
        SearchSession searchSession = Search.session(entityManager);

        int start = page * pageSize;

        SearchResult<Tabulature> result = searchSession.search(Tabulature.class)
                .where(f -> f.match()
                        .fields("title", "author")
                        .matching(query))
                .fetch(start, start + pageSize);

        return new Tuple<>(result.hits(), result.total().hitCount());
    }
}
