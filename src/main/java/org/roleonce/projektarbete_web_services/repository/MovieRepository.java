package org.roleonce.projektarbete_web_services.repository;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
