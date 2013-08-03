/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationserver.swing;

import celization.mapgeneration.perlinnoise.PerlinNoiseGenerator;
import celization.mapgeneration.perlinnoise.PerlinNoiseParameters;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author mjafar
 */
public class PerlinNoisePanel extends javax.swing.JPanel {

    private PerlinNoiseGenerator generator;
    private double cellWidth;
    private int mapWidth, mapHeight;
    private Color backgroundColor = Color.GRAY;
    private Color plainColor = new Color(243, 143, 25);
    private Color mountainColor = new Color(148, 87, 45);
    private Color forestColor = new Color(128, 255, 103);
    private Color waterColor = Color.BLUE;

    /**
     * Creates new form PerlinNoisePanel
     */
    public PerlinNoisePanel() {
        generator = new PerlinNoiseGenerator();
    }

    public void setPerlinNoiseParameters(PerlinNoiseParameters newParams) {
        generator.changeParameters(newParams);
    }

    public void setMapSize(Integer width, Integer height) {
        mapWidth = width.intValue();
        mapHeight = height.intValue();
        int panelWidth = this.getSize().width;
        int panelHeight = this.getSize().height;

        double mapRatio = (double) mapWidth / (double) mapHeight;
        double panelRatio = (double) panelWidth / (double) panelHeight;

        if (mapRatio > panelRatio) {
            cellWidth = (double) panelWidth / (double) mapWidth;
        } else {
            cellWidth = (double) panelHeight / (double) mapHeight;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Rectangle block;
        Point corner = new Point(0, 0);
        Color blockColor;

        int step;
        if (cellWidth >= 1) {
            block = new Rectangle(corner, new Dimension((int) Math.ceil(cellWidth),(int) Math.ceil(cellWidth)));
            step = 1;
        } else {
            block = new Rectangle(corner, new Dimension(1, 1));
            step = (int) Math.ceil(1 / cellWidth);
        }

        double height;
        double max, min;
        min = max = generator.get(0, 0);
        for (int x = 0; x < mapWidth; x += step) {
            for (int y = 0; y < mapHeight; y += step) {
                blockColor = backgroundColor;

                height = 0;
                if (step > 1) {
                    for(int X = x-step ; X < x; X++)
                    for(int Y = y-step ; Y < y; Y++)
                        try {
                           height += generator.get(X, Y);
                        } catch(ArrayIndexOutOfBoundsException e) {}

                    height /= (step * step);
                } else {
                    height = generator.get(x, y);
                }
                height /= 20;
                /**
                 * Assign each value to a land type
                 */

                if (0.0 <= height && height < 0.03) {
                    blockColor = waterColor;
                } else if (0.03 <= height && height < 0.05) {
                    blockColor = forestColor;
                } else if (0.05 <= height && height < 0.15) {
                    blockColor = plainColor;
                } else if (0.15 <= height && height <= 1) {
                    blockColor = mountainColor;
                }

                g2d.setColor(blockColor);

                corner.x = x * (int) block.getWidth();
                corner.y = y * (int) block.getHeight();

                block.setLocation(corner);

                g2d.draw(block);
                g2d.fill(block);
            }
        }

        g2d.setBackground(backgroundColor);
    }
}
