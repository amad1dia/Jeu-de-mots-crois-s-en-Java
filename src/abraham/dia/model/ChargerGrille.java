package abraham.dia.model;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ChargerGrille {

    private Connection connexion;

    public ChargerGrille() {
        try {
            connexion = connecterBD();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection connecterBD() throws SQLException {

        Connection connect;

//        connect = DriverManager.getConnection("jdbc:mysql://mysql.istic.univ-rennes1.fr/base_bousse", "user_amdia", "123456");
        connect = DriverManager.getConnection("jdbc:mysql://localhost/tp2_proga", "root", "");

        return connect;
    }

    // Retourne la liste des grilles disponibles dans la B.D.
    // Chaque grille est d�crite par la concat�nation des valeurs
    // respectives des colonnes nom_grille, hauteur et largeur.
    // L��l�ment de liste ainsi obtenu est index� par le num�ro de
    // la grille (colonne num_grille).
    // Ainsi "Fran�ais d�butants (7x6)" devrait �tre associ� � la cl� 10

    public Map<Integer, String> grillesDisponibles() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {
            Statement stmt = connexion.createStatement();
            String sql = "SELECT * FROM TP5_GRILLE";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int num_grille = rs.getInt("num_grille");
                String nom_grille = rs.getString("nom_grille");
                map.put(num_grille, nom_grille);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public MotsCroisesTP6 extraireGrille(int numGrille) throws SQLException{
        MotsCroisesTP6 mc = null;
        String mot = "SELECT * FROM TP5_GRILLE WHERE num_grille = ?";
        PreparedStatement pstmt = connexion.prepareStatement(mot);
        pstmt.setInt(1, numGrille);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int hauteur = rs.getInt("hauteur");
            int largeur = rs.getInt("largeur");
            mc = new MotsCroisesTP6(hauteur, largeur);
            String grille = "SELECT * FROM TP5_MOT WHERE num_grille = ?";
            PreparedStatement grillePstmt = connexion.prepareStatement(grille);
            grillePstmt.setInt(1, numGrille);
            ResultSet listMots = grillePstmt.executeQuery();
            while (listMots.next()) {
                String definition = listMots.getString("definition");
                String solution = listMots.getString("solution");
                int ligne = listMots.getInt("ligne");
                int colonne = listMots.getInt("colonne");
                boolean horizontal = listMots.getInt("horizontal") == 1;
                rendreCaseBlance(mc, ligne, colonne);
                mc.setDefinition(ligne, colonne, horizontal, definition);
                mc.setSolution(ligne, colonne, solution.charAt(0));
                for (int i = 1; i < solution.length(); i++) {
                    char sol = solution.charAt(i);
                    if (horizontal) {
                        colonne++;
                        rendreCaseBlance(mc, ligne, colonne);
                        mc.setSolution(ligne, colonne, sol);
                    } else {
                        ligne++;
                        rendreCaseBlance(mc, ligne, colonne);
                        mc.setSolution(ligne, colonne, sol);
                    }
                }
            }
        }

        return mc;
    }

    /**
     * Si la case est noire la rendre blance avant de mettre la définition ou la solution
     *
     * @param mc      le mot
     * @param ligne   la ligne du mot
     * @param colonne la colonne du mot
     */
    private void rendreCaseBlance(MotsCroisesTP6 mc, int ligne, int colonne) {
        mc.setCaseNoire(ligne, colonne, false);
    }

    public void insertGrille(int numGrille, String nomGrille, int hauteur, int largeur) {
        try {
            String s = "INSERT INTO TP5_GRILLE VALUES (?,?,?,?)";
            PreparedStatement pstmt = connexion.prepareStatement(s);
            pstmt.setInt(1, numGrille);
            pstmt.setString(2, nomGrille);
            pstmt.setInt(3, hauteur);
            pstmt.setInt(4, largeur);
            int i = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChargerGrille cg = new ChargerGrille();
        Map<Integer, String> mappy = cg.grillesDisponibles();
        for (String grille : mappy.values()) {
//            System.out.println(grille);
        }
//        MotsCroises mots = cg.extraireGrille(10);
//        System.out.println("(" + mots.getHauteur() + "x" + mots.getLargeur() + ")");
//        cg.insertGrille(16, "Pomme", 12, 15);
//        MotsCroisesTP6 mc = cg.extraireGrille(1);
//        System.out.println(mc);
    }
}
