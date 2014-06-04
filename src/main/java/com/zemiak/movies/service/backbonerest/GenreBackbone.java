package com.zemiak.movies.service.backbonerest;

import com.zemiak.movies.service.GenreService;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.service.backbonerest.specialmovie.Last6MonthsGenre;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author vasko
 */
@Path("/genres")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Stateless
public class GenreBackbone {
    @Inject GenreService service;
    @Inject MovieBackbone movies;
    
    @GET
    public List<Genre> all() {
        List<Genre> genres = new ArrayList<>();
        Genre empty = null;
        
        genres.add(getLast6Months());
        
        for (Genre genre: service.all()) {
            if (! genre.isEmpty()) {
                genres.add(genre);
            } else {
                empty = genre;
            }
        }
        
        genres.add(empty);
        
        return genres;
    }
    
    @GET
    @Path("search/{query}")
    public List<Genre> search(@PathParam("query") final String query) {
        return service.getByExpression(query);
    }
    
    @GET
    @Path("{id}")
    public Genre find(@PathParam("id") final String idText) {
        Integer id = Integer.valueOf(idText);
        if (Objects.equals(Last6MonthsGenre.ID, id)) {
            return getLast6Months();
        }
        
        return service.find(id);
    }
    
    @POST
    public Genre create(final Genre item) {
        item.setId(null);
        
        service.save(item);
        return item;
    }
    
    @PUT
    @Path("{id}")
    public Genre update(final Genre item) {
        service.save(item);
        return item;
    }
    
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") final String id) {
        service.remove(Integer.valueOf(id));
    }

    private Genre getLast6Months() {
        Genre res = new Last6MonthsGenre();
        
        return res;
    }
}
