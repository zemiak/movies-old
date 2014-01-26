package com.zemiak.movies.ui.view;

import com.vaadin.data.Property;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Video;
import com.zemiak.movies.MoviesTheme;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.ui.view.components.ButtonIcon;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;

@Dependent
class MovieDetail extends ViewAbstract {
    FormLayout layout;
    Movie movie;
    
    @Resource(name = "com.zemiak.movies")
    private Properties properties;
    
    public MovieDetail() {
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    @Override
    public void attach() {
        super.attach();
        
        layout = new FormLayout();
        layout.setWidth("100%");
        setContent(layout);
    }
    
    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        setCaption(movie.getName());
        
        refresh();
    }
    
    private String getAbsoluteFileName() {
        return properties.getProperty("path") + movie.getFileName();
    }
    
    public class VideoSource implements StreamResource.StreamSource {
        InputStream stream;
        
        public VideoSource(String fileName) {
            try {
                stream = new FileInputStream(getAbsoluteFileName());
                System.err.println("Creating Video Stream: '" + getAbsoluteFileName() + "'");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MovieDetail.class.getName()).log(Level.SEVERE, "Cannot open file " + getAbsoluteFileName(), ex);
                stream = null;
            }
        }

        @Override
        public InputStream getStream() {
            return stream;
        }
        
    }

    private void refresh() {
        layout.removeAllComponents();
        initMovieName();
        initPlayer();
        initDescription();
        initGenre();
        initSerie();
        initLanguage();
        initOriginalLanguage();
        initSubtitles();
        initOriginalName();        
   }

    private void initOriginalName() throws Property.ReadOnlyException {
        if (null != movie.getOriginalName()) {
            TextField origName = new TextField("Original Name");
            origName.setValue(movie.getOriginalName());
            origName.setReadOnly(true);
            layout.addComponent(origName);
        }
    }

    private void initSubtitles() throws Property.ReadOnlyException {
        if (null != movie.getSubtitles() && !movie.getSubtitles().isNone()) {
            TextField subtitles = new TextField("Subtitles");
            subtitles.setValue(movie.getSubtitles().getName());
            subtitles.setReadOnly(true);
            layout.addComponent(subtitles);
        }
    }

    private void initOriginalLanguage() throws Property.ReadOnlyException {
        if (null != movie.getOriginalLanguage() && !movie.getOriginalLanguage().isNone()) {
            TextField origLang = new TextField("Original Language");
            origLang.setValue(movie.getOriginalLanguage().getName());
            origLang.setReadOnly(true);
            layout.addComponent(origLang);
        }
    }

    private void initLanguage() throws Property.ReadOnlyException {
        if (null != movie.getLanguage() && !movie.getLanguage().isNone()) {
            TextField lang = new TextField("Language");
            lang.setValue(movie.getLanguage().getName());
            lang.setReadOnly(true);
            layout.addComponent(lang);
        }
    }

    private void initSerie() throws Property.ReadOnlyException {
        if (null != movie.getSerieId()) {
            TextField serie = new TextField("Serie");
            serie.setValue(movie.getSerieId().getName());
            serie.setReadOnly(true);
            layout.addComponent(serie);
        }
    }

    private void initGenre() throws Property.ReadOnlyException {
        TextField genre = new TextField("Genre");
        genre.setValue(movie.getGenreId().getName());
        genre.setReadOnly(true);
        layout.addComponent(genre);
    }

    private void initDescription() {
        if (null != movie.getDescription() && !movie.getDescription().isEmpty()) {
            Label desc = new Label(movie.getDescription());
            layout.addComponent(desc);
        }
    }

    private void initPlayer() {
        final StreamResource res = new StreamResource(new VideoSource(getAbsoluteFileName()), movie.getName());
        res.setMIMEType("video/mp4");
        
        Video player = new Video();
        
        player.setSource(res);
        player.setWidth("560");
        player.setHeight("432");
        player.setPoster(ButtonIcon.movieIcon(movie));
        layout.addComponent(player);
    }

    private void initMovieName() {
        Label movieLabel = new Label(movie.getName());
        movieLabel.setStyleName(MoviesTheme.LABEL_H1);
        layout.addComponent(movieLabel);
    }
}
