package abraham.dia.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MotsCroisesTP6 extends Grille<Case> implements SpecifMotsCroises {
    //Variables

    public MotsCroisesTP6(int hauteur, int largeur) {
        super(hauteur, largeur);
        for (int i = 1; i <= hauteur; i++) {
            for (int j = 1; j <= largeur; j++) {
                this.setCellule(i, j, new Case());
                this.getCellule(i, j).setProposition(new SimpleStringProperty(" "));
                this.setCaseNoire(i, j, true);
            }
        }
    }

    @Override
    public int getHauteur() {
        return hauteur;
    }

    @Override
    public int getLargeur() {
        return largeur;
    }

    @Override
    public boolean estCaseNoire(int lig, int col) {
        return this.getCellule(lig, col).isCaseNoire();
    }

    @Override
    public void setCaseNoire(int lig, int col, boolean noire) {
        assert coordCorrectes(lig, col) : "Les coordonn�es de la cellules sont incorrectes";
        this.getCellule(lig, col).setCaseNoire(noire);
    }

    @Override
    public char getSolution(int lig, int col) {
        assert !estCaseNoire(lig, col) : "Les coordonn�es de la cellules sont incorrectes";
        return this.getCellule(lig, col).getSolution().get().charAt(0);
    }

    @Override
    public void setSolution(int lig, int col, char sol) {
        assert !estCaseNoire(lig, col) : "Les coordonn�es de la cellules sont incorrectes";
        this.getCellule(lig, col).setSolution(new SimpleStringProperty(String.valueOf(sol)));
    }

    @Override
    public char getProposition(int lig, int col) {
        assert !estCaseNoire(lig, col) : "Les coordonn�es de la cellules sont incorrectes";
        return this.getCellule(lig, col).getProposition().get().charAt(0);
    }

    @Override
    public void setProposition(int lig, int col, char prop) {
        assert !estCaseNoire(lig, col) : "Les coordonn�es de la cellules sont incorrectes";
        this.getCellule(lig, col).setProposition(new SimpleStringProperty(String.valueOf(prop)));
    }

    @Override
    public String getDefinition(int lig, int col, boolean horiz) {
        if (horiz)
            return this.getCellule(lig, col).getHorizontal();
        else
            return this.getCellule(lig, col).getVertical();
    }

    @Override
    public void setDefinition(int lig, int col, boolean horiz, String def) {
        if (horiz)
            this.getCellule(lig, col).setHorizontal(def);
        else
            this.getCellule(lig, col).setVertical(def);
    }

    public void reveler(int lig, int col) {
        Case gCase = this.getCellule(lig, col);
        char sol = gCase.getSolution().get().charAt(0);
        this.getCellule(lig, col).getProposition().set(String.valueOf(sol));
    }

    public StringProperty propositionProperty(int lig, int col) {
        return this.getCellule(lig, col).getProposition();
    }
}
