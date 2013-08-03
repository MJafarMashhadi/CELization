/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.frontend.gameicons;

import celizationclient.frontend.LandTile;
import celizationclient.frontend.MapContainer;
import celizationrequests.GameObjectID;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

/**
 *
 * @author mjafar
 */
public abstract class GameIcon extends Group {

    public static final Image INFANTRY_SPEAR = new Image(GameIcon.class.getResourceAsStream("civilians/Infantry_Spear.png"));
    public static final Image INFANTRY_SAPPER = new Image(GameIcon.class.getResourceAsStream("civilians/Infantry_Sapper.png"));
    public static final Image HORSEMAN_MACE = new Image(GameIcon.class.getResourceAsStream("civilians/Horseman_Mace.png"));
    public static final Image[] CIVILIAN;
    public static final Image DERP = new Image(GameIcon.class.getResourceAsStream("civilians/Derp.png"));
    //
    public static final Image[] INCOMPLETE_BUILDINGS;
    public static final Image GOLD_MINE = new Image(GameIcon.class.getResourceAsStream("GoldMine.png"));
    public static final Image HQ = new Image(GameIcon.class.getResourceAsStream("HQ.png"));
    public static final Image STONE_MINE = new Image(GameIcon.class.getResourceAsStream("StoneMine.png"));
    public static final Image UNIVERSITY = new Image(GameIcon.class.getResourceAsStream("University.png"));
    public static final Image Horseman_Jockey = new Image(GameIcon.class.getResourceAsStream("civilians/Horseman_Jockey.png"));
    public static final Image STORAGE = new Image(GameIcon.class.getResourceAsStream("storage.png"));
    public static final Image FARM = new Image(GameIcon.class.getResourceAsStream("Field.png"));
    public static final Image WOOD_CAMP = new Image(GameIcon.class.getResourceAsStream("WoodCamp.png"));
    public static final Image Boat = new Image(GameIcon.class.getResourceAsStream("Boat.png"));
    public static final Image MARKET = new Image(GameIcon.class.getResourceAsStream("Market.png"));
    public static final Image PORT = new Image(GameIcon.class.getResourceAsStream("Port.png"));
    public static final Image BARRACKS = new Image(GameIcon.class.getResourceAsStream("Barracks.png"));
    public static final Image STABLE = new Image(GameIcon.class.getResourceAsStream("Stable.png"));
    //
    public static final double BLOCK_WIDTH;
    public static final double BLOCK_HEIGHT;

    static {
        CIVILIAN = new Image[16];
        CIVILIAN[0] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_01.png"));
        CIVILIAN[1] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_02.png"));
        CIVILIAN[2] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_03.png"));
        CIVILIAN[3] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_04.png"));
        CIVILIAN[4] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_05.png"));
        CIVILIAN[5] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_06.png"));
        CIVILIAN[6] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_07.png"));
        CIVILIAN[7] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_08.png"));
        CIVILIAN[8] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_09.png"));
        CIVILIAN[9] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_10.png"));
        CIVILIAN[10] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_11.png"));
        CIVILIAN[11] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_12.png"));
        CIVILIAN[12] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_13.png"));
        CIVILIAN[13] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_14.png"));
        CIVILIAN[14] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_15.png"));
        CIVILIAN[15] = new Image(GameIcon.class.getResourceAsStream("civilians/Civilian_16.png"));

        INCOMPLETE_BUILDINGS = new Image[4];
        INCOMPLETE_BUILDINGS[1] = new Image(GameIcon.class.getResourceAsStream("notCompleted1.png"));
        INCOMPLETE_BUILDINGS[1] = new Image(GameIcon.class.getResourceAsStream("notCompleted2.png"));
        INCOMPLETE_BUILDINGS[1] = new Image(GameIcon.class.getResourceAsStream("notCompleted3.png"));

        BLOCK_WIDTH = LandTile.PLAIN.getWidth();
        BLOCK_HEIGHT = LandTile.PLAIN.getHeight();
    }
    protected GameObjectID boundID;
    protected String boundUsername;

    public GameObjectID getBoundID() {
        return boundID;
    }

    public String getBoundUsername() {
        return boundUsername;
    }
}
