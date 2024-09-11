package org.roleonce.projektarbete_web_services.controller;

import org.apache.coyote.Response;
import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.model.Rating;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.repository.RatingRepository;
import org.roleonce.projektarbete_web_services.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/{id}/save")
    public Movie saveMovie(@PathVariable int id) {

        return apiService.saveMovieWithId(id);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Movie> updateMovieWithId(@PathVariable Long id, @RequestBody Movie movie) {

        Optional<Movie> updateMovie = movieRepository.findById(id);

        if (updateMovie.isPresent()) {
            Movie existingMovie = updateMovie.get();
            if (movie.getTitle() != null) {
                existingMovie.setTitle(movie.getTitle());
            }
            if (movie.getOverview() != null) {
                existingMovie.setOverview(movie.getOverview());
            }
            if (movie.getReleaseDate() != null) {
                existingMovie.setReleaseDate(movie.getReleaseDate());
            }
            if (movie.getVoteAverage() != null) {
                existingMovie.setVoteAverage(movie.getVoteAverage());
            }

            movieRepository.save(existingMovie);

            return ResponseEntity.ok(existingMovie);
        } else {
            return ResponseEntity.notFound().build(); // Returnera 404 om filmen inte hittas
        }
    }

}
