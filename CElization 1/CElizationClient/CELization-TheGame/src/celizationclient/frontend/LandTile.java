/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.frontend;

import celization.mapgeneration.BlockType;
import celizationclient.frontend.gameicons.GameIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextBuilder;

/**
 *
 * @author mjafar
 */
public class LandTile extends StackPane {

    public static final Image PLAIN = new Image(GameIcon.class.getResourceAsStream("PlainTile.png"));
    public static final Image WATER = new Image(GameIcon.class.getResourceAsStream("WaterTile.png"));
    public static final Image MOUNTAIN = new Image(GameIcon.class.getResourceAsStream("Mountain.png"));
    public static final Image JUNGLE = new Image(GameIcon.class.getResourceAsStream("GrassTile.png"));
    public static final Image BLACK = new Image(GameIcon.class.getResourceAsStream("BlackTile.png"));
    BlockType type;
    ImageView backImage;

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;

        switch (type) {
            case JUNGLE:
                backImage = new ImageView(LandTile.JUNGLE);
                break;
            case MOUNTAIN:
            case PLAIN:
                backImage = new ImageView(LandTile.PLAIN);
                break;
            case WATER:
                backImage = new ImageView(LandTile.WATER);
                break;
            default:
            case UNKNOWN:
                backImage = new ImageView(LandTile.BLACK);
        }
        this.getChildren().clear();
        this.getChildren().add(backImage);
        if (type == BlockType.MOUNTAIN) {
            this.getChildren().add(new ImageView(LandTile.MOUNTAIN));
        }
    }
}
