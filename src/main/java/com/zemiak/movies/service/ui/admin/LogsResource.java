package com.zemiak.movies.service.ui.admin;

import com.zemiak.movies.domain.BatchLogDTO;
import com.zemiak.movies.domain.DataTablesAjaxData;
import com.zemiak.movies.service.BatchLogService;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("logs")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class LogsResource {
    @Inject
    BatchLogService logs;

    @GET
    public DataTablesAjaxData<BatchLogDTO> getAllMovies() {
        return new DataTablesAjaxData<>(logs.all().stream().map(log -> new BatchLogDTO(log)).collect(Collectors.toList()));
    }
}
