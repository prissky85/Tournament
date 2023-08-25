package com.example.tournament;

public class Score {
    Match.Pair matches = new Match.Pair();
    Match.Pair games = new Match.Pair();
    Match.Pair balls = new Match.Pair();

    String getWins() {
        return matches.aPoints + " : " + matches.bPoints;
    }

    String getGames() {
        return games.aPoints + " : " + games.bPoints;
    }

    String getBalls() {
        return balls.aPoints + " : " + balls.bPoints;
    }
}
