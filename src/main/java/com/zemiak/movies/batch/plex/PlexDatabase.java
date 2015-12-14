package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PlexDatabase {
    private static final BatchLogger LOG = BatchLogger.getLogger(PlexDatabase.class.getName());

    @Inject String plexDatabasePath;
    @Inject SerieService series;
    @Inject MovieService movies;

    Connection connection;
    Statement statement;
    Map<Integer, Integer> tags;
    String now;

    @PostConstruct
    public void init() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plexDatabasePath);
            statement = connection.createStatement();
            statement.setQueryTimeout(3);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Cannot get connection to the Plex DB", ex);
            connection = null;
        }
    }

    @PreDestroy
    public void deinit() {
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Error closing connection", ex);
            }
        }
    }

    /**
     * There is a small problem with this Plex DB refresh.
     * There may be items not yet imported into DB, but already existing
     * as a part of a collection in movies.
     */
    public void refresh() {
        try {
            refreshAll();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error refreshing DB", ex);
        }
    }

    private void refreshAll() throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        now = formatter.format(new java.util.Date());

        removeAssociations();
        removeCollections();
        createCollections();
        createAssociations();
    }

    private void removeAssociations() throws SQLException {
        String sql = "delete from taggings"
                + " where tag_id in"
                + " (select id from tags where tag_type = 2 and tag_value like 'Serie_%')";
        statement.executeUpdate(sql);
    }

    private void removeCollections() throws SQLException {
        String sql = "delete from tags where tag_type = 2 and tag_value like 'Serie_%'";
        statement.executeUpdate(sql);
    }

    private void createCollections() {
        tags = new HashMap<>();
        series.all().stream().forEach(this::createCollection);
    }

    private void createCollection(Serie serie) {
        try {
            createCollection0(serie);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "SQL Error creating a tag " + serie.getName(), ex);
        }
    }

    private void createCollection0(Serie serie) throws SQLException {
        String sql = "insert into tags (tag, tag_type, created_at, updated_at, tag_value)"
                + " values ('%s', 2, '" + now + "', '" + now + "',"
                + " 'Serie_" + String.valueOf(serie.getId()) + "')";

        statement.executeUpdate(sql);

        sql = "select id from tags where tag_value = 'Serie_" + String.valueOf(serie.getId()) + "'";

        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        tags.put(serie.getId(), resultSet.getInt(1));
    }

    private void createAssociations() {
        series.all().forEach(serie -> {
            movies.getSerieMovies(serie).stream().forEach(movie -> createAssociation(movie, serie));
        });
    }

    private void createAssociation(Movie movie, Serie serie) {
        try {
            createAssociation0(movie, serie);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Cannot create tag association for movie " + movie.getPlexFileName(), ex);
        }
    }

    private void createAssociation0(Movie movie, Serie serie) throws SQLException {
        String movieMetadataId = findPlexMovieMetadataId(movie);
        String tagId = findPlexSerieId(serie);

        if (null == movieMetadataId) {
            // not yet imported in Plex
            return;
        }

        String sql = "insert into taggings (metadata_item_id, tag_id, index, created_at)"
                + " values(" + movieMetadataId + ", " + tagId
                + ", " + String.valueOf(movie.getDisplayOrder()) + ", '" + now + "')";
        statement.executeUpdate(sql);
    }

    private String findPlexMovieMetadataId(Movie movie) throws SQLException {
        String sql = "select i.metadata_item_id from media_items i"
                + " left join media_parts p on p.media_item_id = i.id"
                + " where file = '" + movie.getPlexFileName() + "'";
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        return resultSet.getString(1);
    }

    private String findPlexSerieId(Serie serie) throws SQLException {
        String sql = "select id from tags where tag_value = 'Serie_" + String.valueOf(serie.getId()) + "'";
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        return resultSet.getString(1);
    }
}
