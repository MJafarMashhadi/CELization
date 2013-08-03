/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.frontend.gameicons;

import celizationclient.frontend.MapContainer;
import celizationrequests.GameObjectID;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 *
 * @author mjafar
 */
public class BoatIcon extends GameIcon {
    protected ImageView boatIcon;

    public BoatIcon(GameObjectID id, String username) {
        boatIcon = new ImageView(GameIcon.Boat);
        this.getChildren().add(boatIcon);
//        super(MapContainer.Boat);
        boundID = id;
        boundUsername = username;
    }
}
