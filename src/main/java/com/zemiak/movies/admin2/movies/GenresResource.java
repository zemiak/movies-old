package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.DataTablesAjaxData;
import com.zemiak.movies.domain.GenreDTO;
import com.zemiak.movies.service.GenreService;
import java.net.URI;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("genres")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class GenresResource {
    @Inject
    GenreService genres;

    @GET
    public DataTablesAjaxData<GenreDTO> getAllMovies() {
        return new DataTablesAjaxData<>(genres.all().stream().map(movie -> new GenreDTO(movie)).collect(Collectors.toList()));
    }

    @DELETE
    @Path("{id}")
    public Response delete(@NotNull @PathParam("id") Integer id) {
        genres.remove(id);
        return Response.ok().build();
    }
}
