package com.zemiak.movies.boundary.backbonerest;

import com.zemiak.movies.service.SerieService;
import com.zemiak.movies.domain.Serie;
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
@Path("/series")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Stateless
public class SerieBackbone {
    @Inject SerieService service;
    
    @GET
    public List<SerieDTO> all() {
        return convertList(service.all());
    }

    private List<SerieDTO> convertList(final List<Serie> serieList) {
        final List<SerieDTO> dtoList = new ArrayList<>();
        
        for (Serie serie: serieList) {
            dtoList.add(new SerieDTO(serie));
        }
        
        return dtoList;
    }
    
    @GET
    @Path("search/{query}")
    public List<SerieDTO> search(@PathParam("query") final String query) {
        return convertList(service.getByExpression(query));
    }
    
    @GET
    @Path("{id}")
    public SerieDTO find(@PathParam("id") final String id) {
        return new SerieDTO(service.find(Integer.valueOf(id)));
    }
    
    @POST
    public SerieDTO create(final Serie item) {
        item.setId(null);
        
        service.save(item);
        return new SerieDTO(item);
    }
    
    @PUT
    @Path("{id}")
    public SerieDTO update(final Serie item) {
        service.save(item);
        return new SerieDTO(item);
    }
    
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") final String id) {
        service.remove(Integer.valueOf(id));
    }
}
