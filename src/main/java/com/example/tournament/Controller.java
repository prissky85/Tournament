package com.example.tournament;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.tournament.Main.*;

public class Controller implements Initializable {
    @FXML public TableView matchesTable;
    @FXML public TableView groupTable;
    @FXML public TableColumn seed;
    @FXML public TableColumn name;
    @FXML public TableColumn wins;
    @FXML public TableColumn games;
    @FXML public TableColumn balls;
    @FXML public TableColumn pos;
    @FXML public TableColumn p1;
    @FXML public TableColumn p2;
    @FXML public TableColumn p3;
    @FXML public TableColumn p4;
    @FXML public TableColumn time;
    @FXML public TableColumn order;
    @FXML public TableColumn players;
    @FXML public TableColumn res;

    private static final System.Logger LOGGER = System.getLogger("log");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGroup();
        initializeMatches();
        updateMatchTimes();
        updateMatches();

        Platform.runLater(() -> {
            matchesTable.getFocusModel().focus(0, res);
            matchesTable.requestFocus();
        });
    }

    private void initializeMatches() {
        matchesTable.setItems(matchesData);
        matchesTable.setFixedCellSize(25);
        matchesTable.prefHeightProperty()
                .bind(Bindings.size(matchesTable.getItems())
                        .multiply(matchesTable.getFixedCellSize())
                        .add(30));
        order.setStyle("-fx-alignment: CENTER;");
        order.setMinWidth(50);
        order.setMaxWidth(50);

        makeStartTimeEditable();
        res.setCellValueFactory(new PropertyValueFactory<String, String>("balls"));

        makeResultsEditable();

        players.prefWidthProperty().bind(matchesTable.widthProperty().subtract(205));
        name.prefWidthProperty().bind(groupTable.widthProperty().subtract(532));
    }

    private void makeStartTimeEditable() {
        Callback<TableColumn<Match, String>, TableCell<Match, String>> defaultTextFieldCellFactory = TextFieldTableCell.forTableColumn();
        time.setCellFactory(col -> {
            TableCell<Match, String> cell = defaultTextFieldCellFactory.call((TableColumn<Match, String>) col);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow<Match> row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    Match item = cell.getTableRow().getItem();
                    if (item == null) {
                        cell.setEditable(false);
                    } else {
                        cell.setEditable(item.getOrder().equals("1."));
                    }
                }
            });
            return cell ;
        });

        time.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) t -> {
            start = LocalTime.parse(t.getNewValue().toString());
            updateMatchTimes();
        });
    }

    private void initializeGroup() {
        Main.standing.addAll(Arrays.stream(Main.players).toList());
        groupTable.setItems(playersData);
        groupTable.setFixedCellSize(25);
        groupTable.prefHeightProperty()
                  .bind(Bindings.size(groupTable.getItems())
                                .multiply(groupTable.getFixedCellSize())
                                .add(30));
        seed.setMinWidth(25);
        seed.setMaxWidth(25);

        p1.setCellValueFactory(new PropertyValueFactory<String, String>("firstScore"));
        p2.setCellValueFactory(new PropertyValueFactory<String, String>("secondScore"));
        p3.setCellValueFactory(new PropertyValueFactory<String, String>("thirdScore"));
        p4.setCellValueFactory(new PropertyValueFactory<String, String>("fourthScore"));
        wins.setCellValueFactory(new PropertyValueFactory<String, String>("wins"));
        games.setCellValueFactory(new PropertyValueFactory<String, String>("games"));
        balls.setCellValueFactory(new PropertyValueFactory<String, String>("balls"));
        pos.setCellValueFactory(new PropertyValueFactory<String, String>("pos"));

        prepareGroup();
        makeNamesEditable();
    }

    private void makeResultsEditable() {
        res.setCellFactory(TextFieldTableCell.forTableColumn());
        res.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) t -> {
            Match match = (Match) t.getTableView()
                                   .getItems()
                                   .get(t.getTablePosition().getRow());
            if (t.getTablePosition().getRow() > 8 - remainingGames) {
                showAlertAndWait(matchesTable, "First, write down the result of match #" +
                                                (9 - remainingGames) + ", please.");
                return;
            }
            try {
                match.getResult()
                     .setResult(t.getNewValue().toString());
            } catch (Exception e) {
                showAlertAndWait(matchesTable, "Use the predetermined format of scores (best of five games) separated by comas, please.");
                return;
            }
            updateResults(match);
            updateRemaining();
        });
    }

    private void showAlertAndWait(TableView tableView, String message) {
        tableView.refresh();
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(550);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    private static void updateRemaining() {
        int finished = 0;
        for (Match matchData : matchesData) {
            if (!Objects.equals(matchData.getResult().getResult(false), "0 : 0")) {
                finished++;
            }
        }
        remainingGames = 8 - finished;
        LOGGER.log(System.Logger.Level.INFO, "Remaining games: " + remainingGames);
    }

    private void updateResults(Match match) {
//        if (!match.getPlayers().contains(" vs ") ||
//            playersData.stream().noneMatch(p -> p.getName().equals(match.getPlayers().substring(0, match.getPlayers().indexOf(" vs ")))) ||
//            playersData.stream().noneMatch(p -> p.getName().equals(match.getPlayers().substring(match.getPlayers().indexOf(" vs ") + 4)))) {
//            System.out.println("premature logging"); // Replace by LOGGER
//            return;
//        }
        String player1 = match.getPlayers().substring(0, match.getPlayers().indexOf(" vs "));
        String player2 = match.getPlayers().substring(match.getPlayers().indexOf(" vs ") + 4);
        playersData.stream().filter(p -> p.getName().equals(player1)).forEach(Player::updateScore);
        playersData.stream().filter(p -> p.getName().equals(player2)).forEach(Player::updateScore);
        standing.sort(new Player.PlayerComparator());
        playersData.forEach(Player::updatePosition);
        groupTable.refresh();
    }

    private void makeNamesEditable() {
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) t -> {
            ((Player) t.getTableView()
                       .getItems()
                       .get(t.getTablePosition().getRow())).setName(t.getNewValue().toString());
            updateMatches();
        });
        updateFinalMatches();
    }

    private static void updateMatchTimes() {
        for (int i = 0; i < 8; i++) {
            matchesData.get(i).setTime(start.plusMinutes(30L * i).toString());
        }
    }

    private static void updateMatches() {
        matchesData.get(0).setPlayers(playersData.get(0).getName() + " vs " + playersData.get(3).getName());
        matchesData.get(1).setPlayers(playersData.get(1).getName() + " vs " + playersData.get(2).getName());
        matchesData.get(2).setPlayers(playersData.get(0).getName() + " vs " + playersData.get(2).getName());
        matchesData.get(3).setPlayers(playersData.get(3).getName() + " vs " + playersData.get(1).getName());
        matchesData.get(4).setPlayers(playersData.get(0).getName() + " vs " + playersData.get(1).getName());
        matchesData.get(5).setPlayers(playersData.get(2).getName() + " vs " + playersData.get(3).getName());
    }

    private static void updateFinalMatches() {
        matchesData.get(6).setPlayers( "yet to be decided" );
        matchesData.get(7).setPlayers( "yet to be decided" );
    }

    private static void prepareGroup() {
        playersData.get(0).setFirst(Match.getPlaceHolder());
        playersData.get(1).setSecond(Match.getPlaceHolder());
        playersData.get(2).setThird(Match.getPlaceHolder());
        playersData.get(3).setFourth(Match.getPlaceHolder());
    }
}