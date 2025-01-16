import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class DamierView extends JFrame {

    private JButton[][] buttons; // Boutons du damier
    private JButton laserButton0; // enleve les laser de la carte
    private JButton laserButton1; // Laser pour joueur jaune
    private JButton laserButton2; // Laser pour joueur rouge
    private JLabel currentPlayerLabel; // label pour indiquer le joueur
    private boolean shootlaserJaune; // boolean qui permet de savoir si le laser est activer
    private boolean shootlaserRouge; // boolean qui permet de savoir si le laser est activer

    public DamierView(int rows, int cols) {
        setTitle("Jeu Khet");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorderPainted(true);
    
                // Définir une taille explicite pour les boutons
                buttons[i][j].setPreferredSize(new Dimension(64, 64));
                buttons[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                buttons[i][j].setVerticalAlignment(SwingConstants.CENTER);


                gridPanel.add(buttons[i][j]);
            }
        }
        // Bouttons et labels
        laserButton0 = new JButton("Enlever les lasers");
        laserButton1 = new JButton("Laser Jaune");
        laserButton2 = new JButton("Laser Rouge");
        currentPlayerLabel = new JLabel("C'est au tour du joueur Jaune");

        JPanel laserPanel = new JPanel();
        laserPanel.add(laserButton0);
        laserPanel.add(laserButton1);
        laserPanel.add(laserButton2);

        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);
        add(laserPanel, BorderLayout.SOUTH);
        laserPanel.add(currentPlayerLabel);

        setVisible(true);
    }


    public void updateGrid(int[][] grid, String[][] pieces, HashMap<String, ImageIcon> pieceImages, String[][] cellColor) {
        //
        for (int i = 0; i < cellColor.length; i++) {
            for (int j = 0; j < cellColor[i].length; j++) {
                String color = cellColor[i][j];
                if (color.equals("Rouge")) {
                    buttons[i][j].setBackground(new Color(155,0,0));
                } else if (color.equals("Jaune")) {
                    buttons[i][j].setBackground(new Color(255,255,155));
                } else {
                    buttons[i][j].setBackground(Color.WHITE);
                }
            }
        }

        //
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                JButton button = buttons[i][j];

                if (pieces[i][j] != null) {
                    // Si une pièce est présente, afficher l'image correspondante
                    ImageIcon icon = pieceImages.get(pieces[i][j]);
                    if (icon != null) {
                        button.setIcon(icon);
                    } else {
                        System.out.println("Image introuvable pour : " + pieces[i][j]);
                        button.setIcon(null);
                    }
                    button.setBackground(Color.WHITE);

                    // Ajouter une bordure en fonction du joueur
                    if (grid[i][j] == 1) {
                        button.setBackground(new Color(230,230,0));
                    } else if (grid[i][j] == 2) {
                        button.setBackground(new Color(215,0,0)); // un rouge un peu foncer
                    } else {
                        button.setBackground(Color.WHITE);
                    }
                } else {
                    // Si la case est vide, gérer la couleur du laser et la bordure
                    button.setIcon(null);

                    if (grid[i][j] == 1) {
                        // Case traversée par le laser jaune
                        button.setBackground(Color.YELLOW);
                        button.setBorder(new LineBorder(Color.BLACK, 1)); // Bordure par défaut
                    } else if (grid[i][j] == 2) {
                        // Case traversée par le laser rouge
                        button.setBackground(Color.RED);
                        button.setBorder(new LineBorder(Color.BLACK, 1)); // Bordure par défaut
                    } else {
                        // Case vide, sans laser et cases nob reserver
                        if (cellColor[i][j].equals("Neutre")) {
                        button.setBackground(Color.WHITE);
                        button.setBorder(new LineBorder(Color.BLACK, 1)); // Enlever la bordure
                        }
                    }
                }
            }
        }
    }


    public void updateCurrentPlayerLabel(int currentPlayer) {
        if (currentPlayer == 1) {
            currentPlayerLabel.setText("C'est au tour du joueur Jaune");
            currentPlayerLabel.setForeground(new Color(240,240,0));
        } else {
            currentPlayerLabel.setText("C'est au tour du joueur Rouge");
            currentPlayerLabel.setForeground(Color.RED);
        }
    }

    public void showGameOverMessage(int winningPlayer) {
        String playerColor = (winningPlayer == 1) ? "Jaune" : "Rouge";
        JOptionPane.showMessageDialog(
                this,
                "Pharaon touché ! Le joueur " + playerColor + " gagne !",
                "Fin de la partie",
                JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0); // Terminer le programme
    }

    // Getter & Setter
    public boolean isShootlaserJaune() {
        return shootlaserJaune;
    }

    public void setShootlaserJaune(boolean shootlaserJaune) {
        this.shootlaserJaune = shootlaserJaune;
    }

    public boolean isShootlaserRouge() {
        return shootlaserRouge;
    }

    public void setShootlaserRouge(boolean shootlaserRouge) {
        this.shootlaserRouge = shootlaserRouge;
    }

    public JButton[][] getButtons() {
        return buttons;
    }

    public JButton getLaserButton0() {
        return laserButton0;
    }

    public JButton getLaserButton1() {
        return laserButton1;
    }

    public JButton getLaserButton2() {
        return laserButton2;
    }

}