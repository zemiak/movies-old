package com.zemiak.movies.batch.plex;

import com.zemiak.movies.service.ConfigurationProvider;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

@Dependent
public class TriggerPlexRefresh {
    private static final Logger LOG = Logger.getLogger(TriggerPlexRefresh.class.getName());

    @Inject
    ConfigurationProvider conf;

    public enum LibraryType {
        MUSIC("music"),
        MOVIES("movies"),
        TV_SHOWS("tvshows");

        private final String fileName;

        LibraryType(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public void refreshAll() {
        Arrays.asList(LibraryType.values()).stream().forEach(lib -> refresh(lib));
    }

    private void refresh(LibraryType lib) {
        String id = parseLibraryId(lib);
        if (null == id) {
            return;
        }

        Client client = ClientBuilder.newClient();
        client.property("jersey.config.client.connectTimeout", 2000);
        client.property("jersey.config.client.readTimeout", 1000);

        WebTarget target = client.target(conf.getConfigValue("plexUrl"))
                .path("library").path("sections").path(id).path("refresh");
        Response response;

        try {
            response = target.request(MediaType.APPLICATION_JSON).get();
        } catch (ProcessingException ex) {
            LOG.log(Level.SEVERE, "Error refreshing library "
                    + lib + " / "
                    + target.getUri().toASCIIString()
                    + ".",
                    ex);
            return;
        }

        if (! response.getStatusInfo().getFamily().equals(Family.SUCCESSFUL)) {
            String answer = response.readEntity(String.class);
            LOG.log(Level.SEVERE, "Error refreshing library {0} / {1}. Return code {2}, response {3}",
                    new Object[]{lib, target.getUri().toASCIIString(), response.getStatus(), answer});
            return;
        }
    }

    private String parseLibraryId(LibraryType lib) {
        Path libraryConfigFile = Paths.get(conf.getConfigValue("plexConfigPath"),
                lib.getFileName() + ".json");

        FileReader fileReader;
        try {
            fileReader = new FileReader(libraryConfigFile.toFile());
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Cannot find the config file {0} for a library", libraryConfigFile.toString());
            return null;
        }

        JsonReader reader = Json.createReader(fileReader);
        JsonObject main = reader.readObject();
        JsonArray children = main.getJsonArray("_children");
        JsonObject first = children.getJsonObject(0);
        return first.getString("key");
    }
}

/**   /mnt/media/plex/data/{music|movies|tvshows}.json
{
  "_elementType": "MediaContainer",
  "_children": [
    {
      "_elementType": "Directory",
      "art": "/:/resources/movie-fanart.jpg",
      "composite": "/library/sections/-1/composite/1446149128",
      "thumb": "/:/resources/movie.png",
      "key": "4",
      "type": "movie",
      "title": "Movies2",
      "agent": "com.plexapp.agents.imdb",
      "scanner": "Plex Movie Scanner",
      "language": "en",
      "uuid": "19a3d8ca-67c6-4125-997f-3ea79edb62b7",
      "updatedAt": "1446149128",
      "createdAt": "1446149128",
      "_children": [
        {
          "_elementType": "Location",
          "id": 4,
          "path": "/data2"
        }
      ]
    }
  ]
}
 */
