package com.zemiak.movies.service.jsp;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.lookup.CDILookup;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class GenreWebService implements Serializable {
    private final com.zemiak.movies.service.GenreService service;

    public GenreWebService() {
        CDILookup lookup = new CDILookup();
        service = lookup.lookup(com.zemiak.movies.service.GenreService.class);
    }

    public List<Genre> getAll() {
        return service.all();
    }
}
