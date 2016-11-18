# Développement et intégration d'un éditeur WYSIWYG 

Contexte : OpenFlexo est une plateforme de mise en relation de divers documents et sources de données, qui comprend notamment un module d'édition WYSIWYG prenant en charge les fichiers Microsoft Word grâce à la bibliothèque java docx4all.

L'objectif principal de ce projet est de développer un éditeur WYSIWYG universel, qui prendrait en charge non seulement les fichiers Microsoft Word, mais aussi la plupart des formats équivalents courants, et en particulier OpenDocument.
Pour ce faire, il faudra analyser la structure d'un document, et déterminer quelles sont les fonctionnalités prioritaires pour le manipuler tout en conservant le lien entre la vue et le contenu.

Dans un second temps, est envisagée l'intégration de ce module au sein du logiciel OpenFlexo.

Ce projet fera l'objet d'un rapport technique dont le format reste encore à déterminer.

TODO :
 - écrire une petite interface permettant d'écrire du texte et de générer / charger un docx
 **écrire du texte partiellement fait, générer un docx fait**
 - essayer avec du formatage basique
 **done**
 - étudier la partie chargement / sauvegarde
 **en cours**
 - faire attention à ne pas créer de dépendances inutiles, etc (penser à l'intégration)
 **Pour le moment, uniquement Swing et docx4j**
 - chercher un équivalent de docx4all pour odt et se renseigner sur la structure du document
 **en cours**
 - vérifier la faisabilité du document générique
