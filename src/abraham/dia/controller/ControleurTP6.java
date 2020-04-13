package abraham.dia.controller;


import abraham.dia.model.ChargerGrille;
import abraham.dia.model.MotsCroisesTP6;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ControleurTP6 {
    @FXML
    public GridPane monGridPane;
    @FXML
    public ProgressBar progressBar;
    private MotsCroisesTP6 motsCroises;
    private TextField[][] textFields;
    private boolean randomGrid;
    private int gridNumber;
    private float progressFull = 0;
    private float progress = 0;
    private List<TextField> solution;


    public ControleurTP6() {
    }

    public ControleurTP6(boolean randomGrid, int gridNumber) {
        this.randomGrid = randomGrid;
        this.gridNumber = gridNumber;


    }

    @FXML
    public void initialize() {
        ChargerGrille cg = new ChargerGrille();
        try {
            if (randomGrid) {
                motsCroises = cg.extraireGrille(getRandomNumber());
            } else {
                motsCroises = cg.extraireGrille(gridNumber);
            }
            initializeGrid(motsCroises);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initializeGrid(MotsCroisesTP6 motsCroises) throws Exception {
        if (motsCroises == null)
            throw new Exception("Grille non existante");
        int hauteur = motsCroises.getHauteur();
        int largeur = motsCroises.getLargeur();
        //initialiser un tableau de textfields pour changer le focus après
        textFields = new TextField[largeur][hauteur];
        solution = new ArrayList<>();
        TextField modele = (TextField) monGridPane.getChildren().get(0);
        monGridPane.getChildren().clear();
        for (int lig = 1; lig <= motsCroises.getHauteur(); lig++) {
            for (int col = 1; col <= motsCroises.getLargeur(); col++) {
                if (!motsCroises.estCaseNoire(lig, col)) {
                    TextField newTextField = new TextField();
                    newTextField.setPrefWidth(modele.getPrefWidth());
                    newTextField.setPrefHeight(modele.getPrefHeight());
                    for (Object cle : modele.getProperties().keySet()) {
                        newTextField.getProperties().put(cle, modele.getProperties().get(cle));
                    }
                    //Ajouter le textfield a monPridPane
                    monGridPane.add(newTextField, col - 1, lig - 1);
                    textFields[col - 1][lig - 1] = newTextField;
                    ++progressFull;
                }
            }
        }
        gridProperties(motsCroises);
    }

    private void gridProperties(MotsCroisesTP6 motsCroises) {
        for (Node n : monGridPane.getChildren()) {
            if (n instanceof TextField) {
                TextField tf = (TextField) n;
                tf.addEventFilter(KeyEvent.KEY_TYPED, maxLength(1));
                int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
                int col = ((int) n.getProperties().get("gridpane-column")) + 1;
                // tf.textProperty().bindBidirectional(motsCroises.propositionProperty(lig, col));
                //On met la solution du motsCroisés dans le champ de tf
//                tf.textProperty().bindBidirectional(motsCroises.getCellule(lig, col).getSolution());
                //Lier le champ de texte de la grille aux cases des mots croisés
                motsCroises.getCellule(lig, col).setProposition(tf.textProperty());
                tf.textProperty().addListener((observableValue, oldVaue, newValue) -> {
                    if (!"".equals(newValue)) {
                            ScaleTransition  transition=new ScaleTransition(Duration.millis(100),tf) ;
                            transition.setFromX(.5);
                            transition.setFromY(.5);
                            transition.setToX(1);
                            transition.setToY(1);
                            transition.play();
                        //SI le caractère est compris entre a et Z avancer le curseur
                        if (charBetweenAanZ(newValue.charAt(0))) {
                            changeFocus(lig - 1, col, 0);
                            //Update progress
                            ++progress;
                            updateProgressBar();
                        }
                        if (tf.textProperty().get().charAt(0) == motsCroises.getSolution(lig, col)) {
                            solution.add(tf);
                        }
                    } else {
                        --progress;
                        if (progress >= 0)
                            updateProgressBar();
                    }
                });
                //Afficher la définition horiz et verticale séparées par / sous forme d'infobulles
                showTooltip(tf, motsCroises.getDefinition(lig, col, true) + " /"
                        + motsCroises.getDefinition(lig, col, false));
                tf.setOnMouseClicked(this::clicCase);
                tf.setOnKeyPressed(keyEvent -> {
                    keyPressed(keyEvent, lig - 1, col - 1);
                });


            }
        }

    }

    private void keyPressed(KeyEvent e, int lig, int col) {
        TextField textField = textFields[col][lig];
        switch (e.getCode()) {
            case UP:
                //Si la case à gauche n'est pas nulle déplacer le curseur
                changeFocus(lig, col, 1);
                break;
            case DOWN:
                //Si la case en bas n'est pas nulle déplacer le curseur
                changeFocus(lig + 1, col, 0);
                break;
            case LEFT:
                //Si la case à gauche n'est pas nulle déplacer le curseur
                changeFocus(lig, col - 1, 0);
                break;
            case RIGHT:
                //Si la case a droite n'est pas nulle déplacer le curseur
                changeFocus(lig, col + 1, 0);
                break;
            case ENTER:
                for (TextField tf : solution) {
                    tf.getStyleClass().add("solution");
                }
                break;
            case BACK_SPACE:
                //SI on clique sur la touche arrière effacer le contenu du champ de texte et réculer le curseur
                textField.clear();
                changeTextFieldStyle(textField, "-fx-control-inner-background : rgb(255,255, 255)");
                changeFocus(lig, col - 1, 0);
                break;
        }

    }

    private void updateProgressBar() {
        float progression = progress / progressFull;
        progressBar.setProgress(progression);
    }

    private void changeTextFieldStyle(TextField textField, String s) {
        textField.setStyle(s);
    }

    /**
     * Change le focus du saisi
     *
     * @param lig
     * @param col
     * @param offset
     */
    private void changeFocus(int lig, int col, int offset) {
        if (lig >= offset && col >= 0 && (col < motsCroises.getHauteur() && lig < motsCroises.getLargeur())) {
            TextField textField = textFields[col][lig - offset];
            if (textField != null) {
                textField.requestFocus();
            }
        }
    }

    @FXML
    public void clicCase(MouseEvent e) {
        TextField textField = (TextField) e.getSource();
        //Show solution when double click
        if (e.getButton().equals(MouseButton.PRIMARY)) {
            if (e.getClickCount() == 2) {
                int lig = ((int) textField.getProperties().get("gridpane-row")) + 1;
                int col = ((int) textField.getProperties().get("gridpane-column")) + 1;
                motsCroises.reveler(lig, col);
                changeFocus(lig - 1, col, 0);
                changeTextFieldStyle(textField, "-fx-control-inner-background : rgb(85,255, 51)");

            }
        }
    }


    public void showTooltip(TextField textField, String texte) {
        textField.setTooltip(new Tooltip(texte));
    }

    public EventHandler<KeyEvent> maxLength(final Integer i) {
        return event -> {
            TextField tx = (TextField) event.getSource();
            if (tx.getText().length() >= i) {
                event.consume();
            }
        };

    }

    private int getRandomNumber() {
        //Générer un nombre aléatoire compris entre 1 et 12
        return 1 + (int) (Math.random() * 12);
    }

    private boolean charBetweenAanZ(char ch) {
        return (ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122);
    }

}

