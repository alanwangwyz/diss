package Client;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameGUI {



    public void GameGUI(){
        Stage gamestage = new Stage();
        gamestage.setTitle("Scrabble Game");
        Scene gamescene;
        GridPane GP = createGameGridPane();
        Label headerLabel = new Label("Welcome!");
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(0, 0,20,0));
        GP.add(headerLabel,0,0,2,1);
        gamescene=new Scene(GP,1500,1000);
        gamestage.setScene(gamescene);
        gamestage.show();

    }

    private GridPane createGameGridPane ()
    {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }
}
