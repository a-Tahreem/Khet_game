import javax.swing.*;


public class DamierController {
    private final Damier model;
    private final DamierView view;
    private int currentPlayer;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean moveMode = false;
    private boolean decompileObelisque= false;
    private final boolean isSinglePlayer; // Nouveau champ pour savoir si l'IA joue

    public DamierController(Damier model, DamierView view, boolean isSinglePlayer) {
        this.model = model;
        this.view = view;
        this.currentPlayer = 1;
        this.isSinglePlayer = isSinglePlayer;
        initializePieces();
        initializeController();
        showPlayerTurn();

    }
    
    private void initializePieces() {
       // exemple de mise en place de pion via le code : model.setPiece(0, 0, "Pharaon");
       // model.setPiece(model.getRows() - 1, model.getCols() - 1, "Pharaon");
        
        view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());
    }

    private void initializeController() {
        JButton[][] buttons = view.getButtons();

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handlePlayerTurn(row, col));
            }
        }

        view.getLaserButton0().addActionListener(e -> {
            if (view.isShootlaserJaune() || view.isShootlaserRouge()){
                model.resetLaserGridForPlayer(1);
                model.resetLaserGridForPlayer(2);
                view.setShootlaserJaune(false);
                view.setShootlaserRouge(false);
                view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());
            }
        });

        view.getLaserButton1().addActionListener(e -> {
            model.shootLaser(model.getRows()-1 ,model.getCols()-1, 0, 1, false);
            view.setShootlaserJaune(true);
            view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());


            if (model.isGameOver()) {
                view.showGameOverMessage(model.getWinningPlayer());
            }

        });

        view.getLaserButton2().addActionListener(e -> {
            model.shootLaser(0, 0, 2, 2, false);
            view.setShootlaserRouge(true);
            view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());


            if (model.isGameOver()) {
                view.showGameOverMessage(model.getWinningPlayer());
            }
        });
    }

            /*
            // Si la case est vide, proposer de placer une pièce. Pour un mode de jeu avec placement des pièce custom.
            // code a mettre dans le handleButtonClick
            String[] options = {"Pharaon",
                     "Pyramide-SE", "Pyramide-EN", "Pyramide-NO", "Pyramide-OS",
                     "Djed1", "Djed2",
                     "Horus",
                    "Obelisque_seul", "Obelisque_double"};
            String choice = (String) JOptionPane.showInputDialog(
                    null, "Choisissez un pion :", "Sélection",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]
            );
    
            if (choice != null) {
                model.setPiece(row, col, choice);
                Color borderColor = (currentPlayer == 1) ? Color.YELLOW : Color.RED; // Couleur selon le joueur
                view.setButtonBorder(row, col, borderColor); // Appliquer la bordure
                view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());
            }
             */
            private void handleButtonClick(int row, int col) {
                int [][] grid = model.getLaserGrid();
                if (!moveMode) {
                    // Étape 1 : Sélection d'une pièce et action à réaliser
                    String piece = model.getPiece(row, col);

                    if (piece == null) {
                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une pièce valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (grid [row][col] != currentPlayer) {
                        JOptionPane.showMessageDialog(null, "Ce n'est pas votre pièce. Jouez avec vos propres pièces.", "Mauvaise pièce", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Vérifier si la pièce est un Obelisque_double
                    if (piece.equals("Obelisque_double")) {
                        // Proposer l'option de décompiler
                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Voulez-vous décomposer cet Obélisque double en deux Obélisques simples ?",
                                "Décomposer l'Obélisque double",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            // Décompiler l'Obélisque double
                            model.setPiece(row, col, "Obelisque_double_tuer", currentPlayer);
                            model.setPiece(row, col, "Obelisque_seul", currentPlayer); // Remplacer par un Obélisque simple
                            JOptionPane.showMessageDialog(null, "Placez le deuxième Obélisque simple sur une case adjacente.", "Placement", JOptionPane.INFORMATION_MESSAGE);

                            // Activer le mode déplacement pour le second obélisque
                            selectedRow = row;
                            selectedCol = col;
                            moveMode = true; // Activer le mode déplacement pour placer le second obélisque
                            decompileObelisque = true; // option pour remetre l'obelique d'origine aorès le déplacement
                            return; // Sortir pour attendre le clic de destination
                        }
                    }

                    // Afficher la boîte de dialogue pour choisir une action
                    String[] actions = {"Déplacer", "Rotation horaire", "Rotation antihoraire", "Annuler"};
                    int action = JOptionPane.showOptionDialog(
                            null,
                            "Que voulez-vous faire avec la pièce en (" + row + ", " + col + ") ?",
                            "Actions sur la pièce",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            actions,
                            actions[0]
                    );

                    switch (action) {
                        case 0: // Déplacer
                            selectedRow = row;
                            selectedCol = col;
                            moveMode = true; // Activer le mode déplacement
                            JOptionPane.showMessageDialog(null, "Cliquez sur la case de destination.", "Déplacement", JOptionPane.INFORMATION_MESSAGE);
                            break;

                        case 1: // Rotation horaire
                            if (model.rotatePiece(row, col, true)) {
                                view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());
                                // met à jour les laser.
                                if (view.isShootlaserRouge()){
                                    model.shootLaser(0, 0, 2, 2, false);
                                }
                                if(view.isShootlaserJaune()){
                                    model.shootLaser(model.getRows()-1 ,model.getCols()-1, 0, 1, false);
                                }
                                view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors()); // second update pour rafraichir l'affichage avec le nouveau laser dû déplacement de pièce

                                // Changer de joueur
                                currentPlayer = (currentPlayer == 1) ? 2 : 1;
                                view.updateCurrentPlayerLabel(currentPlayer);
                                showPlayerTurn();
                            } else {
                                JOptionPane.showMessageDialog(null, "Cette pièce ne peut pas être pivotée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                            break;

                        case 2: // Rotation antihoraire
                            if (model.rotatePiece(row, col, false)) {
                                view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());
                                // Changer de joueur
                                currentPlayer = (currentPlayer == 1) ? 2 : 1;
                                view.updateCurrentPlayerLabel(currentPlayer);

                                // met à jour les laser.
                                if (view.isShootlaserRouge()){
                                    model.shootLaser(0, 0, 2, 2,false);
                                }
                                if(view.isShootlaserJaune()){
                                    model.shootLaser(model.getRows()-1 ,model.getCols()-1, 0, 1,false);
                                }
                                view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors()); // second update pour rafraichir l'affichage avec le nouveau laser dû déplacement de pièce

                                showPlayerTurn();
                            } else {
                                JOptionPane.showMessageDialog(null, "Cette pièce ne peut pas être pivotée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                            break;

                        case 3: // Annuler
                        default:
                            // Ne rien faire
                            break;
                    }
                } else {
                    // Étape 2 : Sélection de la destination pour le déplacement
                    if (model.getPiece(row, col) != null) {

                        String startPiece = model.getPiece(selectedRow, selectedCol);
                        if (model.getPiece(row, col).startsWith("Obelisque_seul") && startPiece != null && startPiece.startsWith("Obelisque_seul")  && !decompileObelisque){
                            // retirer les deux obelisque et mettre l'obelisque double
                            model.setPiece(row, col, "obelisque_cible",currentPlayer);
                            model.setPiece(selectedRow, selectedCol, "obelisque_deplacer",currentPlayer);
                            model.setPiece(row, col, "Obelisque_double",currentPlayer);


                            view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());
                            JOptionPane.showMessageDialog(null, "Obélisque double formé !", "Succès", JOptionPane.INFORMATION_MESSAGE);

                            if (view.isShootlaserRouge()){
                                model.shootLaser(0, 0, 2, 2, false);
                            }
                            if(view.isShootlaserJaune()){
                                model.shootLaser(model.getRows()-1 ,model.getCols()-1, 0, 1,false);
                            }
                            view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors()); // second update pour rafraichir l'affichage avec le nouveau laser dû déplacement de pièce

                            // Changer de joueur
                            currentPlayer = (currentPlayer == 1) ? 2 : 1;
                            view.updateCurrentPlayerLabel(currentPlayer);
                            showPlayerTurn();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "La case de destination est déjà occupée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (selectedRow != -1 && selectedCol != -1) {
                        if (model.movePiece(selectedRow, selectedCol, row, col,currentPlayer)) {

                            if (decompileObelisque) {
                                view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());
                                model.setPiece(selectedRow,selectedCol,"Obelisque_seul",currentPlayer);
                                decompileObelisque =false;
                            }
                            view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors()); // 1er update pour déplacer la pièce
                            // met à jour les laser.
                            if (view.isShootlaserRouge()){
                                model.shootLaser(0, 0, 2, 2, false);
                            }
                            if(view.isShootlaserJaune()){
                                model.shootLaser(model.getRows()-1 ,model.getCols()-1, 0, 1,false);
                            }
                            view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors()); // second update pour rafraichir l'affichage avec le nouveau laser dû déplacement de pièce

                            // Changer de joueur
                            currentPlayer = (currentPlayer == 1) ? 2 : 1;
                            view.updateCurrentPlayerLabel(currentPlayer);
                            showPlayerTurn();
                        } else {
                            JOptionPane.showMessageDialog(null, "Déplacement invalide. Réessayez.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    // Réinitialiser l'état après le déplacement
                    selectedRow = -1;
                    selectedCol = -1;
                    moveMode = false;

                    if (model.isGameOver()) {
                        view.showGameOverMessage(model.getWinningPlayer());
                    }

                }
            }

    private void showPlayerTurn() {
        String playerColor = (currentPlayer == 1) ? "Jaune" : "Rouge";
        JOptionPane.showMessageDialog(null, "C'est au joueur " + playerColor + " de jouer !", "Tour du joueur", JOptionPane.INFORMATION_MESSAGE);
    }

    // méthode pour jouer contre une IA

    private void handlePlayerTurn(int row, int col) {
        // Code pour gérer le tour du joueur
        handleButtonClick(row, col);

        // Si c'est un jeu contre l'IA et que le joueur Jaune a joué, l'IA joue ensuite
        if (isSinglePlayer && currentPlayer == 2) {
            handleComputerTurn();
        }
    }

    private void handleComputerTurn() {
        JOptionPane.showMessageDialog(
                null,
                "L'ordinateur (Rouge) réfléchit...",
                "Tour de l'ordinateur",
                JOptionPane.INFORMATION_MESSAGE
        );

        int[] bestMove = findBestMove(model, 2, 3); // 2 = Joueur ordinateur (Rouge), profondeur 3
        model.movePiece(bestMove[0], bestMove[1], bestMove[2], bestMove[3], 2);
        view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors());

        if (view.isShootlaserRouge()){
            model.shootLaser(0, 0, 2, 2, false);
        }
        if(view.isShootlaserJaune()){
            model.shootLaser(model.getRows()-1 ,model.getCols()-1, 0, 1, false);
        }
        view.updateGrid(model.getLaserGrid(), model.getPieceGrid(), model.getPieceImages(), model.getCellColors()); // second update pour rafraichir l'affichage avec le nouveau laser dû déplacement de pièce


        // Vérifier si la partie est terminée
        if (model.isGameOver()) {
            view.showGameOverMessage(model.getWinningPlayer());
        } else {
            currentPlayer = 1; // Repasser au joueur humain
            view.updateCurrentPlayerLabel(currentPlayer);
            showPlayerTurn();
        }
    }

    public int[] findBestMove(Damier board, int player, int depth) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[4]; // startRow, startCol, endRow, endCol

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (board.getPiece(row, col) != null && board.getGrid()[row][col] == player) {
                    for (int newRow = row - 1; newRow <= row + 1; newRow++) {
                        for (int newCol = col - 1; newCol <= col + 1; newCol++) {
                            if (board.movePiece(row, col, newRow, newCol, player)) {
                                int score = minimax(board, depth - 1, false, player == 1 ? 2 : 1);
                                if (score > bestScore) {
                                    bestScore = score;
                                    bestMove = new int[]{row, col, newRow, newCol};
                                }
                                board.movePiece(newRow, newCol, row, col, player); // Annuler le déplacement
                            }
                        }
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(Damier board, int depth, boolean isMaximizing, int player) {
        if (board.isGameOver() || depth == 0) {
            return board.evaluateBoard(player);
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                // Vérifier si le déplacement est valide
                if (board.getPiece(row, col) != null && board.getGrid()[row][col] == player) {
                    for (int newRow = row - 1; newRow <= row + 1; newRow++) {
                        for (int newCol = col - 1; newCol <= col + 1; newCol++) {
                            if (board.movePiece(row, col, newRow, newCol, player)) {
                                // Appeler récursivement Minimax
                                int score = minimax(board, depth - 1, !isMaximizing, player == 1 ? 2 : 1);
                                bestScore = isMaximizing ? Math.max(bestScore, score) : Math.min(bestScore, score);

                                // Annuler le déplacement
                                board.movePiece(newRow, newCol, row, col, player);
                            }
                        }
                    }
                }
            }
        }
        return bestScore;
    }

}