package com.example.tournament;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class Main extends Application {

    static Match[] matches = new Match[] {
            new Match("1.", "14"),
            new Match("2.", "23"),
            new Match("3.", "13"),
            new Match("4.", "42"),
            new Match("5.", "12"),
            new Match("6.", "34"),
            new Match("Bronze", "bronze"),
            new Match("Final", "final")};
    static Player[] players = new Player[] {
            new Player(1, "Player1"),
            new Player(2, "Player2"),
            new Player(3, "Player3"),
            new Player(4, "Player4")};
    static final ObservableList<Player> playersData = FXCollections.observableArrayList(players);
    static final ObservableList<Match> matchesData = FXCollections.observableArrayList(matches);
    static LocalTime start = LocalTime.parse("16:55");
    static int[] wins = new int[4];
    static int[] positions = new int[4];
    static int remainingGames = 8;
    static List<Player> standing = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tournament.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 640);
        stage.setTitle("Tournament");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Match getMatch(int playerA, int playerB) {
        if (Objects.equals(playerA, playerB)) {
            return Match.getPlaceHolder();
        }
        Optional<Match> match = Arrays.stream(matches)
                                      .filter(m -> m.getId().contains(String.valueOf(playerA)) &&
                                                   m.getId().contains(String.valueOf(playerB)))
                                      .findAny();
        return match.orElse(null);
    }
}