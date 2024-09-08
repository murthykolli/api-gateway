
package com.selfpro.tmdb.controller;


import com.selfpro.tmdb.model.Movie;
import com.selfpro.tmdb.model.MovingRating;
import com.selfpro.tmdb.model.Rating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${movie-service.url}")
    private String movieServiceUrl;

    @Value("${ratings-service.url}")
    private String ratingsServiceUrl;

    @PostMapping
    public ResponseEntity<Object> addMovie(@RequestBody Movie movie) {
        try {
            log.info("Adding Movie");
            Movie savedMovie = restTemplate.postForObject(movieServiceUrl, movie, Movie.class);
            return ResponseEntity.ok().body(savedMovie);
        } catch (HttpStatusCodeException ex) {
            log.error("Error adding Movie: {}", ex.getMessage());
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        try {
            log.info("Update Movie: {}", id);
            restTemplate.put(movieServiceUrl + "/" + id,movie);
            return ResponseEntity.ok().build();
        } catch (HttpStatusCodeException ex) {
            log.error("Error update movie:{}", ex.getMessage());
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> fetchMovieAndRating(@PathVariable Long id) {
//        Movie movie = restTemplate.getForObject(movieServiceUrl + "/" + id, Movie.class);
//        Rating rating = restTemplate.getForObject(ratingsServiceUrl + "/" + movie.getName(), Rating.class);

        // If any possibilities errors we will write like bellow

        Movie movie;

        try {
            movie = restTemplate.getForObject(movieServiceUrl + "/" + id, Movie.class);
        } catch(HttpStatusCodeException ex) {
            log.error("Fetching Movie: {}", ex.getMessage());
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
        Rating rating;
        try {
            rating = restTemplate.getForObject(ratingsServiceUrl + "/" + movie.getName(), Rating.class);
        } catch(HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                rating = new Rating(null, movie.getName(), 0.0, 0);
            } else {
                rating = new Rating(null, movie.getName(), -1.0, -1);
            }
        } catch (ResourceAccessException ex) {
            log.warn("Exception: {}", ex.getMessage());
            rating = new Rating(null, movie.getName(), -1.0, -1);
        }

        MovingRating movingRating = new MovingRating();
        movingRating.setMovie(movie);
        movingRating.setRating(rating);
        return ResponseEntity.ok(movingRating);
    }
}

