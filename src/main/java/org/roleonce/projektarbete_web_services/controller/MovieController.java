package org.roleonce.projektarbete_web_services.controller;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.response.ErrorResponse;
import org.roleonce.projektarbete_web_services.response.WsResponse;
import org.roleonce.projektarbete_web_services.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<WsResponse> getMovie(@PathVariable int id) {

        return apiService.getMovieById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<WsResponse> searchMovie(@RequestParam("title") String title) {

        return apiService.searchForMoviesByTitle(title);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<WsResponse> postReview(@PathVariable Long id, @RequestBody Movie movie) {

        return apiService.postAReviewById(id, movie);
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<WsResponse> saveMovie(@PathVariable int id) {

        return apiService.saveMovieById(id);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<WsResponse> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {

        return apiService.updateMovieCredentialsById(id, movie);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {

        return apiService.deleteMovieById(id);
    }

}
