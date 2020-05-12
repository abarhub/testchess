package org.chess.core.utils;

import java.util.Map;
import java.util.StringJoiner;

public class PerftStockfich {

    private long pertf;

    private Map<String,Long> map;

    public long getPertf() {
        return pertf;
    }

    public void setPertf(long pertf) {
        this.pertf = pertf;
    }

    public Map<String, Long> getMap() {
        return map;
    }

    public void setMap(Map<String, Long> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PerftStockfich.class.getSimpleName() + "[", "]")
                .add("pertf=" + pertf)
                .add("map=" + map)
                .toString();
    }
}
