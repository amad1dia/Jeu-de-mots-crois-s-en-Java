package abraham.dia.model;

import javafx.beans.property.StringProperty;

public class Case {

    private StringProperty proposition, solution;
    private String horizontal, vertical;
    private boolean caseNoire;

	public Case() {
	}


    public StringProperty getProposition() {
        return proposition;
    }

    public void setProposition(StringProperty proposition) {
        this.proposition = proposition;
    }

    public StringProperty getSolution() {
        return solution;
    }

    public void setSolution(StringProperty solution) {
        this.solution = solution;
    }

    public String getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(String horizontal) {
        this.horizontal = horizontal;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public boolean isCaseNoire() {
        return caseNoire;
    }

    public void setCaseNoire(boolean caseNoire) {
        this.caseNoire = caseNoire;
    }
}
