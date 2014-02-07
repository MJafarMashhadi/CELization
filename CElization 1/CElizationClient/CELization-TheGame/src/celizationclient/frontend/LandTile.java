package celizationclient.frontend;

import celization.mapgeneration.BlockType;
import celizationclient.frontend.gameicons.GameIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author mjafar
 */
public final class LandTile extends StackPane {

    public static final Image PLAIN = new Image(GameIcon.class.getResourceAsStream("PlainTile.png"));
    public static final Image WATER = new Image(GameIcon.class.getResourceAsStream("WaterTile.png"));
    public static final Image MOUNTAIN = new Image(GameIcon.class.getResourceAsStream("Mountain.png"));
    public static final Image JUNGLE = new Image(GameIcon.class.getResourceAsStream("GrassTile.png"));
    public static final Image BLACK = new Image(GameIcon.class.getResourceAsStream("BlackTile.png"));
    
    private BlockType type;
    private ImageView backImage;
    private int row;
    private int col;

    public LandTile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public LandTile(BlockType type, int row, int col) {
        setType(type);
        this.row = row;
        this.col = col;
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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public BlockType getType() {
        return type;
    }

    public ImageView getBackImage() {
        return backImage;
    }
    
    
}
