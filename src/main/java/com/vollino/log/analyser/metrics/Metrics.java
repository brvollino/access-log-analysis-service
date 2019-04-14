package com.vollino.log.analyser.metrics;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Vollino
 */
public class Metrics {
    private List<String> topHitUrls;
    private List<String> topHitUrlsPerRegion;
    private String leastHitsUrl;
    private Map<String, List<String>> topHitUrlsByDayWeekOrYear;
    private String minuteWithMostHits;

    public Metrics() {
    }

    public List<String> getTopHitUrls() {
        return topHitUrls;
    }

    public void setTopHitUrls(List<String> topHitUrls) {
        this.topHitUrls = topHitUrls;
    }

    public List<String> getTopHitUrlsPerRegion() {
        return topHitUrlsPerRegion;
    }

    public void setTopHitUrlsPerRegion(List<String> topHitUrlsPerRegion) {
        this.topHitUrlsPerRegion = topHitUrlsPerRegion;
    }

    public String getLeastHitsUrl() {
        return leastHitsUrl;
    }

    public void setLeastHitsUrl(String leastHitsUrl) {
        this.leastHitsUrl = leastHitsUrl;
    }

    public Map<String, List<String>> getTopHitUrlsByDayWeekOrYear() {
        return topHitUrlsByDayWeekOrYear;
    }

    public void setTopHitUrlsByDayWeekOrYear(Map<String, List<String>> topHitUrlsByDayWeekOrYear) {
        this.topHitUrlsByDayWeekOrYear = topHitUrlsByDayWeekOrYear;
    }

    public String getMinuteWithMostHits() {
        return minuteWithMostHits;
    }

    public void setMinuteWithMostHits(String minuteWithMostHits) {
        this.minuteWithMostHits = minuteWithMostHits;
    }
}
