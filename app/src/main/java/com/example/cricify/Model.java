package com.example.cricify;

public class Model {
    Integer Balls, Boundaries;
    Double EconomyRate;
    String Name;
    Integer Overs,Runs,RunsGiven,Sixes;
    Double StrikeRate;

    public Integer getBalls() {
        return Balls;
    }

    public Integer getBoundaries() {
        return Boundaries;
    }

    public Double getEconomyRate() {
        return EconomyRate;
    }

    public String getName() {
        return Name;
    }

    public Integer getOvers() {
        return Overs;
    }

    public Integer getRuns() {
        return Runs;
    }

    public Integer getRunsGiven() {
        return RunsGiven;
    }

    public Integer getSixes() {
        return Sixes;
    }

    public Double getStrikeRate() {
        return StrikeRate;
    }

    public Integer getWicketsTaken() {
        return WicketsTaken;
    }

    Integer WicketsTaken;
}
