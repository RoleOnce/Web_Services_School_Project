package org.roleonce.projektarbete_web_services.controller;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.response.WsResponse;
import org.roleonce.projektarbete_web_services.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class MovieController {

    private final MovieRepository movieRepository;
    private final ApiService apiService;

    @Autowired
    public MovieController(ApiService apiService, MovieRepository movieRepository) {
        this.apiService = apiService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WsResponse> getMovie(@PathVariable Long id) {

        return apiService.getMovieById(id);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovies() {

        return ResponseEntity.ok(movieRepository.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<WsResponse> searchMovie(@RequestParam("title") String title) {

        return apiService.searchForMoviesByTitle(title);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<WsResponse> getAllMovies(@PathVariable("country") String country, @RequestParam(value = "title", required = false) String title) {

        return apiService.getMoviesByCountry(country, title);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<WsResponse> postReview(@PathVariable Long id, @RequestBody Movie movie) {

        return apiService.postAReviewById(id, movie);
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<WsResponse> saveMovie(@PathVariable Long id) {

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
