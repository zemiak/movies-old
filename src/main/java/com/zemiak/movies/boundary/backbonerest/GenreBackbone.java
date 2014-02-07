package com.zemiak.movies.boundary.backbonerest;

import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.domain.Genre;
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
@Path("/genres")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Stateless
public class GenreBackbone {
    @Inject GenreService service;
    
    @GET
    public List<Genre> all() {
        return service.all();
    }
    
    @GET
    @Path("search/{query}")
    public List<Genre> search(@PathParam("query") final String query) {
        return service.getByExpression(query);
    }
    
    @GET
    @Path("{id}")
    public Genre find(@PathParam("id") final String id) {
        return service.find(Integer.valueOf(id));
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
}
