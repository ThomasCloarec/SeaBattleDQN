import battle.BattleShip;

public class LaunchBattle {
    /**
     * Create game instances
     *
     * @param args arguments given by the user at jar execution
     */
    public static void main(String[] args) {
        new BattleShip("config1.txt", "Player 1", "Player 2");
    }
}
