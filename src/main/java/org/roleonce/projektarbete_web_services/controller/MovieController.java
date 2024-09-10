package org.roleonce.projektarbete_web_services.controller;

import org.apache.coyote.Response;
import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.model.Rating;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.repository.RatingRepository;
import org.roleonce.projektarbete_web_services.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final ApiService apiService;

    @Autowired
    public MovieController(MovieRepository movieRepository, RatingRepository ratingRepository, ApiService apiService) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.apiService = apiService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllUsers() {

        return ResponseEntity.ok(movieRepository.findAll());
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable int id) {

        return apiService.getMovieWithId(id);
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<String> postRating(@PathVariable int id, @RequestParam double rating) {

        Rating newRating = new Rating(id, rating);
        ratingRepository.save(newRating);

        return ResponseEntity.ok("Rating added to " + id);
    }

    @PostMapping("/save/{id}")
    public Movie saveMovie(@PathVariable int id) {

        return apiService.saveMovieWithId(id);
    }

}
