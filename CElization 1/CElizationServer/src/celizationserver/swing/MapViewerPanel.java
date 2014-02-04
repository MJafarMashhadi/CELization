package celizationserver.swing;

import celization.GameState;
import celization.UserInfo;
import celization.buildings.Building;
import celization.civilians.Civilian;
import celization.equipment.Boat;
import celization.mapgeneration.BlockType;
import celization.mapgeneration.GameMap;
import celization.mapgeneration.LandBlock;
import celizationrequests.Coordinates;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author mjafar
 */
public final class MapViewerPanel extends javax.swing.JPanel {

    private celization.CElization game;
    private double zoomScale = 1.0;
    private int cellSize = 36;
    private double radic_2 = Math.sqrt(2.0);
    private Point mousePosition = new Point(0, 0);
    private boolean mouseOnMap = false;
    private MapViewer mapViewer;
    private boolean showUnits = false;
    public static final Image INFANTRY_SPEAR = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Infantry_Spear.png"));
    public static final Image INFANTRY_SAPPER = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Infantry_Sapper.png"));
    public static final Image HORSEMAN_MACE = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Horseman_Mace.png"));
    public static final Image[] CIVILIAN;
    public static final Image DERP = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Derp.png"));
    //
    public static final Image[] INCOMPLETE_BUILDINGS;
    public static final Image GOLD_MINE = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/GoldMine.png"));
    public static final Image HQ = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/HQ.png"));
    public static final Image STONE_MINE = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/StoneMine.png"));
    public static final Image UNIVERSITY = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/University.png"));
    public static final Image Horseman_Jockey = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Horseman_Jockey.png"));
    public static final Image STORAGE = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/storage.png"));
    public static final Image FARM = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/Field.png"));
    public static final Image WOOD_CAMP = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/WoodCamp.png"));
    public static final Image Boat = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/Boat.png"));
    public static final Image MARKET = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/Market.png"));
    public static final Image PORT = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/Port.png"));
    public static final Image BARRACKS = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/Barracks.png"));
    public static final Image STABLE = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/Stable.png"));

    static {
        CIVILIAN = new Image[16];
        CIVILIAN[0] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_01.png"));
        CIVILIAN[1] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_02.png"));
        CIVILIAN[2] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_03.png"));
        CIVILIAN[3] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_04.png"));
        CIVILIAN[4] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_05.png"));
        CIVILIAN[5] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_06.png"));
        CIVILIAN[6] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_07.png"));
        CIVILIAN[7] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_08.png"));
        CIVILIAN[8] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_09.png"));
        CIVILIAN[9] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_10.png"));
        CIVILIAN[10] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_11.png"));
        CIVILIAN[11] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_12.png"));
        CIVILIAN[12] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_13.png"));
        CIVILIAN[13] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_14.png"));
        CIVILIAN[14] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_15.png"));
        CIVILIAN[15] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/civilians/Civilian_16.png"));

        INCOMPLETE_BUILDINGS = new Image[4];
        INCOMPLETE_BUILDINGS[1] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/notCompleted1.png"));
        INCOMPLETE_BUILDINGS[2] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/notCompleted2.png"));
        INCOMPLETE_BUILDINGS[3] = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/notCompleted3.png"));

    }
    private static final Image PLAIN;
    private static final Image WATER;
    private static final Image MOUNTAIN;
    private static final Image FOREST;

    static {
        PLAIN = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/PlainTile.png"));
        WATER = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/WaterTile.png"));
        MOUNTAIN = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/Mountain.png"));
        FOREST = imageRead(MapViewerPanel.class.getResourceAsStream("gameicons/GrassTile.png"));
    }

    public static Image imageRead(String address) {
        return imageRead(new File(address));
    }

