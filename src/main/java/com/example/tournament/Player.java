package com.example.tournament;

import javafx.beans.property.SimpleStringProperty;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Player {
    private final int seed;
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private Match first;
    private Match second;
    private Match third;
    private Match fourth;
    private final Score score = new Score();
    private int position = 1;

    public Player() {
        this(0, "");
    }

    public Player(int s, String n) {
        setName(n);
        seed = s;
        first = Main.getMatch(1, s);
        second = Main.getMatch(2, s);
        third = Main.getMatch(3, s);
        fourth = Main.getMatch(4, s);
    }

    public void updateScore() {
        score.matches.aPoints = first.getResult().isWin(true) +
                                second.getResult().isWin(seed > 1 && seed != 4) +
                                third.getResult().isWin(seed > 2) +
                                fourth.getResult().isWin(seed == 2);
        score.matches.bPoints = first.getResult().isWin(false) +
                                second.getResult().isWin(seed <= 1 || seed == 4) +
                                third.getResult().isWin(seed <= 2) +
                                fourth.getResult().isWin(seed != 2);
        score.games.aPoints = first.getResult().getGamesWon(true) +
                              second.getResult().getGamesWon(seed > 1 && seed != 4) +
                              third.getResult().getGamesWon(seed > 2) +
                              fourth.getResult().getGamesWon(seed == 2);
        score.games.bPoints = first.getResult().getGamesWon(false) +
                              second.getResult().getGamesWon(seed <= 1 || seed == 4) +
                              third.getResult().getGamesWon(seed <= 2) +
                              fourth.getResult().getGamesWon(seed != 2);
        score.balls.aPoints = first.getResult().getBallsWon(true) +
                              second.getResult().getBallsWon(seed > 1 && seed != 4) +
                              third.getResult().getBallsWon(seed > 2) +
                              fourth.getResult().getBallsWon(seed == 2);
        score.balls.bPoints = first.getResult().getBallsWon(false) +
                              second.getResult().getBallsWon(seed <= 1 || seed == 4) +
                              third.getResult().getBallsWon(seed <= 2) +
                              fourth.getResult().getBallsWon(seed != 2);
        Main.wins[seed - 1] = score.matches.aPoints;
        if (score.matches.bPoints == 3) {
            Main.matchesData.get(6).setPlayers("... vs " + this.getName());
        }
        if (score.matches.aPoints == 3) {
            Main.matchesData.get(7).setPlayers(this.getName() + " vs ...");
        }
    }

    public void updatePosition() { // TODO: implement sorting
        int[] sortedIndices = Arrays.stream(IntStream.of(Main.wins).toArray())
                                    .distinct()
                                    .boxed()
                                    .sorted(Comparator.comparing(Integer::intValue).reversed())
                                    .mapToInt(ele -> ele)
                                    .toArray();
        position = Arrays.stream(sortedIndices).boxed().toList().indexOf(score.matches.aPoints) + 1;
        System.out.println(getName() + ": position #" + position);
    }

    public int getSeed() {
        return seed;
    }

    public void setName(String n) {
        name.set(n);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public Match getFirst() {
        return first;
    }

    public Match getSecond() {
        return second;
    }

    public Match getThird() {
        return third;
    }

    public Match getFourth() {
        return fourth;
    }

    public String getFirstScore() {
        return first.getResult().getResult(true);
    }

    public String getSecondScore() {
        return second.getResult().getResult(seed > 1 && seed != 4);
    }

    public String getThirdScore() {
        return third.getResult().getResult(seed > 2);
    }

    public String getFourthScore() {
        return fourth.getResult().getResult(seed == 2);
    }

    public void setFirst(Match first) {
        this.first = first;
    }

    public void setSecond(Match second) {
        this.second = second;
    }

    public void setThird(Match third) {
        this.third = third;
    }

    public void setFourth(Match fourth) {
        this.fourth = fourth;
    }

    public String getWins() {
        return score.getWins();
    }

    public String getGames() {
        return score.getGames();
    }

    public String getBalls() {
        return score.getBalls();
    }

    public String getPos() {
        return position > 0 ? String.valueOf(position) : "-";
    }

    static class PlayerComparator implements Comparator<Player> {

        @Override
        public int compare(Player o1, Player o2) {
            int comparison = Integer.compare(o2.score.matches.aPoints, o1.score.matches.aPoints);
            if (comparison == 0) {
                comparison = Integer.compare(o1.score.matches.bPoints, o2.score.matches.bPoints);
            }

            if (comparison == 0) {
                int gameRatio1 = o1.score.matches.bPoints > 0 ? o1.score.games.aPoints/o1.score.matches.bPoints : o1.score.games.aPoints;
                int gameRatio2 = o2.score.matches.bPoints > 0 ? o2.score.games.aPoints/o2.score.matches.bPoints : o2.score.games.aPoints;
                comparison = Integer.compare(gameRatio1, gameRatio2);
            }
            return comparison;
        }
    }
}