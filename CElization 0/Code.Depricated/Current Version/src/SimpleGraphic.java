/**
 *
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import celization.CElization;
import celization.GameParameters;
import celization.civilians.Civilian;
import celization.mapgeneration.BlockType;
import org.omg.CORBA.UNKNOWN;

/**
 * @author mjafar
 *
 */
public class SimpleGraphic {
	public static int w = 5;
	public static Dimension s = new Dimension(w, w);
	public static CElization game;
	public static GameFrame frame;
	public static JFrame mousePos;
	public static JLabel mouseClickLabel = new JLabel();
	public static JLabel mouseMoveLabel = new JLabel();

	/**
	 * @param args
	 */
	public static void main(CElization g) {
		game = g;


		mouseClickLabel.setText(String.format("Row: %03d  Col: %03d", 0, 0));
		mouseMoveLabel.setText(String.format("Row: %03d  Col: %03d", 0, 0));
		mousePos = new JFrame("CELization - info");
		mousePos.setLayout(new GridLayout(2, 0, 3,6));
		mousePos.add(mouseClickLabel);
		mousePos.add(mouseMoveLabel);
		mousePos.setBounds(10, 10, 1, 1);
		mousePos.setResizable(false);
		mousePos.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mousePos.pack();
		mousePos.setAlwaysOnTop(true);
		mousePos.setVisible(true);

		frame = new GameFrame(w);
		frame.addMouseListener(new MouseHandler());
	}

	public static void refresh() {
		System.err.println("refreshed view");
	}

	public static void close() {
		frame.dispose();
	}
}

@SuppressWarnings("serial")
class GameFrame extends javax.swing.JFrame {
	int w;
	ShowFullMap fullMap;
	ShowDoo doo;

	JLayeredPane layers = new JLayeredPane();

	Integer DOO_LAYER = new Integer(2);
	Integer MAP_LAYER = new Integer(1);

	public GameFrame(int w) {
		super("CELization");
		System.err.println("Raw window created");
		this.w = w;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(300, 170, 2, 2);
		setSize(w * GameParameters.gameMapSize.col, w
				* GameParameters.gameMapSize.row + 34);
		setLayout(null);
		System.err.println("Window initialized");

		layers.setSize(this.getSize());

		showFullMap();
		refresh();

		setVisible(true);
		System.err.println("Set window visible");
		System.err.flush();
	}

	public void refresh() {
		if (doo != null) {
			deleteOldDoos();
			System.err.println("deleted old doos");
			System.err.flush();
		}

		showDoos();
		System.err.println("Show doos");
	}

	public void showFullMap() {
		fullMap = new ShowFullMap();
		System.err.println("map created");
		System.err.flush();
		fullMap.setBounds(0, 0, w * GameParameters.gameMapSize.col, w
				* GameParameters.gameMapSize.row);

		layers.add(fullMap, MAP_LAYER);

		System.err.println("map added");
		System.err.flush();
	}

	public void deleteOldDoos() {
//		layers.remove(doo);
//		doo = null;
	}

	public void showDoos() {
		doo = new ShowDoo();
		doo.repaint();
		layers.add(doo, DOO_LAYER);
	}
}

@SuppressWarnings("serial")
class ShowDoo extends JPanel {
	@Override
	public void paintComponents(Graphics g) {
		System.err.println("Hey!");
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setPaint(Color.red);
		double x, y;
		for (Civilian c : SimpleGraphic.game) {
			System.err.println(c.getPos());
			x = c.getPos().col * SimpleGraphic.w;
			y = c.getPos().row * SimpleGraphic.w;

			Shape circle = new Ellipse2D.Double(x, y, SimpleGraphic.w,
					SimpleGraphic.w);
			g2d.draw(circle);
			g2d.fill(circle);
		}
	}
}

class MouseHandler implements MouseListener, MouseMotionListener {

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int row, col;
		row = ((arg0.getLocationOnScreen().y - SimpleGraphic.frame
				.getLocation().y) / SimpleGraphic.w) - 5;
		col = (arg0.getLocationOnScreen().x - SimpleGraphic.frame.getLocation().x)
				/ SimpleGraphic.w;
		if (row >= 0 && col >= 0) {
			SimpleGraphic.mouseMoveLabel.setText(String.format(
					"Row: %03d  Col: %03d", row, col));
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int row, col;
		row = ((arg0.getLocationOnScreen().y - SimpleGraphic.frame
				.getLocation().y) / SimpleGraphic.w) - 5;
		col = (arg0.getLocationOnScreen().x - SimpleGraphic.frame.getLocation().x)
				/ SimpleGraphic.w;
		if (row >= 0 && col >= 0) {
			SimpleGraphic.mouseClickLabel.setText(String.format(
					"Row: %03d  Col: %03d", row, col));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}

@SuppressWarnings("serial")
class ShowFullMap extends JPanel {

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		this.setBackground(new Color(243, 143, 25));
		g2d.setStroke(new BasicStroke(0));

		Color c = new Color(128, 255, 103);

		Rectangle block;
		Point corner = new Point(0, 0);

		block = new Rectangle(corner, SimpleGraphic.s);
		for (int i = 0; i < GameParameters.gameMapSize.col; i++) {
			for (int j = 0; j < GameParameters.gameMapSize.row; j++) {

				switch (SimpleGraphic.game.gameMap.map[i][j].getType(false)) {
				case WATER:
					c = Color.blue;
					break;
				case JUNGLE:
					c = new Color(128, 255, 103);
					break;
				case MOUNTAIN:
					c = new Color(148, 87, 45);
					break;
				case UNKNOWN:
				default:
					c = Color.black;
					break;
				}

				g2d.setPaint((Paint) c);

				if (SimpleGraphic.game.gameMap.map[i][j].getType(false) != BlockType.PLAIN) {
					corner.x = i * SimpleGraphic.w;
					corner.y = j * SimpleGraphic.w;
					block.setLocation(corner);
					g2d.draw(block);
					g2d.fill(block);
				}
			}
		}
	}
}
