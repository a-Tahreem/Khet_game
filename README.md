
---
title: "Khet - Implémentation en Java"
output: github_document
---

# Khet - Implémentation en Java

![Khet Banner](https://via.placeholder.com/800x200?text=Khet+Java+Game)  
*Une version interactive du jeu Khet, intégrant réflexion stratégique et lasers !*

## Description
Ce projet propose une implémentation complète du jeu de société **Khet** en Java. En utilisant le modèle **MVC** (Modèle-Vue-Contrôleur), il permet aux joueurs de s'affronter en alternant stratégie et précision, avec la possibilité de jouer en mode **1 joueur (contre l'IA)** ou **2 joueurs**.

## Fonctionnalités
- **Damier interactif** : Affichage graphique des pièces sur une grille de 8x10.
- **Pièces uniques et mécaniques réalistes** :
  - **Pharaon** : Protège-le à tout prix pour éviter de perdre.
  - **Pyramides** : Réfléchissent les lasers selon leur orientation.
  - **Obélisques** : Absorbent ou se décomposent en deux pièces.
  - **Djed et Horus** : Modifient les trajectoires des lasers de façon unique.
- **Gestion des lasers** :
  - Tir dynamique de lasers pour réfléchir, absorber ou détruire les pièces.
  - Gestion des collisions et interactions physiques réalistes.
- **Modes de jeu** :
  - **1 joueur** : Défiez une IA stratégique basée sur Minimax.
  - **2 joueurs** : Affrontez un ami en local.
- **Interface utilisateur intuitive** :
  - Couleurs dynamiques pour indiquer les zones traversées par les lasers.
  - Boutons pour chaque case et actions spécifiques.
- **Fin de partie automatique** : La partie se termine dès que le Pharaon d’un joueur est touché.

## Captures d'écran
Ajoutez ici des captures d'écran pour montrer :
- La grille initiale avec les pièces.
- Un tir de laser en action.
- L'écran de fin de partie.

## Comment jouer
1. **Lancer le jeu** :
   - Exécutez le fichier `Main.java`.
2. **Choisir le mode de jeu** :
   - *1 joueur* : Vous contre une IA.
   - *2 joueurs* : Affrontez un ami en local.
3. **Objectif** : Protégez votre **Pharaon** tout en essayant de toucher celui de votre adversaire.
4. **Actions possibles** :
   - Déplacer une pièce (une case adjacente).
   - Tourner une pièce (90° dans le sens horaire ou antihoraire).
   - Tirer un laser à la fin de votre tour.
5. **Gagner la partie** : Touchez le **Pharaon** adverse avec un laser.

## Installation
1. Clonez ce dépôt :
   ```bash
   git clone https://github.com/username/khet-java.git
   ```
2. Importez le projet dans votre IDE préféré (Eclipse, IntelliJ IDEA, etc.).
3. Assurez-vous que le répertoire `resources` contient les images des pièces.
4. Exécutez le fichier `Main.java`.

## Technologies utilisées
- **Java Swing** : Pour l'interface graphique.
- **Algorithme Minimax** : Implémenté pour l'IA.
- **Modèle MVC** : Structure du projet pour une séparation claire des responsabilités.
- **HashMap** : Pour la gestion des images des pièces.

## Structure du projet
- **`Main.java`** : Point d'entrée du jeu, choix du mode de jeu.
- **`Damier.java`** :
  - Modèle de données représentant la grille, les pièces, et les lasers.
  - Gestion des interactions entre pièces et lasers.
- **`DamierView.java`** :
  - Vue graphique du jeu (Swing).
  - Mise à jour des cases, des lasers, et des messages de fin de partie.
- **`DamierController.java`** :
  - Contrôleur gérant les actions des joueurs, le changement de tours, et les interactions IA.

## Améliorations possibles
- Ajout d'un mode multijoueur en ligne.
- Personnalisation des configurations initiales de la grille.
- Amélioration de l'IA pour des stratégies plus complexes.
- Ajout de skins ou thèmes pour les pièces et la grille.

## Contributions
Les contributions sont les bienvenues ! Si vous souhaitez améliorer ce projet, veuillez :
1. Forker le dépôt.
2. Créer une branche pour vos modifications :
   ```bash
   git checkout -b feature/amélioration
   ```
3. Soumettre une **pull request**.

