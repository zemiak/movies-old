package com.zemiak.movies.description;

import com.zemiak.movies.domain.Serie;
import java.util.ArrayList;
import java.util.List;

class GameOfThrones extends AbstractSeriesDescriptionReader {
    GameOfThrones() {
    }

    @Override
    protected Integer getSerieId() {
        return Serie.GAME_OF_THRONES_ID;
    }

    @Override
    protected String getBundleName() {
        return "game-of-thrones";
    }

    @Override
    public String getReaderName() {
        return "GoT";
    }
    
    @Override
    protected List<Integer> getRange() {
        List<Integer> list = new ArrayList<>();
        addAll(list, 101, 111);
        addAll(list, 201, 211);
        addAll(list, 301, 311);
        addAll(list, 401, 411);
        return list;
    }
}
