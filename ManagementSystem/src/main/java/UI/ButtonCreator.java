package UI;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import java.util.function.Consumer;

public class ButtonCreator {


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
