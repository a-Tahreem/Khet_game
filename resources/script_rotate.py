from PIL import Image
import os

# Dossier contenant les images originales (dossier courant)
input_folder = "."
# Dossier où les images tournées seront sauvegardées (dossier courant)
output_folder = "."

# Liste des pièces à traiter
pieces = ["Pyramide", "Djed", "Horus"]

# Rotation et sauvegarde des images
def rotate_and_save_images():
    for piece in pieces:
        input_file = os.path.join(input_folder, f"{piece}.png")
        if not os.path.exists(input_file):
            print(f"Image introuvable : {input_file}")
            continue

        # Ouvrir l'image
        image = Image.open(input_file)

        # Sauvegarder l'image dans sa position initiale (Nord)
        output_file_nord = os.path.join(output_folder, f"{piece}-Nord.png")
        image.save(output_file_nord)
        print(f"Sauvegardé : {output_file_nord}")

        # Effectuer les rotations
        orientations = {
            "Est": 90,
            "Sud": 180,
            "Ouest": 270,
        }
        for orientation, angle in orientations.items():
            rotated_image = image.rotate(angle, expand=True)
            output_file = os.path.join(output_folder, f"{piece}-{orientation}.png")
            rotated_image.save(output_file)
            print(f"Sauvegardé : {output_file}")

# Exécution
rotate_and_save_images()
