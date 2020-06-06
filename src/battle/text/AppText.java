package battle.text;

import java.util.ResourceBundle;

/**
 * Utility class used for internationalization
 */
public final class AppText {
    /**
     * The constant text.
     */
    private static final ResourceBundle text = ResourceBundle.getBundle("battle/text/TextLabels");

    /**
     * Private constructor to not allow instantiation
     */
    private AppText() {

    }

    /**
     * Check if the app has a text for a particular key
     *
     * @param key the key to check
     * @return if the key exists in the resource bundle
     */
    public static boolean hasTextFor(String key) {
        return AppText.text.containsKey(key);
    }

    /**
     * Get the text for a particular key
     *
     * @param key the key of the text
     * @return the text in the right language
     */
    public static String getTextFor(String key) {
        return AppText.text.getString(key);
    }
}
