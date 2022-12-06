package com.example.cricify;

public class LiveMatchModel {
    String name,matchType,status,venue,date,time;
    String teams[],URLS[],runs[],wickets[],overs[];
    String matchEnded,matchStarted;

    public LiveMatchModel(String name, String matchType, String status, String venue, String date, String time, String[] teams, String[] URLS, String[] runs, String[] wickets, String[] overs, String matchEnded, String matchStarted) {
        this.name = name;
        this.matchType = matchType;
        this.status = status;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.teams = teams;
        this.URLS = URLS;
        this.runs = runs;
        this.wickets = wickets;
        this.overs = overs;
        this.matchEnded = matchEnded;
        this.matchStarted = matchStarted;
    }

    public String getName() {
        return name;
    }

    public String getMatchType() {
        return matchType;
    }

    public String getStatus() {
        return status;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String[] getTeams() {
        return teams;
    }

    public String[] getURLS() {
        return URLS;
    }

    public String[] getRuns() {
        return runs;
    }

    public String[] getWickets() {
        return wickets;
    }

    public String[] getOvers() {
        return overs;
    }

    public String getMatchEnded() {
        return matchEnded;
    }

    public String getMatchStarted() {
        return matchStarted;
    }

}
