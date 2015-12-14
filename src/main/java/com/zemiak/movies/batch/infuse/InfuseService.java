package com.zemiak.movies.batch.infuse;

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class InfuseService {
    @Inject BasicInfuseFolderStructureCreator basic;
    @Inject InfuseMovieWriter writer;

    public void process(List<String> files) {
        basic.cleanAndCreate();
        writer.process(files);
    }
}
