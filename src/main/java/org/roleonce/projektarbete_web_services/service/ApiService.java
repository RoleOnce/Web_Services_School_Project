package org.roleonce.projektarbete_web_services.service;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    final String apiUrl = "https://api.themoviedb.org/3/movie/";
    final String apiKey = "270c87c7d9febee8d8c0856291ff4572";

    public ResponseEntity<?> getMovieWithId(int movieId) {
        String url = apiUrl + movieId + "?api_key=" + apiKey;

        try {
            ResponseEntity<Movie> response = restTemplate.getForEntity(url, Movie.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            }
            return ResponseEntity.status(response.getStatusCode())
                    .body(new ErrorResponse("Unexpected error: " + response.getStatusCode()));

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(new ErrorResponse("Movie not found."));
        }
    }

    public Movie saveMovieWithId(int movieId) {
        String url = apiUrl + movieId + "?api_key=" + apiKey;
        Movie movie = restTemplate.getForObject(url, Movie.class);

        if (movie != null) {
            if (movie.getOverview().length() > 254) {
                movie.setOverview(movie.getOverview().substring(0, 254));
            }
            movieRepository.save(movie);
        }
        return movie;
    }

}
