package UI;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import java.util.function.Consumer;

/**
 * This class provides a utility method to create clickable buttons with custom styling.
 * <p>
 * The buttons created by this class are interactive and respond to mouse clicks.
 * </p>
 */
public class ButtonCreator {

    /**
     * Creates a clickable button with custom styling.
     *
     * @param text           The text to display on the button.
     * @param fontSize       The font size of the button text.
     * @param textColorHex   The color of the button text in hexadecimal format.
     * @param onClick        The action to perform when the button is clicked.
     * @return The created button.
     */

    public static Button createButton(String text, int fontSize, String textColorHex, Consumer<Button> onClick) {
        Button button = new Button(text);
        button.setFont(Font.font(fontSize));
        button.setStyle(String.format("-fx-background-color: #333333; " +
                "-fx-text-fill: %s; " +
                "-fx-font-size: %dpx; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 10;", textColorHex, fontSize));

        // Set up highlight effect on mouse enter and exit
        button.setOnMouseEntered(e -> button.setStyle(String.format("-fx-background-color: #d3d3d3; " +
                "-fx-text-fill: black; " +
                "-fx-font-size: %dpx; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 10;", fontSize)));
        button.setOnMouseExited(e -> button.setStyle(String.format("-fx-background-color: #333333; " +
                "-fx-text-fill: %s; " +
                "-fx-font-size: %dpx; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 10;", textColorHex, fontSize)));

        // Handle button clicks
        button.setOnAction(e -> onClick.accept(button));

        return button;
    }
}
