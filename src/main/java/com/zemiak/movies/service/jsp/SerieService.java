package com.zemiak.movies.service.jsp;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.backbonerest.SerieDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class SerieService implements Serializable {
    private final com.zemiak.movies.service.SerieService service;
    private Integer genreId;

    public SerieService() {
        CDILookup lookup = new CDILookup();
        service = lookup.lookup(com.zemiak.movies.service.SerieService.class);
    }

    public List<SerieDTO> getByGenreId() {
        List<SerieDTO> results = new ArrayList<>();
        service.all().stream().filter(serie -> genreId.equals(serie.getGenreId().getId())).forEach((serie) -> {
            results.add(new SerieDTO(serie));
        });
        
        return results;
    }
    
    public void setGenreId(HttpServletRequest request) {
        if (null != request.getParameter("id")) {
            genreId = Integer.valueOf(request.getParameter("id"));
        }
    }
}
