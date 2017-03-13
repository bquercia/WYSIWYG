# Développement et intégration d'un éditeur WYSIWYG 

Contexte : OpenFlexo est une plateforme de mise en relation de divers documents et sources de données, qui comprend notamment un module d'édition WYSIWYG prenant en charge les fichiers Microsoft Word grâce à la bibliothèque java docx4all.

L'objectif principal de ce projet est de développer un éditeur WYSIWYG universel, qui prendrait en charge non seulement les fichiers Microsoft Word, mais aussi la plupart des formats équivalents courants, et en particulier OpenDocument.
Pour ce faire, il faudra analyser la structure d'un document, et déterminer quelles sont les fonctionnalités prioritaires pour le manipuler tout en conservant le lien entre la vue et le contenu.

Dans un second temps, est envisagée l'intégration de ce module au sein du logiciel OpenFlexo.

Les descriptions ci-dessous tiennent lieu de rapport technique.

# Entités en jeu

Le code s’articule en trois parties : les entités constitutives d’un document génériques, réparties dans les packages content et style ; les parties d’interface utilisateurs, dans le package ui, et un intermédiaire appelé ici Translator, qui fait le lien entre l’interface et le document générique via des règles de traduction. Ces trois entités constituent une architecture MVC. À cela s’ajoute un main de test, dans le package test.

# Documentation
La Javadoc a été générée et est disponible dans ce repository, rédigée en anglais.

# Difficultés

Plusieurs points sensibles ont nécessité et / ou nécessiteront des ajustements :
•	Fonctionnalités d’un tableau : pour permettre la souplesse d’utilisation sans autoriser de situation indésirable (cellule qui augmente son colspan sans adapter le reste du tableau par exemple), de nombreuses fonctions auxiliaires ont été utilisées.
•	Propriétés de style : ce problème n’a pas été résolu. Il apparaît difficile de traduire toutes les propriétés stylistiques, car JEditorPane ne permet pas tout. Il n’est par exemple pas possible de créer un tableau dont les bordures des cellules voisines sont partagées (border-collapse : collapse). À moins de faire du dessin à bas niveau, il faut s’accommoder de solutions proches (border-spacing : 0px).
•	Création d’une règle à partir d’une propriété : dans la structure d’une propriété a été prévue l’existence de valeurs possibles (par ex pour font-weight : bold, normal), et ce afin de mieux les prendre en charge, et d’éviter des valeurs incongrues.  Ainsi, pour la valeur bold, on créerait un run en gras, alors que pour normal on essaierait au contraire de supprimer des runs… Toutefois, d’autres propriétés pouvant prendre des valeurs arbitraires, on ne peut pas  restreindre la création de règles à une énumération de comportements en fonction des valeurs. Finalement, le code fourni ici ne fait pas grand usage des valeurs possibles.
•	Un problème de noms s'est également fait ressentir. En effet, le composant JEditorPane possède son propre modèle de document, qui ressemble par son vocabulaire à celui développé ici. C'est pourquoi EditorPanel dispose de deux fonctions : getDocument(), héritée de JEditorPane, et getDocumentModel(), pour récupérer le modèle de document de ce projet.

# Travail ultérieur

La suite de ce projet aura lieu directement sur le repository d’OpenFlexo, et non sur ce repository, afin de faciliter l’intégration.

# Fonctionnement

Pour s'approprier ce projet, on peut utiliser le main de test fourni. Les étapes principales sont :
•	Création d'un Translator
•	Création d'un EditorPanel
•	Ajout d'éléments dans le document de l'EditorPanel. Pour ce faire, on peut par exemple utiliser EditorPanel.getDocumentModel().addParagraph("texte").

