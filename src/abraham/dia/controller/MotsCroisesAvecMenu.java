package abraham.dia.controller;

import abraham.dia.model.ChargerGrille;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


public class MotsCroisesAvecMenu {

    @FXML
    public Button randomGridButton;
    @FXML
    public Button chooseGridButton;
    @FXML
    public Button exitButton;

    private boolean isRandom = false;
    private int numGrid;

    @FXML
    public void initialize() {
        //Selon le bouton choisi faire l'action correspondante
        exitButton.setOnMouseClicked(mouseEvent -> {
            closeButtonAction();
        });
        randomGridButton.setOnMouseClicked(mouseEvent -> {
            isRandom = true;
            launchGrid();
        });

        chooseGridButton.setOnMouseClicked(mouseEvent -> {
            //Afficher un dialogue de choix des grille
            isRandom =false;
            showInputDialog();
        });

    }

    @FXML
    private void closeButtonAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    private void showInputDialog() {
        //Créer une liste de grille

        ChargerGrille chargerGrille = new ChargerGrille();
        //On charge la liste des grille disponible de la base
        Map<Integer, String> grilles = chargerGrille.grillesDisponibles();

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, grilles.keySet());
        dialog.setTitle("Choix d'une grille");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisir un numéro de grille :");
        Optional<Integer> result = dialog.showAndWait();
        //Si le resultat existe le mettre dans numgrid (Java 8 style)
        result.ifPresent(grid -> {
            //lancer la grille selon le numéro de grille sélectionner
            numGrid = grid;
            launchGrid();
        });
    }

    private void launchGrid() {
        try {
            //Lancer la grille en chargeant la vue correspondante
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/VueTP6.fxml"));
            ControleurTP6 controleur = new ControleurTP6(isRandom, numGrid);
            fxmlLoader.setController(controleur);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Mots croisés => ABRAHAM & DIA");
            stage.setScene(scene);
            stage.show();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


}
