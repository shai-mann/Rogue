package map.level;
import helper.Helper;
import main.GameManager;
import map.CustomCellRenderer;
import map.CustomRoomTable;
import map.RoomTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class Level extends JComponent {

    /*
    * LEVEL CLASS:
    * The map class is made of a grid (represented by a JTable) which things such as the player and monsters
    * can be added to
    * Rooms can only be from 11 length to 5
    * Rooms must be odd lengths so as to have a center
     */

    private JPanel panel;
    private CustomRoomTable table;
    private Random random = new Random();
    private ArrayList zones;

    private static Level level;

    public Level() {
        level = this;

        Room.zones = Room.setZones();
        zones = (ArrayList) Room.zones.clone();

        setDefaults();
        createRooms();
    }
    /*
    * Level size is 69 x 39
     */
    public void createRooms() {
        // Add something to make them in different zones each time LoL
        int roomNumber = random.nextInt(4) + 5;

        for (int i = 0; i < roomNumber; i++) {
            Point center;
            Dimension size;
            do {
                center = getRandomPoint();
                try {
                    size = getRandomRoomSize(center);
                } catch (Exception e) {
                    size = null;
                }
            } while(size == null);
            new Room(table.getCustomModel(), center, size);
        }
    }
    private Point getRandomPoint() {
        Point point;
        Polygon zone;

        do {
            // TODO: Should be changed to only make the random points inside of the zones (randomly selected, but never selected twice)
            zone = (Polygon) zones.get(random.nextInt(zones.size() - 1));
            point = new Point(random.nextInt(zone.getBounds().width) + (int) zone.getBounds().getMinX(),
                    random.nextInt(zone.getBounds().height) + (int) zone.getBounds().getMinY());


            if (checkValidPoint(point)) {
                return point;
            } else {
                point = null;
            }
        } while (point == null);
        zones.remove(zone);
        return point;
    }
    private Dimension getRandomRoomSize(Point p) throws Exception {
        Dimension dim;
        int iterations = 0;

        do {
            dim = new Dimension(random.nextInt(6) + 5, random.nextInt(6) + 5);

            if (Room.checkValidSpace(p.x, p.y, dim)) {
                return dim;
            } else {
                dim = null;
            }
            iterations++;

            if (iterations == 10) {
                throw new Exception("Entered infinite loop");
            }
        } while (dim == null);
        return dim;
    }
    private boolean checkValidPoint(Point p) {
        try {
            return GameManager.getTable().getValueAt(p.y, p.x) == "";
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setMaximumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setMinimumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setBorder(null);
        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
    }
    private void createUIComponents() {
        RoomTableModel model = createTableModel();
        table = new CustomRoomTable(model);

        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setForeground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
        table.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 12));

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
            table.getColumnModel().getColumn(i).setMinWidth(20);
            table.getColumnModel().getColumn(i).setMaxWidth(20);
        }
    }
    private RoomTableModel createTableModel() {
        RoomTableModel model = new RoomTableModel();
        for (int j = 0; j != 69; j++) {
            model.addColumn("");
        }
        for (int i = 0; i != 39; i++) {
            model.addRow(createTableRow());
        }
        return model;
    }
    private String[] createTableRow() {
        String[] rowValueList = new String[69];
        Arrays.fill(rowValueList, "");

        return rowValueList;
    }
    public CustomRoomTable getTable() {
        return table;
    }
    public static Level getLevel() {
        return level;
    }
}
