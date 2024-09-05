package org.roleonce.projektarbete_web_services.service;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final String apiKey = "270c87c7d9febee8d8c0856291ff4572";
    private final String apiUrl = "https://api.themoviedb.org/3/movie/";

    @Autowired
    private RestTemplate restTemplate;

    public Movie getMovieId(int movieId) {
        String url = apiUrl + movieId + "?api_key=" + apiKey;

        return restTemplate.getForObject(url, Movie.class);
    }
}
