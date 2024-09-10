package org.roleonce.projektarbete_web_services.repository;

import org.roleonce.projektarbete_web_services.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
}
