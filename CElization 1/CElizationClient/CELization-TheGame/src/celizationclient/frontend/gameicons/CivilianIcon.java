/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.frontend.gameicons;

import celizationclient.backend.net.CElizationClient;
import celization.civilians.Scholar;
import celizationclient.frontend.MapContainer;
import celizationrequests.GameObjectID;
import java.awt.Color;
import java.io.IOException;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontBuilder;
import javafx.scene.text.TextBuilder;

/**
 *
 * @author mjafar
 */
public class CivilianIcon extends GameIcon {

    CElizationClient client;

    public CivilianIcon(GameObjectID civilianID, CElizationClient client, String name, String username) {
        boundID = civilianID;
        boundUsername = username;
        this.client = client;

        int outfitNumber = 0;
        try {
            outfitNumber = client.getGameState().getCivilianByUID(civilianID).getOutfitNumber();
        } catch (IOException ex) {
        }
        if (civilianID.getNumber() == 0) {
            // derp
            this.getChildren().add(new ImageView(GameIcon.DERP));
        } else {
            this.getChildren().add(new ImageView(GameIcon.CIVILIAN[outfitNumber]));
        }

        this.getChildren().add(TextBuilder.create().text(name).layoutY(- GameIcon.DERP.getHeight() / 2).fill(javafx.scene.paint.Color.RED).build());
        this.setLayoutY(this.getLayoutY()- GameIcon.DERP.getHeight() / 2);
    }
}
