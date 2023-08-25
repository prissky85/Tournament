package com.example.tournament;

public class Result {
    String resultString;

    Match.Pair games = new Match.Pair();
    Match.Pair balls = new Match.Pair();

    public void setResult(String resultString) throws NumberFormatException {
        String result = resultString.replaceAll(" ", "");
        if (result.length() > 2) {
            String[] sets = result.split(",");
            if (sets.length < 3 || sets.length > 5) {
                throw new NumberFormatException();
            }
            games = new Match.Pair();
            balls = new Match.Pair();
            for (String set : sets) {
                int points = Math.abs(Integer.parseInt(set));
                if (set.contains("-")) {
                    games.bPoints++;
                    balls.aPoints += points;
                    balls.bPoints += Math.max(11, points + 2);
                } else {
                    games.aPoints++;
                    balls.aPoints += Math.max(11, points + 2);
                    balls.bPoints += points;
                }
            }
            this.resultString = result;
        } else if (result.equals("\\")) {
            this.resultString = result;
        } else {
            throw new NumberFormatException();
        }
    }

    public Result() {
        this.resultString = "0, 0, 0";
    }

    public String getBalls() {
        return resultString;
    }

    public String getResult(boolean reversed) {
        if (resultString.equals("\\")) {
            return "\\";
        }
        return reversed ? games.bPoints + " : " + games.aPoints :
                          games.aPoints + " : " + games.bPoints;
    }

    public int isWin(boolean reversed) {
        int win = games.bPoints < games.aPoints ? 1 : 0;
        int lost = games.bPoints > games.aPoints ? 1 : 0;
        if (reversed) {
            return lost;
        } else {
            return win;
        }
    }

    public int getGamesWon(boolean reversed) {
        return reversed ? games.bPoints : games.aPoints;
    }

    public int getBallsWon(boolean reversed) {
        return reversed ? balls.bPoints : balls.aPoints;
    }
}
