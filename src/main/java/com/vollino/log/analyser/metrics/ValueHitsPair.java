package com.vollino.log.analyser.metrics;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * @author Bruno Vollino
 */
public class ValueHitsPair {
    private String value;
    private long hits;

    public ValueHitsPair() {
    }

    public ValueHitsPair(String value, long hits) {
        this.value = value;
        this.hits = hits;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueHitsPair that = (ValueHitsPair) o;
        return getHits() == that.getHits() &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getHits());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("hits", hits)
                .toString();
    }
}
