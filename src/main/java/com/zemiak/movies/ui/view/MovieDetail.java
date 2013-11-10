package com.zemiak.movies.ui.view;

import com.vaadin.data.Property;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Video;
import com.zemiak.movies.MoviesTheme;
import com.zemiak.movies.domain.Movie;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;

@Dependent
class MovieDetail extends ViewAbstract {
    final private String MOVIE_PREFIX = "/mnt/media/Movies/";
    
    VerticalLayout layout;
    Movie movie;
    
    public MovieDetail() {
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    @Override
    public void attach() {
        super.attach();
        
        layout = new VerticalLayout();
        layout.setWidth("100%");
        setContent(layout);
    }
    
    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        setCaption(movie.getName());
        
        refresh();
    }
    
    public class VideoSource implements StreamResource.StreamSource {
        InputStream stream;
        
        public VideoSource(String fileName) {
            try {
                stream = new FileInputStream(MOVIE_PREFIX + fileName);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MovieDetail.class.getName()).log(Level.SEVERE, "Cannot open file " + MOVIE_PREFIX + fileName, ex);
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
        if (null != movie.getSubtitles()) {
            TextField subtitles = new TextField("Subtitles");
            subtitles.setValue(movie.getSubtitles().getName());
            subtitles.setReadOnly(true);
            layout.addComponent(subtitles);
        }
    }

    private void initOriginalLanguage() throws Property.ReadOnlyException {
        if (null != movie.getOriginalLanguage()) {
            TextField origLang = new TextField("Original Language");
            origLang.setValue(movie.getOriginalLanguage().getName());
            origLang.setReadOnly(true);
            layout.addComponent(origLang);
        }
    }

    private void initLanguage() throws Property.ReadOnlyException {
        if (null != movie.getLanguage()) {
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
        Video player = new Video();
        player.setAltText("Please, use a HTML5 compatible browser to play the video.");
        player.setSource(new StreamResource(new VideoSource(movie.getFileName()), movie.getFileName()));
        player.setWidth("560");
        player.setHeight("432");
        layout.addComponent(player);
    }

    private void initMovieName() {
        Label movieLabel = new Label(movie.getName());
        movieLabel.setStyleName(MoviesTheme.LABEL_H1);
        layout.addComponent(movieLabel);
    }
}
