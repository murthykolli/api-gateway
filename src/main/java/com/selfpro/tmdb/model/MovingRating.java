package com.selfpro.tmdb.model;

import lombok.Data;

@Data
public class MovingRating {

    private Movie movie;
    private Rating rating;
}
