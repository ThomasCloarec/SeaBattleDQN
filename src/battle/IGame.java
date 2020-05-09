package battle;

/**
 * The interface of a normal game, could be implemented by any kind of game
 */
public interface IGame {
    /**
     * This method gives the description of the game
     *
     * @return the description of the game
     */
    String description();

    /**
     * This method starts the game
     */
    void start();

    /**
     * This method stops the game
     */
    void endOfGame();
}
