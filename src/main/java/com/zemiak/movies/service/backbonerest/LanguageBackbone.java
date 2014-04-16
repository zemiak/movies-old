package com.zemiak.movies.service.backbonerest;

import com.zemiak.movies.service.LanguageService;
import com.zemiak.movies.domain.Language;
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
@Path("/languages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Stateless
public class LanguageBackbone {
    @Inject LanguageService service;
    
    @GET
    public List<Language> all() {
        return service.all();
    }
    
    @GET
    @Path("search/{query}")
    public List<Language> search(@PathParam("query") final String query) {
        return service.getByExpression(query);
    }
    
    @GET
    @Path("{id}")
    public Language find(@PathParam("id") final String id) {
        return service.find(id);
    }
    
    @POST
    public Language create(final Language item) {
        item.setId(null);
        
        service.save(item);
        return item;
    }
    
    @PUT
    @Path("{id}")
    public Language update(final Language item) {
        service.save(item);
        return item;
    }
    
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") final String id) {
        service.remove(id);
    }
}
