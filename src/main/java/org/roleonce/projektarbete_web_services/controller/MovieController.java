package org.roleonce.projektarbete_web_services.controller;

import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/movies/{id}")
    public Movie getMovie(@PathVariable int id) {
        return apiService.getMovieId(id);
    }
}
