/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.frontend.gameicons;

import celizationrequests.GameObjectID;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 *
 * @author mjafar
 */
public class BuildingIcon extends GameIcon {
    protected ImageView buildingIcon;

    public BuildingIcon(Image icon, GameObjectID id, String username) {
        this.buildingIcon = new ImageView(icon);
//        super(icon);
        this.getChildren().add(buildingIcon);
        boundID = id;
        boundUsername = username;
    }
    
    public void setIcon(Image icon) {
        this.getChildren().remove(this.buildingIcon);
        this.buildingIcon = new ImageView(icon);
//        super(icon);
        this.getChildren().add(buildingIcon);
    }
}
