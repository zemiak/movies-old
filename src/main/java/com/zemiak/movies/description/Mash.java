package com.zemiak.movies.description;

import com.zemiak.movies.domain.Serie;
import java.util.ArrayList;
import java.util.List;

class Mash extends AbstractSeriesDescriptionReader {
    @Override
    protected Integer getSerieId() {
        return Serie.MASH_ID;
    }

    @Override
    protected String getBundleName() {
        return "mash";
    }

    @Override
    public String getReaderName() {
        return "MASH";
    }
    
    @Override
    protected List<Integer> getRange() {
        List<Integer> list = new ArrayList<>();
        addAll(list, 1, 257);
        return list;
    }
}
