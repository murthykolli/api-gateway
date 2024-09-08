package com.selfpro.tmdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rating {

    private Long id;
    private String name;
    private double avgRating;
    private int count;

}
