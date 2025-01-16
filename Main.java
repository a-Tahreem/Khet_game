import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int rows = 8, cols = 10;

        // Afficher une boîte de dialogue pour choisir le mode
        String[] options = {"1 joueur", "2 joueurs"};
        int mode = JOptionPane.showOptionDialog(
                null,
                "Choisissez un mode de jeu :",
                "Mode de jeu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        boolean isSinglePlayer = (mode == 0); // Si "1 joueur" est sélectionné

        Damier model = new Damier(rows, cols);
        DamierView view = new DamierView(rows, cols);
        DamierController controller = new DamierController(model, view, isSinglePlayer);
    }
}
