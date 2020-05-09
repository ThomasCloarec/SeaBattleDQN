import battle.BattleShip;

public class LaunchBattle {
    /**
     * Create game instances
     *
     * @param args arguments given by the user at jar execution
     */
    public static void main(String[] args) {
        String path;
        String playerName1;
        String playerName2;

        if (args.length > 0) {
            path = args[0];
        } else {
            path = "../data/config1.txt";
        }

        if (args.length == 3) {
            playerName1 = args[1];
            playerName2 = args[2];
        } else {
            playerName1 = "Player 1";
            playerName2 = "Player 2";
        }

        new BattleShip(path, playerName1, playerName2);
    }
}
