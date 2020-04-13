package abraham.dia.test;

import abraham.dia.model.ChargerGrille;
import abraham.dia.model.MotsCroisesTP6;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class ChargerGrilleTest {
    private static Connection connection;

    @BeforeAll
    static void setUp() {
        try {
            connection = ChargerGrille.connecterBD();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void grillesDisponibles() {

    }

    @Test
    void extraireGrille() throws SQLException {
        String controle = null;
        int numGrille = 10;
        StringBuilder solution = new StringBuilder();
        int hauteur = 0;
        int largeur = 0;
        ChargerGrille cg = new ChargerGrille();
        MotsCroisesTP6 mc = cg.extraireGrille(numGrille);

        String sql = "SELECT * FROM tp5_grille where num_grille = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, numGrille);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                controle = rs.getString("controle");
                hauteur = rs.getInt("hauteur");
                largeur = rs.getInt("largeur");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int l = 1; l <= hauteur; l++) {
            for (int c = 1; c <= largeur; c++) {
                if (mc.estCaseNoire(l, c))
                    solution.append("*");
                else
                    solution.append(mc.getSolution(l, c));
            }

        }

        assertEquals("Mot croisé correct", solution.toString().toUpperCase(), controle);
        solution.append("failed test");
        assertNotEquals("La solution est differente du controle", solution.toString(), controle);
    }
}