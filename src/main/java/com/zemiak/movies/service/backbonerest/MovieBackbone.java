package com.zemiak.movies.service.backbonerest;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.backbonerest.specialmovie.Last6MonthsGenre;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
        final List<MovieDTO> list = new ArrayList<>();
        
        list.addAll(convertList(service.all()));
        list.addAll(getLastNMonthsMovies(6));
        
        return list;
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
    
    public List<MovieDTO> getLastNMonthsMovies(int months) {
        Genre genre = new Last6MonthsGenre();
        
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, months);
        
        List<MovieDTO> res = new ArrayList<>();
        for (Movie movie: service.all()) {
            if (null != movie.getCreated() && movie.getCreated().after(cal.getTime())) {
                MovieDTO newMovie = new MovieDTO(movie);
                newMovie.setGenre(genre);
                res.add(newMovie);
            }
            
        }
        
        Collections.sort(res, new Comparator<MovieDTO>() {
            @Override
            public int compare(MovieDTO o1, MovieDTO o2) {
                return o1.getCreated().compareTo(o2.getCreated()) * -1; // descending
            }
        });
        
        return res;
    }
}