    public static Image imageRead(InputStream stream) {
        try {
            return ImageIO.read(stream);
        } catch (IOException ex) {
            Logger.getLogger(MapViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Image imageRead(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(MapViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Creates new form MapViewerPanel
     */
    public MapViewerPanel() {
        initComponents();
    }

    public MapViewerPanel(celization.CElization game) {
        initComponents();
        setGame(game);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            mapViewer.selectedTool = MapViewer.SelectedTool.SELECT;
            repaint();
            return;
        }
        if ((mapViewer.selectedTool == MapViewer.SelectedTool.ZOOM_IN && !evt.isAltDown())
                || (mapViewer.selectedTool == MapViewer.SelectedTool.ZOOM_OUT && evt.isAltDown())) {
            zoomIn();
        } else if ((mapViewer.selectedTool == MapViewer.SelectedTool.ZOOM_OUT && !evt.isAltDown())
                || (mapViewer.selectedTool == MapViewer.SelectedTool.ZOOM_IN && evt.isAltDown())) {
            zoomOut();
        }
        if (mapViewer.selectedTool == MapViewer.SelectedTool.ZOOM_OUT
                || mapViewer.selectedTool == MapViewer.SelectedTool.ZOOM_IN) {
            resetSize();
            repaint();
        }
        int col = (int) (evt.getX() / (cellSize * zoomScale));
        int row = (int) (evt.getY() / (cellSize * zoomScale));
    }//GEN-LAST:event_formMouseClicked

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        mouseOnMap = true;
        this.repaint(evt.getX(), evt.getY(), evt.getX() + cellSize, evt.getY() + cellSize);
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        mouseOnMap = true;
        this.repaint(evt.getX(), evt.getY(), evt.getX() + cellSize, evt.getY() + cellSize);
    }//GEN-LAST:event_formMouseExited

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        mousePosition.setLocation(evt.getX(), evt.getY());
        int col = (int) (mousePosition.x / (int) (cellSize * zoomScale));
        int row = (int) (mousePosition.y / (int) (cellSize * zoomScale));
        int cellSizeZoomed = (int) (cellSize * zoomScale);

        try {
            LandBlock thisBlock = game.getGameMap().get(col, row);
            this.mapViewer.setResources(thisBlock.getResources(), thisBlock.getType());
        } catch (IndexOutOfBoundsException e) {
        }
        Rectangle mapRectangle = new Rectangle(Math.max(0, evt.getX() - cellSizeZoomed), Math.max(0, evt.getY() - cellSizeZoomed), cellSizeZoomed * 3, cellSizeZoomed * 3);
        this.repaint(mapRectangle);


    }//GEN-LAST:event_formMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void setGame(celization.CElization game) {
        this.game = game;
        resetSize();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (game == null) {
            return;
        }
        int targetSize = (int) (this.zoomScale * cellSize);
        GameMap map = game.getGameMap();
        for (int c = 0; c < map.getGameMapSize().col; ++c) {
            for (int r = 0; r < map.getGameMapSize().row; ++r) {
                Image img = getImage(map.get(c, r).getType());

                int sourceWidth = img.getWidth(this);
                int sourceHeight = img.getHeight(this);
                int sourceSize = Math.min(sourceHeight, sourceWidth);

                if (map.get(c, r).getType() == BlockType.MOUNTAIN) {
                    g.drawImage(MapViewerPanel.PLAIN,
                            c * targetSize, r * targetSize, (c + 1) * targetSize, (r + 1) * targetSize,
                            0, 0, sourceSize, sourceSize, this);
                }
                g.drawImage(img,
                        c * targetSize, r * targetSize, (c + 1) * targetSize, (r + 1) * targetSize,
                        0, 0, sourceSize, sourceSize, this);
            }
        }
        if (mouseOnMap) {
            // make area lighter
            int row;
            int col;
            row = (int) Math.floor(mousePosition.getY() / targetSize);
            col = (int) Math.floor(mousePosition.getX() / targetSize);

            g2d.setColor(new Color(250, 250, 250, 80));
            g2d.fillRect(col * targetSize, row * targetSize, targetSize, targetSize);
        }

        if (showUnits) {

            for (UserInfo ui : game.getUsersList().values()) {
                GameState gameState = ui.getGame();
                for (Boat b : gameState.getBoats().values()) {
                    g.drawImage(Boat, b.getLocation().col * targetSize, b.getLocation().row * targetSize, targetSize, targetSize, this);
                    g.drawString(ui.getUsername(), targetSize * b.getLocation().col - targetSize / 2, targetSize * (b.getLocation().row + 1));
                }

                for (Civilian c : gameState.getCivilians().values()) {
                    if (c.getName().equals("derp")) {
                        g.drawImage(DERP, c.getLocation().col * targetSize, c.getLocation().row * targetSize, targetSize, targetSize, this);
                    } else {
                        g.drawImage(CIVILIAN[c.getOutfitNumber()], c.getLocation().col * targetSize, c.getLocation().row * targetSize, targetSize, targetSize, this);
                    }
                    g.drawString(ui.getUsername(), targetSize * c.getLocation().col - targetSize / 2, targetSize * (c.getLocation().row + 1));
                }

                for (Building b : gameState.getBuildings().values()) {
                    Image img = null;
                    int size = b.size.row;
                    int r = b.getLocation().row;
                    int c = b.getLocation().col;

                    if (!b.buildBuildingFinished()) {
                        img = INCOMPLETE_BUILDINGS[size];
                    } else {
                        if (b instanceof celization.buildings.HeadQuarters) {
                            img = HQ;
                        } else if (b instanceof celization.buildings.University) {
                            img = UNIVERSITY;
                        } else if (b instanceof celization.buildings.extractables.GoldMine) {
                            img = GOLD_MINE;
                        } else if (b instanceof celization.buildings.extractables.StoneMine) {
                            img = STONE_MINE;
                        } else if (b instanceof celization.buildings.extractables.Farm) {
                            img = FARM;
                        } else if (b instanceof celization.buildings.extractables.WoodCamp) {
                            img = WOOD_CAMP;
                        } else if (b instanceof celization.buildings.Stable) {
                            img = STABLE;
                        } else if (b instanceof celization.buildings.Barracks) {
                            img = BARRACKS;
                        } else if (b instanceof celization.buildings.Port) {
                            img = PORT;
                        } else if (b instanceof celization.buildings.Market) {
                            img = MARKET;
                        } else if (b instanceof celization.buildings.Storage) {
                            img = STORAGE;
                        }
                    }

                    g.drawImage(img, c * targetSize, r * targetSize, (c + size) * targetSize, (r + size) * targetSize, this);
                    g.drawString(ui.getUsername(), targetSize * c - targetSize / 2, targetSize * (r + 1));
                }
            }
        }
    }

    protected void resetSize() {
        Coordinates mapSize = this.game.getGameMap().getGameMapSize();
        this.setPreferredSize(new Dimension(
                (int) (mapSize.col * this.zoomScale * cellSize),
                (int) (mapSize.row * this.zoomScale * cellSize)));
        mapViewer.scroller.updateUI();
    }

    public double getZoomScale() {
        return zoomScale;
    }

    public void setZoomScale(double zoomScale) {
        this.zoomScale = zoomScale;
        resetSize();
    }

    public void zoomIn() {
        zoomScale *= radic_2;
    }

    public void zoomOut() {
        zoomScale /= radic_2;
    }

    private Image getImage(celization.mapgeneration.BlockType type) {
        switch (type) {
            case PLAIN:
                return MapViewerPanel.PLAIN;
            case WATER:
                return MapViewerPanel.WATER;
            case MOUNTAIN:
                return MapViewerPanel.MOUNTAIN;
            case JUNGLE:
                return MapViewerPanel.FOREST;
        }
        return null;
    }

    void setMapViewerInstance(MapViewer mapViewer) {
        this.mapViewer = mapViewer;
    }

    void setShowUnits(boolean showUnits) {
        this.showUnits = showUnits;
        this.repaint();
    }
}