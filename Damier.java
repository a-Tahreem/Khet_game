import java.awt.*;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Damier {

    private int[][] grid; // Grille pour gérer les lasers (0 = vide, 1 = jaune, 2 = rouge)
    private String[][] pieces; // Grille pour gérer les pions
    private HashMap<String, ImageIcon> pieceImages; // Images des pions
    private String[][] cellColors;// Stocke la couleur des cases du damier
    private boolean gameOver; // Indique si le jeu est terminé
    private int winningPlayer; // Stocke le joueur gagnant 

    public Damier(int rows, int cols) {
        grid = new int[rows][cols];
        pieces = new String[rows][cols];
        cellColors = new String[rows][cols];

        initializeCellColors();
        initializeGrid();
        loadPieceImages();
        initializeDefaultSetup();
    }

    private void initializeGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0; // Case vide
                pieces[i][j] = null; // Pas de pion
            }
        }
    }

    private void loadPieceImages() {
        pieceImages = new HashMap<>();
        String[] pieceNames = {
                "Pharaon",
                 "Pyramide-SE", "Pyramide-EN", "Pyramide-NO", "Pyramide-OS",
                 "Djed1","Djed2",
                 "Horus1","Horus2",
                "Obelisque_seul", "Obelisque_double"
        };
    
        for (String piece : pieceNames) {
            // Charger l'image
            ImageIcon originalIcon = new ImageIcon("resources/" + piece + ".png");
            
            // Redimensionner l'image à 40x40 pixels (ou ajuste selon la taille des boutons)
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 82, Image.SCALE_SMOOTH);
            
            // Ajouter l'image redimensionnée au HashMap
            pieceImages.put(piece, new ImageIcon(scaledImage));
        }
    }

    public void initializeDefaultSetup() {

        // Placement des pièces du joueur jaune (1)
        setPiece(7, 4, "Pharaon",1);

        setPiece(3, 2, "Pyramide-OS",1);

        setPiece(2, 3, "Pyramide-NO",1);

        setPiece(4, 4, "Djed1",1);

        setPiece(4, 5, "Horus1",1);

        setPiece(7, 3, "Obelisque_seul",1);

        setPiece(7, 5, "Obelisque_seul",1);

        setPiece(7, 2, "Pyramide-NO",1);

        setPiece(4, 2, "Pyramide-NO",1);

        setPiece(3, 9, "Pyramide-NO",1);

        setPiece(4, 9, "Pyramide-OS",1);

        setPiece(6, 7, "Pyramide-EN",1);

        // Placement des pièces du joueur rouge (2)
        setPiece(0, 5, "Pharaon",2);

        setPiece(0, 4, "Obelisque_seul",2);

        setPiece(0, 6, "Obelisque_seul",2);

        setPiece(0, 7, "Pyramide-SE",2);

        setPiece(1, 2, "Pyramide-OS",2);

        setPiece(3, 5, "Djed1",2);

        setPiece(3, 4, "Horus1",2);

        setPiece(3, 7, "Pyramide-SE",2);

        setPiece(4, 7, "Pyramide-EN",2);

        setPiece(5, 6, "Pyramide-SE",2);

        setPiece(1, 2, "Pyramide-OS",2);

        setPiece(3, 0, "Pyramide-EN",2);

        setPiece(4, 0, "Pyramide-SE",2);
    }
    private void initializeCellColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                cellColors[i][j] = "Neutre"; // Par défaut, neutre
            }
        }

        // Colonnes fixes
        for (int i = 0; i < 8; i++) {
            cellColors[i][0] = "Rouge";
            cellColors[i][9] = "Jaune";
        }

        // Coins spécifiques
        cellColors[0][1] = "Jaune";
        cellColors[7][1] = "Jaune";
        cellColors[0][8] = "Rouge";
        cellColors[7][8] = "Rouge";
    }

    public void setPiece(int row, int col, String piece, int player) {
        String expectedColor = (player == 1) ? "Jaune" : "Rouge";
        if (!cellColors[row][col].equals(expectedColor) && !cellColors[row][col].equals("Neutre")) {
            System.out.println("Impossible de placer une pièce ici : cette case est réservée a votre adversaire.");
            return;
        }

        if (pieces[row][col] == null) {
            pieces[row][col] = piece;
            grid[row][col] = player;
        } else if (piece.equals("Obelisque_double_tuer")) {
            pieces[row][col] = null;
        } else if (piece.equals("obelisque_cible")) {
            pieces[row][col] = null;
        } else if (piece.equals("obelisque_deplacer")) {
            pieces[row][col] = null;
            grid[row][col] = 0;
        } else {
            System.out.println("La case [" + row + ", " + col + "] est déjà occupée.");
        }
    }

    public void shootLaser(int startRow, int startCol, int direction, int player, boolean booladdlaser) {
        int dr = 0, dc = 0;
        if(!booladdlaser) {
            resetLaserGridForPlayer(player);
        }
        // Définir les déplacements en fonction de la direction
        switch (direction) {
            case 0: dr = -1; break; // Haut
            case 1: dc = 1; break;  // Droite
            case 2: dr = 1; break;  // Bas
            case 3: dc = -1; break; // Gauche
        }

        // Parcours du laser
        while (startRow >= 0 && startRow < grid.length && startCol >= 0 && startCol < grid[0].length) {
            String piece = getPiece(startRow, startCol);

            if (piece != null) {
                String laserColor = (player == 1) ? "Jaune" : "Rouge";
                System.out.println("Laser " + laserColor + " touche " + piece + " en [" + startRow + ", " + startCol + "]");

                // Gérer l'effet en fonction du type de pièce
                if (piece.startsWith("Pyramide")) {
                    // Calculer la réflexion selon l'orientation
                    direction = reflectLaserPyramide(piece, direction);
                    if (direction == -1) {
                        pieces[startRow][startCol] = null; // la pièce a été "tuer"
                        System.out.println("Laser absorbé par la Pyramide à [" + startRow + ", " + startCol + "].");
                        break; // Laser absorbé
                    }
                    else{
                        dr=0;
                        dc=0;
                        switch (direction) {
                            case 0: dr = -1; break; // Haut
                            case 1: dc = 1; break;  // Droite
                            case 2: dr = 1; break;  // Bas
                            case 3: dc = -1; break; // Gauche
                        }

                    }

                } else if (piece.startsWith("Obelisque_seul")) {
                    // Obélisque détruit
                    pieces[startRow][startCol] = null;
                    System.out.println("Obélisque détruit à [" + startRow + ", " + startCol + "]");
                    break; // Laser s'arrête

                } else if (piece.startsWith("Obelisque_double")) {
                    pieces[startRow][startCol] = "Obelisque_seul";
                    System.out.println("Obélisque  double détruit à [" + startRow + ", " + startCol + "] et il devient un obélisque simple");
                    break; // Laser s'arrête

                }else if (piece.startsWith("Djed")) {
                    direction =reflectLaserDjed(piece, direction);
                    dr=0;
                    dc=0;
                    switch (direction) {
                        case 0: dr = -1; break; // Haut
                        case 1: dc = 1; break;  // Droite
                        case 2: dr = 1; break;  // Bas
                        case 3: dc = -1; break; // Gauche
                    }
                }else if (piece.startsWith("Horus")) {
                    direction =reflectLaserHorus(piece,direction,startRow,startCol,player);
                    dr=0;
                    dc=0;
                    switch (direction) {
                        case 0: dr = -1; break; // Haut
                        case 1: dc = 1; break;  // Droite
                        case 2: dr = 1; break;  // Bas
                        case 3: dc = -1; break; // Gauche
                }
                } else if (piece.startsWith("Pharaon")) {
                    // Pharaon touché, fin de partie
                    gameOver = true;
                    winningPlayer = ( grid[startRow][startCol] == 1) ? 2 : 1; // on regarde à qui a apartien le pharaon eleminer pour déterminer le gagnant
                    break;
                }
            }

            // Colorer la case parcourue par le laser
                grid[startRow][startCol] = player; // Coloration selon le joueur
            // si deux lasers se croisent. Le plus récent écrase le plus vieux. Le joueur peux retirer les laser depuis la view si ça le gène.


            // Avancer le laser
            startRow += dr;
            startCol += dc;
        }
    }

    private int reflectLaserPyramide(String piece, int direction) {
        String orientation = piece.split("-")[1]; // Exemple : "Pyramide-EN" -> "Nord"

        // Définir les réflexions en fonction de l'orientation
        switch (orientation) {
            case "EN":
                if (direction == 2) return 1; // Bas -> Droite
                if (direction == 3) return 0; // Gauche -> Haut
                break;
            case "SE":
                if (direction == 0) return 1; // Haut -> Droite
                if (direction == 3) return 2; // Gauche -> Bas
                break;
            case "OS":
                if (direction == 0) return 3; // Haut -> Gauche
                if (direction == 1) return 2; // Droite -> Bas
                break;
            case "NO":
                if (direction == 1) return 0; // Droite -> Haut
                if (direction == 2) return 3; // Bas -> Gauche
                break;
        }

        // Si aucune réflexion possible, le laser est absorbé
        return -1;
    }

    // Gestion de la réflexion pour Djed
    private int reflectLaserDjed(String piece, int direction) {

        // Pour Djed1 : miroir diagonal de bas-gauche à haut-droit
        if (piece.equals("Djed1")) {
            if (direction == 2) { // Laser venant du bas
                return 3; // Réfléchit à gauche
            } else if (direction == 1) { // Laser venant de la droite
                return 0; // Réfléchit vers le haut
            } else if (direction == 0) { // Laser venant du haut
                return 1; // Réfléchit à droite
            } else if (direction == 3 ) { // Laser venant de la gauche
                return 2; // Réfléchit vers le bas
            }
        }
        // Pour Djed2 : miroir diagonal de bas-droit à haut-gauche
        else if (piece.equals("Djed2")) {
            if (direction == 2) { // Laser venant du bas
                return 1; // Réfléchit à droite
            } else if (direction ==1) { // Laser venant de la droite
                return 2; // Réfléchit vers le bas
            } else if (direction == 0) { // Laser venant du haut
                return 3; // Réfléchit à gauche
            } else if (direction == 3) { // Laser venant de la gauche
                return 0; // Réfléchit vers le haut
            }
        }
        return -1;
    }

    // Gestion de la réflexion et duplication pour Horus
    private int reflectLaserHorus(String piece, int direction, int row, int col,int player) {
        // Réfléchir le laser à 90° et ajouter un laser secondaire à 180°
        if (piece.equals("Horus1")) {
            if (direction == 2) { // Laser venant du bas
                addLaser(row - 1, col, 2, player); // Laser secondaire continue
                return 3; // Réfléchit à gauche
            } else if (direction == 1) { // Laser venant de la gauche
                addLaser(row, col + 1, 1, player); // Laser secondaire continue
                return 0; // Réfléchit vers le haut
            } else if (direction == 0) { // Laser venant du haut
                addLaser(row + 1, col, 0, player); // Laser secondaire continue
                return 1; // Réfléchit à droite
            } else if (direction == 3 ) { // Laser venant de la droite
                addLaser(row, col - 1, 3, player); // Laser secondaire continue
                return 2; // Réfléchit vers le bas
            }
        }


        if (piece.equals("Horus2")) {
            if (direction == 2) { // Laser venant du bas
                addLaser(row - 1, col, 0, player); // Laser secondaire vers le haut
                return 1; // Réfléchit à droite
            } else if (direction ==1) { // Laser venant de la droite
                addLaser(row, col - 1, 3, player); // Laser secondaire vers la gauche
                return 2; // Réfléchit vers le bas
            } else if (direction == 0) { // Laser venant du haut
                addLaser(row + 1, col, 2, player); // Laser secondaire vers le bas
                return 3; // Réfléchit à gauche
            } else if (direction == 3) { // Laser venant de la gauche
                addLaser(row, col + 1, 1, player); // Laser secondaire vers la droite
                return 0; // Réfléchit vers le haut
            }
        }
        return -1;
    }

    // méthode qui enlève le laser du damier
    public void resetLaserGridForPlayer(int player) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == player && pieces[i][j] == null) { // Si la case est colorée par ce joueur et n'est pas une pièce
                    grid[i][j] = 0; // Réinitialiser à "vide"
                }
            }
        }
    }

    public boolean movePiece(int startRow, int startCol, int endRow, int endCol, int player) {
        // Vérifier si les indices sont dans les limites de la grille et si le déplacement est bien d'une case
        if (!isMoveValid(startRow, startCol, endRow, endCol)) {
            System.out.println("Déplacement invalide.");
            return false;
        }

        // on vérifie si la case est réservée
        String expectedColor = (player == 1) ? "Jaune" : "Rouge";
        if (!cellColors[endRow][endCol].equals(expectedColor) && !cellColors[endRow][endCol].equals("Neutre")) {
            System.out.println("Impossible de déplacer la pièce ici : cette case est réservée.");
            return false;
        }

        // Déplacement valide : mise à jour de la grille
        pieces[endRow][endCol] = pieces[startRow][startCol];
        pieces[startRow][startCol] = null;
        grid[startRow][startCol] = 0;
        grid[endRow][endCol] = player;
        System.out.println("Pièce déplacée de [" + startRow + ", " + startCol + "] à [" + endRow + ", " + endCol + "].");
        return true;
    }

    private boolean isMoveValid(int startRow, int startCol, int endRow, int endCol) {
        // Vérifier si les indices sont dans les limites de la grille
        if (startRow < 0 || startRow >= grid.length || startCol < 0 || startCol >= grid[0].length ||
                endRow < 0 || endRow >= grid.length || endCol < 0 || endCol >= grid[0].length) {
            return false;
        }

        // Vérifier si la case de départ contient une pièce
        if (pieces[startRow][startCol] == null) {
            return false;
        }

        // Vérifier si la case d'arrivée est vide
        if (pieces[endRow][endCol] != null) {
            return false;
        }

        // Vérifier si le déplacement est adjacent
        return Math.abs(startRow - endRow) <= 1 && Math.abs(startCol - endCol) <= 1;
    }

    public boolean rotatePiece(int row, int col, boolean clockwise) {
        // Vérifier si une pièce est présente
        String piece = pieces[row][col];
        if (piece == null) {
            System.out.println("Aucune pièce à faire pivoter à [" + row + ", " + col + "].");
            return false;
        }

        // Vérifier si la pièce est rotatable (Pyramide, Djed, Horus)
        if (piece.startsWith("Pharaon") || piece.startsWith("Obelisque")) {
            System.out.println("La pièce à [" + row + ", " + col + "] ne peut pas être pivotée.");
            return false;
        }

        if (piece.startsWith("Djed")){
           if (piece.startsWith("Djed1")){
               pieces[row][col]="Djed2";
            }else{
               pieces[row][col]="Djed1";
            }
           return true;
        }

        if (piece.startsWith("Horus")){
            if (piece.startsWith("Horus1")){
                pieces[row][col]="Horus2";
            }else{
                pieces[row][col]="Horus1";
            }
            return true;
        }

        // Récupérer les orientations possibles
        String[] orientations = {"EN", "SE", "OS", "NO"};
        int currentOrientationIndex = 0;

        // Extraire l'orientation actuelle
        for (int i = 0; i < orientations.length; i++) {
            if (piece.endsWith(orientations[i])) {
                currentOrientationIndex = i;
                break;
            }
        }

        // Calculer la nouvelle orientation
        int newOrientationIndex = clockwise
            ? (currentOrientationIndex + 1) % orientations.length
            : (currentOrientationIndex - 1 + orientations.length) % orientations.length;

        // Mettre à jour la pièce avec la nouvelle orientation
        String baseName = piece.split("-")[0]; // Exemple : "Pyramide-EN" -> "Pyramide"
        pieces[row][col] = baseName + "-" + orientations[newOrientationIndex];
        System.out.println("La pièce à [" + row + ", " + col + "] a été pivotée vers " + orientations[newOrientationIndex] + ".");
        return true;
    }

    // Méthode pour ajouter un second laser
    private void addLaser(int startRow, int startCol, int direction, int player) {
        if (isWithinBounds(startRow, startCol)) {
            shootLaser(startRow, startCol, direction, player, true);
        }
    }

    // Vérifier si les coordonnées sont dans les limites de la grille
    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int evaluateBoard(int player) {
        int score = 0;

        // Exemple simple : donner un score pour les pièces présentes
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                String piece = getPiece(i, j);
                if (piece != null) {
                    if (grid[i][j] == player) {
                        score += getPieceValue(piece); // Ajouter la valeur de la pièce au score
                    } else {
                        score -= getPieceValue(piece); // Soustraire la valeur des pièces adverses
                    }
                }
            }
        }

        // Ajouter des critères supplémentaires : contrôle de zones, exposition des Pharaons, etc.
        return score;
    }

    // getter && setter

    public String getPiece(int row, int col) {
        return pieces[row][col];
    }

    public int[][] getLaserGrid() {
        return grid;
    }

    public String[][] getPieceGrid() {
        return pieces;
    }

    public HashMap<String, ImageIcon> getPieceImages() {
        return pieceImages;
    }

    public int getRows() {
        return grid.length;
    }

    public int getCols() {
        return grid[0].length;
    }

    public String[][] getCellColors() {
        return cellColors;
    }

    public int[][] getGrid() {
        return grid;
    }

    private int getPieceValue(String piece) {
        return switch (piece) {
            case "Pharaon" -> 1000;
            case "Pyramide" -> 300;
            case "Obelisque_seul" -> 100;
            case "Obelisque_double" -> 200;
            case "Djed" -> 500;
            case "Horus" -> 400;
            default -> 0;
        };
    }

    public int getWinningPlayer() {
        return winningPlayer;
    }

}