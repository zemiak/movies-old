package com.zemiak.movies.service.jsp;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.GenreDTO;
import com.zemiak.movies.lookup.CDILookup;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("genreWeb")
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

    public GenreDTO find(String id) {
        return new GenreDTO(service.find(Integer.valueOf(id)));
    }
}
