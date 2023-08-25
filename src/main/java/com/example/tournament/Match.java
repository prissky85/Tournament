package com.example.tournament;

import javafx.beans.property.SimpleStringProperty;

public class Match {

    private static Match placeHolder;

    private final SimpleStringProperty order = new SimpleStringProperty("");
    private final SimpleStringProperty time = new SimpleStringProperty("");
    private final SimpleStringProperty players = new SimpleStringProperty("");
    private String id;
    private Result result = new Result();

    static Match getPlaceHolder() {
        if (placeHolder == null) {
            placeHolder = new Match();
            Result dummyResult = new Result();
            dummyResult.setResult("\\");
            placeHolder.setResult(dummyResult);
        }
        return placeHolder;
    }

    public Match(String order, String id) {
        setOrder(order);
        setID(id);
    }

    public Match() {
    }

    public String getOrder() {
        return order.get();
    }

    public SimpleStringProperty orderProperty() {
        return order;
    }

    public void setOrder(String order) {
        this.order.set(order);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getPlayers() {
        return players.get();
    }

    public SimpleStringProperty playersProperty() {
        return players;
    }

    public void setPlayers(String players) {
        this.players.set(players);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getBalls() {
        return result.getBalls();
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static class Pair {
        int aPoints;
        int bPoints;
    }
}
