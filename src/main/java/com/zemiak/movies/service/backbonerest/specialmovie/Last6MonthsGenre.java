package com.zemiak.movies.service.backbonerest.specialmovie;

import com.zemiak.movies.domain.Genre;
import java.util.Date;

public class Last6MonthsGenre extends Genre {
    public final static Integer ID = -10;
    
    public Last6MonthsGenre() {
        this.setId(ID);
        this.setCreated(new Date());
        this.setDisplayOrder(ID);
        this.setName("Last 6 months");
        this.setPictureFileName("clock.png");
    }
}
