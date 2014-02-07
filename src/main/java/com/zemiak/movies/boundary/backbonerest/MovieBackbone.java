package com.zemiak.movies.boundary.backbonerest;

import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.domain.Movie;
import java.util.ArrayList;
import java.util.List;
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
@Path("/movies")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Stateless
public class MovieBackbone {
    @Inject MovieService service;
    
    @GET
    public List<MovieDTO> all() {
        return convertList(service.all());
    }
    
    @GET
    @Path("search/{query}")
    public List<MovieDTO> search(@PathParam("query") final String query) {
        return convertList(service.getByExpression(query));
    }
    
    @GET
    @Path("{id}")
    public MovieDTO find(@PathParam("id") final String id) {
        return new MovieDTO(service.find(Integer.valueOf(id)));
    }
    
    @POST
    public MovieDTO create(final Movie item) {
        item.setId(null);
        
        service.save(item);
        return new MovieDTO(item);
    }
    
    @PUT
    @Path("{id}")
    public MovieDTO update(final Movie item) {
        service.save(item);
        return new MovieDTO(item);
    }
    
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") final String id) {
        service.remove(Integer.valueOf(id));
    }
    
    @GET
    @Path("version")
    @Produces({MediaType.TEXT_PLAIN})
    public Integer version() {
        Integer lastId = -1;
        for (Movie entry: service.all()) {
            if (entry.getId() > lastId) {
                lastId = entry.getId();
            }
        }
        
        return lastId;
    }
    
    private List<MovieDTO> convertList(final List<Movie> movieList) {
        final List<MovieDTO> dtoList = new ArrayList<>();
        
        for (Movie movie: movieList) {
            dtoList.add(new MovieDTO(movie));
        }
        
        return dtoList;
    }
}
