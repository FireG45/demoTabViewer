package ru.fireg45.demotabviewer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Repository;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.util.List;

@Repository
public class TabulatureSearchRepositoryImpl implements TabulatureSearchRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Tabulature> search(String query) throws InterruptedException {
        SearchSession searchSession = Search.session(entityManager);

        MassIndexer indexer = searchSession.massIndexer(Tabulature.class);
        indexer.startAndWait();

        SearchResult<Tabulature> result = searchSession.search(Tabulature.class)
                .where( f -> f.match()
                        .fields( "title", "author")
                        .matching(query))
                .fetchAll();

        List<Tabulature> hits = result.hits();
        return null;
    }
}
