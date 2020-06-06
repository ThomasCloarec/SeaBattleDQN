###Exécution du projet :
    Pour lancer l'exécution du projet, il suffit de taper la commande "java -jar battle_thomasCloarec.jar"
    à l'emplacement de l'archive jar (qui est normalement le même que celui de ce README.txt).

###Placement des répertoires de données :
    Quand vous aurez exécuté la commande indiqué ci-dessous, le logiciel vous demandera la localisation des fichiers de configuration.
    Ces fichiers se trouvent dans le répertoire data (lui-même divisé en trois répertoires (ai_ai, humain_ai et human_human) selon le mode voulu.
    Le premier fichier demandé par le système est le fichier de jeu, c'est celui finissant par "game.txt".
    Ensuite selon le mode vous avez un certain nombre de fichiers joueurs à sélectionner (en ai_ai : 0, human_ai : 1, human_human : 2).
    Les fichiers de configuration des joueurs humains sont nommés tels "human1.txt", "human2.txt", etc.

###Explications du mode IA contre IA :
    Ce mode est principalement utilisé afin d'entraîner un réseau de neurones (un approximateur de fonction) utilisant l'apprentissage par renforcement (essai/erreur -> récompense) pour le mode humain contre IA.
    L'entraînement se fait grâce à un joueur pré-programmé dont le but est de jouer de façon relativement bête,
    c'est à dire que le seul élément auquel il fait attention est de ne pas retaper deux fois au même endroit.
    En revanche, j'avais pour but de proposer un joueur de niveau humain, c'est ainsi que j'en suis venu à construire un réseau de neurones.
    Pour donner une rapide explication du but de celui-ci, en lui donnant les informations de l'état du jeu (isHit et isFree pour chaque case du plateau),
    il doit prédire la récompense qu'il obtiendra à ce tour pour chaque action possible. Cette récompense vaut 1 si l'algorithme touche et -1 s'il rate son tir.
    Ainsi, cette prédiction de la récompense devant être la plus précise possible, j'utilise une fonction "coût" (simplement la différence entre la récompense prédite et celle reçue)
    que je dois minimiser. En fait, cette utilisation d'une récompense est le point de départ du domaine de l'apprentissage par renforcement.
    Ce réseau utilise l'algorithme de descente de gradient en guise d'optimisateur afin de diminuer au maximum la fonction coût.
    Grâce à cette récompense prédite, l'algorithme va tirer en priorité vers la case qui a la plus grande récompense (selon sa prédiction) afin de
    maximiser le nombre de coup "touché" ce qui aura pour effet global d'augmenter son taux de victoire.
    Le réseau de neurones est initialisé de façon aléatoire avec des valeurs proches de 0.
    Cela signifie qu'on ne connait pas encore la récompense des cases, donc je la rend aléatoire (sans doute pas la meilleure manière de faire, mais cela fonctionne plutôt bien).
    Dû à une puissance de calcul limité, à un algorithme pas forcément optimisé au mieux
    ainsi qu'au fait que je ne voulais pas prendre plusieurs heures à entraîner mon modèle (voir des jours),
    j'ai préféré limiter la taille du plateau à 4 lignes et 4 colonnes. On peut y remarquer des éléments de compréhension du jeu du fait que l'algorithme ne retape
    pas plusieurs fois au même endroit, que quand il a touché une case alors il va tenter de voir ce qu'il y a autour ainsi que du fait que quand il pense avoir coulé
    un bateau, il évite de taper les cases autour (en ayant appris que cela n'arrive pas).
    Voici ci-dessous un exemple d'entraînement de l'IA "réseau de neurones" contre l'IA pré-programmée.
    0 minute d'entrainement et 0 partie jouée : 0.0% of games won by the neural network over the programmed IA
    1 minute d'entrainement et 50 000 parties jouées : 1.36% of games won by the neural network over the programmed IA
    2 minutes d'entrainement et 100 000 parties jouées : 4.7% of games won by the neural network over the programmed IA
    3 minutes d'entrainement et 150 000 parties jouées : 18.76% of games won by the neural network over the programmed IA
    4 minutes d'entrainement et 200 000 parties jouées : 49.96% of games won by the neural network over the programmed IA
    5 minutes d'entrainement et 250 000 parties jouées : 76.72% of games won by the neural network over the programmed IA
    6 minutes d'entrainement et 300 000 parties jouées : 94.66% of games won by the neural network over the programmed IA
    ~ 6 minutes d'entrainement et 310 000 parties jouées : 95.82% of games won by the neural network over the programmed IA
    A NOTER : Le temps d'entrainement indiqué ci-dessus prend en compte l'affichage d'une partie / 5000 (cela constitue la majorité du temps indiqué).

###Explications du mode Humain VS IA :
    Ce mode utilise simplement un fichier sérializé ("trained_nn.ser" embarqué dans le jar) contenant un réseau de neurones pré-entrainé.

###Limites du projet :
    Il aurait été intéressant de voir ce que le réseau de neurones aurait pu apprendre
    en utilisant une plus grande puissance de calcul (bien que cette "démo" en 4x4
    démontre tout de même son bon fonctionnement). Mon implémentation est proche d'un DQN (Deep Q Network) 
    qui est une technique assez "ancienne" d'apprentissage par renforcement profond (utilisant des réseaux de neurones).
    Elle a le mérite d'être assez "simple" mais elle date maintenant de quelques années.
    
##Autres :
    J'ai également implémenté une prise en charge multilingue du jeu grâce aux RessourceBundle et aux fichiers ".properties" (voir package battle.text)
