package org.roleonce.projektarbete_web_services.service;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.response.ErrorResponse;
import org.roleonce.projektarbete_web_services.response.ListResponse;
import org.roleonce.projektarbete_web_services.response.MovieResponse;
import org.roleonce.projektarbete_web_services.response.WsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Value("${api.key}")
    private String apiKey;
    final String apiUrl = "https://api.themoviedb.org/3/movie/";

    public ResponseEntity<WsResponse> getMoviesByCountry(String country, String title) {

        List<Movie> movies;

        if (title != null && !title.isEmpty()) {
            // om land och titel är angivna så kör denna
            movies = movieRepository.findByOriginCountryAndTitle(Collections.singletonList(country), title);
        } else {
            // om endast land angivs kör denna
            movies = movieRepository.findByOriginCountry(Collections.singletonList(country));
        }

        if (movies.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("No movies found for the country: " + country + " with the title: " + title));
        }

        return ResponseEntity.ok(new ListResponse(movies));
    }

    public ResponseEntity<WsResponse> getMovieById(Long movieId) {

        String url = apiUrl + movieId + "?api_key=" + apiKey;

        try {
            ResponseEntity<Movie> response = restTemplate.getForEntity(url, Movie.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Movie movie = response.getBody();
                return ResponseEntity.ok(new MovieResponse(movie));
            }
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(new ErrorResponse("Unexpected error: " + response.getStatusCode()));

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("Movie not found."));
        }
    }

    public ResponseEntity<WsResponse> saveMovieById(Long movieId) {

        String url = apiUrl + movieId + "?api_key=" + apiKey;

        try {
            Movie movie = restTemplate.getForObject(url, Movie.class);

            if (movie == null) {
                return ResponseEntity
                        .status(404)
                        .body(new ErrorResponse("Movie not found."));
            }
            if (movie.getOverview().length() > 254) {
                movie.setOverview(movie.getOverview().substring(0, 254));
            }

            movieRepository.save(movie);
            return ResponseEntity.ok(new MovieResponse(movie));

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("Movie couldn't be saved due to not found"));
        }
    }

    public ResponseEntity<String> deleteMovieById(Long movieId) {

        try {
            Optional<Movie> movie = movieRepository.findById(movieId);

            if (movie.isEmpty()) {
                return ResponseEntity.status(404).body("Movie not found in database.");
            }

            movieRepository.deleteById(movieId);
            return ResponseEntity.ok().body("Movie deleted successfully.");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Movie not found in database.");
        }
    }

    public ResponseEntity<WsResponse> updateMovieCredentialsById(Long movieId, @RequestBody Movie movie) {

        try {
            Optional<Movie> updateMovie = movieRepository.findById(movieId);

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
                if (movie.getOriginCountry() != null) {
                    existingMovie.setOriginCountry(movie.getOriginCountry());
                }
                if (movie.getVote_average() != null) {
                    existingMovie.setVote_average(movie.getVote_average());
                }

                movieRepository.save(existingMovie);
                return ResponseEntity.ok(new MovieResponse(existingMovie));

            }
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("Can't update a non-existing movie"));

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("Can't update movie credentials: " + e.getMessage()));
        }
    }

    public ResponseEntity<WsResponse> postAReviewById(Long movieId, @RequestBody Movie movie) {

        try {
            Optional<Movie> reviewMovie = movieRepository.findById(movieId);
            if (reviewMovie.isPresent()) {
                Movie existingMovie = reviewMovie.get();
                existingMovie.setMovie_review(movie.getMovie_review());

                movieRepository.save(existingMovie);

                return ResponseEntity.ok(new MovieResponse(existingMovie));
            }
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error occurred with message :" + e.getMessage()));
        }

        return ResponseEntity.status(404).body(new ErrorResponse("Movie not found in database. Try another id"));
    }

    public ResponseEntity<WsResponse> searchForMoviesByTitle(String title) {

        List<Movie> movies = movieRepository.findByTitle(title);

        try {
            if (movies.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body(new ErrorResponse("No movies found with the title: " + title));
            }
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Error occurred with message :" + e.getMessage()));
        }
        return ResponseEntity.ok(new MovieResponse(movies.get(0)));
    }

}
