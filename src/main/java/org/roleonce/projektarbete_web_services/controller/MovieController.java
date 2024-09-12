package org.roleonce.projektarbete_web_services.controller;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.response.ErrorResponse;
import org.roleonce.projektarbete_web_services.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;
    private final ApiService apiService;

    @Autowired
    public MovieController(MovieRepository movieRepository, ApiService apiService) {
        this.movieRepository = movieRepository;
        this.apiService = apiService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllUsers() {

        return ResponseEntity.ok(movieRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovie(@PathVariable int id) {

        return apiService.getMovieWithId(id);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<String> postReview(@PathVariable Long id, @RequestBody Movie movie) {

        Optional<Movie> reviewMovie = movieRepository.findById(id);

        if (reviewMovie.isPresent()) {
            Movie existingMovie = reviewMovie.get();
            existingMovie.setMovie_review(movie.getMovie_review());

            movieRepository.save(movie);

            return ResponseEntity.ok("REVIEW added to " + id);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<ErrorResponse> saveMovie(@PathVariable int id) {

        return (ResponseEntity<ErrorResponse>) apiService.saveMovieWithId(id);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Movie> updateMovieById(@PathVariable Long id, @RequestBody Movie movie) {

        Optional<Movie> updateMovie = movieRepository.findById(id);

        if (updateMovie.isPresent()) {
            Movie existingMovie = updateMovie.get();
            if (movie.getTitle() != null) {
                existingMovie.setTitle(movie.getTitle());
            }
            if (movie.getOverview() != null) {
                existingMovie.setOverview(movie.getOverview());
            }
            if (movie.getMovie_review() != null) {
                existingMovie.setMovie_review(movie.getMovie_review());
            }
            if (movie.getRelease_date() != null) {
                existingMovie.setRelease_date(movie.getRelease_date());
            }
            if (movie.getVote_average() != null) {
                existingMovie.setVote_average(movie.getVote_average());
            }

            movieRepository.save(existingMovie);

            return ResponseEntity.ok(existingMovie);
        } else {
            return ResponseEntity.notFound().build(); // Returnera 404 om filmen inte hittas
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteMovieById(@PathVariable Long id) {

        Optional<Movie> movie = movieRepository.findById(id);

        movie.ifPresent(movieRepository::delete);
        return ResponseEntity.ok("Movie deleted");
    }

}
