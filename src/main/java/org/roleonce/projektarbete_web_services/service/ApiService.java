package org.roleonce.projektarbete_web_services.service;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.response.ErrorResponse;
import org.roleonce.projektarbete_web_services.response.ListResponse;
import org.roleonce.projektarbete_web_services.response.MovieResponse;
import org.roleonce.projektarbete_web_services.response.WsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    //private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final MovieRepository movieRepository;

    public ApiService(WebClient.Builder webclientBuilder, MovieRepository movieRepository) {
        this.webClient = webclientBuilder.build();
        this.movieRepository = movieRepository;
    }

    @Value("${api.key}")
    private String apiKey;
    final String apiUrl = "https://api.themoviedb.org/3/movie/";

    public ResponseEntity<WsResponse> getMovieById(Long movieId) {

        String url = apiUrl + movieId + "?api_key=" + apiKey;

        try {
            Movie movie = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block(); // Blockar tills svaret kommer

            if (movie != null) {
                return ResponseEntity.ok(new MovieResponse(movie));
            } else {
                return ResponseEntity
                        .status(404)
                        .body(new ErrorResponse("Movie not found."));
            }
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("Movie not found."));
        } catch (WebClientResponseException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(new ErrorResponse("Unexpected error: " + e.getStatusCode()));
        }
        catch (ResourceAccessException e) {
            return ResponseEntity
                    .status(503)
                    .body(new ErrorResponse("Service unavailable. Please check your network connection."));
        }
    }

    public ResponseEntity<WsResponse> searchMoviesByTitleAndCountry(String title, List<String> country) {

        List<Movie> movies;

        try {
            if (country != null && !country.isEmpty()) {
                movies = movieRepository.findByTitleAndOriginCountry(title, country);
            } else {
                movies = movieRepository.findByTitle(title);
            }
            if (movies.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body(new ErrorResponse("No movies found for the title: " + title + " with the country: " + country));
            }

            return ResponseEntity.ok(new ListResponse(movies));

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("Unexpected error: " + e.getStatusCode()));
        }
    }

    public ResponseEntity<List<Movie>> budgetHighToLow() {

        List<Movie> movie = movieRepository.findAllByOrderByBudgetDesc();

        try {
            if (movie.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body(null);
            }

            return ResponseEntity.ok(movie);

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(500)
                    .body(null);
        }
    }

    public ResponseEntity<List<Movie>> ratingHighToLow() {

        List<Movie> movie = movieRepository.findAllByOrderByVoteAverageDesc();

        try {
            if (movie.isEmpty()) {
                return ResponseEntity.status(404).body(null);
            }

            return ResponseEntity.ok(movie);

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(500)
                    .body(null);
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
            return ResponseEntity
                    .status(404)
                    .body(new ErrorResponse("Movie not found in database. Try another id"));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("Error occurred with message :" + e.getMessage()));
        }
    }

    public ResponseEntity<WsResponse> saveMovieById(Long movieId) {

        String url = apiUrl + movieId + "?api_key=" + apiKey;

        try {
            Movie movie = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

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
        } catch (WebClientResponseException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(new ErrorResponse("Unexpected error: " + e.getStatusCode()));
        }
        catch (ResourceAccessException e) {
            return ResponseEntity
                    .status(503)
                    .body(new ErrorResponse("Service unavailable. Please check your network connection."));
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
                if (movie.getVoteAverage() != null) {
                    existingMovie.setVoteAverage(movie.getVoteAverage());
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

    public ResponseEntity<String> deleteMovieById(Long movieId) {

        try {
            Optional<Movie> movie = movieRepository.findById(movieId);

            if (movie.isEmpty()) {
                return ResponseEntity
                        .status(404)
                        .body("Movie not found in database.");
            }

            movieRepository.deleteById(movieId);
            return ResponseEntity.ok().body("Movie deleted successfully.");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity
                    .status(404)
                    .body("Movie not found in database.");
        }
    }

}
