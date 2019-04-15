package com.vollino.log.analyser.metrics;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Bruno Vollino
 */
public class Metrics {
    private List<ValueHitsPair> topHitUrls;
    private List<ValueHitsPair> topHitUrlsPerRegion;
    private ValueHitsPair leastHitsUrl;
    private Map<String, List<ValueHitsPair>> topHitUrlsByDayWeekOrYear;
    private ValueHitsPair minuteWithMostHits;

    public Metrics() {
    }

    public List<ValueHitsPair> getTopHitUrls() {
        return topHitUrls;
    }

    public void setTopHitUrls(List<ValueHitsPair> topHitUrls) {
        this.topHitUrls = topHitUrls;
    }

    public List<ValueHitsPair> getTopHitUrlsPerRegion() {
        return topHitUrlsPerRegion;
    }

    public void setTopHitUrlsPerRegion(List<ValueHitsPair> topHitUrlsPerRegion) {
        this.topHitUrlsPerRegion = topHitUrlsPerRegion;
    }

    public ValueHitsPair getLeastHitsUrl() {
        return leastHitsUrl;
    }

    public void setLeastHitsUrl(ValueHitsPair leastHitsUrl) {
        this.leastHitsUrl = leastHitsUrl;
    }

    public Map<String, List<ValueHitsPair>> getTopHitUrlsByDayWeekOrYear() {
        return topHitUrlsByDayWeekOrYear;
    }

    public void setTopHitUrlsByDayWeekOrYear(Map<String, List<ValueHitsPair>> topHitUrlsByDayWeekOrYear) {
        this.topHitUrlsByDayWeekOrYear = topHitUrlsByDayWeekOrYear;
    }

    public ValueHitsPair getMinuteWithMostHits() {
        return minuteWithMostHits;
    }

    public void setMinuteWithMostHits(ValueHitsPair minuteWithMostHits) {
        this.minuteWithMostHits = minuteWithMostHits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metrics metrics = (Metrics) o;
        return Objects.equals(getTopHitUrls(), metrics.getTopHitUrls()) &&
                Objects.equals(getTopHitUrlsPerRegion(), metrics.getTopHitUrlsPerRegion()) &&
                Objects.equals(getLeastHitsUrl(), metrics.getLeastHitsUrl()) &&
                Objects.equals(getTopHitUrlsByDayWeekOrYear(), metrics.getTopHitUrlsByDayWeekOrYear()) &&
                Objects.equals(getMinuteWithMostHits(), metrics.getMinuteWithMostHits());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopHitUrls(), getTopHitUrlsPerRegion(), getLeastHitsUrl(), getTopHitUrlsByDayWeekOrYear(), getMinuteWithMostHits());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("topHitUrls", topHitUrls)
                .add("topHitUrlsPerRegion", topHitUrlsPerRegion)
                .add("leastHitsUrl", leastHitsUrl)
                .add("topHitUrlsByDayWeekOrYear", topHitUrlsByDayWeekOrYear)
                .add("minuteWithMostHits", minuteWithMostHits)
                .toString();
    }
}
