package ru.fireg45.demotabviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireg45.demotabviewer.model.Favorite;
import ru.fireg45.demotabviewer.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
