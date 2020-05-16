import battle.BattleShip;
import battle.text.AppText;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class LaunchBattle {
    /**
     * Create game instances
     *
     * @param args arguments given by the user at jar execution
     */
    public static void main(String[] args) {
        String path = null;
        String playerName1;
        String playerName2;

        if (args.length > 0) {
            path = args[0];
        } else {
            // Create a new file chooser
            JFileChooser jFileChooser = new JFileChooser();

            // set the default directory to the current one
            File workingDirectory = new File(System.getProperty("user.dir"));
            jFileChooser.setCurrentDirectory(workingDirectory);

            // Set a filter to only accept txt files
            jFileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    boolean ret = false;

                    int i = file.getAbsolutePath().lastIndexOf('.');
                    String extension = "";
                    if (i >= 0) {
                        extension = file.getAbsolutePath().substring(i + 1);
                    }

                    if (file.isDirectory() || "txt".equals(extension)) {
                        ret = true;
                    }
                    return ret;
                }

                @Override
                public String getDescription() {
                    return "Filtering text files";
                }
            });

            // Keep asking the user the file to use until he gives it or asks to cancel
            while (path == null) {
                int fileChooserValue = jFileChooser.showDialog(null, AppText.getTextFor("open_battleship_config"));
                if (fileChooserValue == JFileChooser.APPROVE_OPTION) {
                    path = jFileChooser.getSelectedFile().getAbsolutePath();
                } else if (fileChooserValue == JFileChooser.CANCEL_OPTION) {
                    System.err.println("No file chosen");
                    System.exit(1);
                }
            }
        }

        if (args.length == 3) {
            playerName1 = args[1];
            playerName2 = args[2];
        } else {
            playerName1 = AppText.getTextFor("player") + " 1";
            playerName2 = AppText.getTextFor("player") + " 2";
        }

        new BattleShip(path, playerName1, playerName2);
    }
}
