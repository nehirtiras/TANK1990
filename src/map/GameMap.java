package map;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;


public class GameMap {

    private final int COLS = 17, ROWS = 17;
    private int[][] mapData = new int[ROWS][COLS];
    private int tileSize;

    private Image brick, steel, tree, eagle, water, ice, gray_wall, flag, tank_tree;

    private int playerStartX = -1;
    private int playerStartY = -1;

    private boolean eagleDestroyed = false;
    private int eagleX, eagleY;

    private ArrayList<Point> basicTankSpawns = new ArrayList<>();
    private ArrayList<Point> fastTankSpawns = new ArrayList<>();
    private ArrayList<Point> powerTankSpawns = new ArrayList<>();
    private ArrayList<Point> armorTankSpawns = new ArrayList<>();
    private ArrayList<Point> enemySpawnPoints = new ArrayList<>(); // ✅ yeni

    public ArrayList<Point> getBasicTankSpawns() { return basicTankSpawns; }
    public ArrayList<Point> getFastTankSpawns() { return fastTankSpawns; }
    public ArrayList<Point> getPowerTankSpawns() { return powerTankSpawns; }
    public ArrayList<Point> getArmorTankSpawns() { return armorTankSpawns; }
    public ArrayList<Point> getEnemySpawnPoints() { return enemySpawnPoints; } // ✅ getter

    public int getPlayerStartX() { return playerStartX; }
    public int getPlayerStartY() { return playerStartY; }
//map için gerekli tilelar
    public GameMap(int tileSize) {
        this.tileSize = tileSize;

        brick = Toolkit.getDefaultToolkit().getImage("res/tiles/brick_wall.png");
        steel = Toolkit.getDefaultToolkit().getImage("res/tiles/steel_wall.png");
        tree  = Toolkit.getDefaultToolkit().getImage("res/tiles/trees.png");
        eagle = Toolkit.getDefaultToolkit().getImage("res/kartal/kartal.png");
        water = Toolkit.getDefaultToolkit().getImage("res/tiles/water.png");
        ice   = Toolkit.getDefaultToolkit().getImage("res/tiles/ice.png");
        gray_wall = Toolkit.getDefaultToolkit().getImage("res/tiles/gray_wall.png");
        flag = Toolkit.getDefaultToolkit().getImage("res/tiles/flag.png");
        tank_tree = Toolkit.getDefaultToolkit().getImage("res/tiles/tank_tree.png");

        loadMap("res/map/map.txt");
    }
//harita oluşturma
    private void loadMap(String path) {
        try (Scanner scanner = new Scanner(new File(path))) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (scanner.hasNextInt()) {
                        int value = scanner.nextInt();
                        mapData[row][col] = value;

                        int x = col * tileSize;
                        int y = row * tileSize;

                        if (value == 2) {
                            playerStartX = x;
                            playerStartY = y;
                        }

                        if (value == 1) {
                            eagleX = x;
                            eagleY = y;
                        }

                        switch (value) {
                            case 10 -> basicTankSpawns.add(new Point(x, y));
                            case 11 -> fastTankSpawns.add(new Point(x, y));
                            case 12 -> powerTankSpawns.add(new Point(x, y));
                            case 13 -> armorTankSpawns.add(new Point(x, y));
                            case 20, 21, 22 -> enemySpawnPoints.add(new Point(x, y)); // ✅ yeni doğum noktaları
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Map okunamadı: " + e.getMessage());
        }
    }


    public void drawTiles(Graphics g) {
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[0].length; col++) {
                int tile = mapData[row][col];
                int x = col * tileSize;
                int y = row * tileSize;

                if (tile == 1 && eagleDestroyed) continue;

                switch (tile) {
              case 1 -> g.drawImage(eagle, x, y, tileSize, tileSize, null);
              case 3 -> g.drawImage(brick, x, y, tileSize, tileSize, null);
              case 4 -> g.drawImage(tree, x, y, tileSize, tileSize, null);
              case 5 -> g.drawImage(steel, x, y, tileSize, tileSize, null);
              case 6 -> g.drawImage(water, x, y, tileSize, tileSize, null);
              case 7 -> g.drawImage(ice, x, y, tileSize, tileSize, null);
              case 8 -> g.drawImage(gray_wall, x, y, tileSize, tileSize, null);
              case 9 -> g.drawImage(flag, x, y, tileSize, tileSize, null);
              case 10 -> g.drawImage(tank_tree, x, y, tileSize, tileSize, null);
                    default -> {}
                }
            }
        }
    }
//    public void upgradeEagleWalls() {
//        for (int y = 0; y < mapData.length; y++) {
//            for (int x = 0; x < mapData[0].length; x++) {
//                if (isEagleWall(x, y)) {
//                    mapData[y][x] = 5; // 5 = çelik duvar
//                }
//            }
//        }
//    }
//    private boolean isEagleWall(int x, int y) {
//
//        return (mapData[y][x] == 3 || mapData[y][x] == 5) && isNearEagle(x, y);
//    }
//
//    private boolean isNearEagle(int x, int y) {
//        for (int dy = -1; dy <= 1; dy++) {
//            for (int dx = -1; dx <= 1; dx++) {
//                int nx = x + dx;
//                int ny = y + dy;
//                if (ny >= 0 && ny < mapData.length && nx >= 0 && nx < mapData[0].length) {
//                    if (mapData[ny][nx] == 8) return true; // 8 = kartal
//                }
//            }
//        }
//        return false;
//    }

//    public void removeEagleWalls() {
//        for (int y = 0; y < mapData.length; y++) {
//            for (int x = 0; x < mapData[0].length; x++) {
//                if (isEagleWall(x, y)) {
//                    mapData[y][x] = 0; 
//                }
//            }
//        }
//    }
//    public void restoreEagleWalls() {
//        for (int y = 0; y < mapData.length; y++) {
//            for (int x = 0; x < mapData[0].length; x++) {
//                if (isEagleWall(x, y)) {
//
//                    if (mapData[y][x] == 5) {
//                        mapData[y][x] = 3;
//                    }
//                }
//            }
//        }
//    }


//ağacın altından tank geçmesi durumu
    public void drawTreeOverlays(Graphics g, List<Rectangle> tankRects) {
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[0].length; col++) {
                if (mapData[row][col] != 4) continue;

                int x = col * tileSize;
                int y = row * tileSize;
                Rectangle tileRect = new Rectangle(x, y, tileSize, tileSize);

                for (Rectangle tankRect : tankRects) {
                    if (tileRect.intersects(tankRect)) {
                        g.drawImage(tank_tree, x, y, tileSize, tileSize, null);
                        break;
                    }
                }
            }
        }
    }


    public int getTile(int x, int y) {
        int col = x / tileSize;
        int row = y / tileSize;
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            return mapData[row][col];
        }
        return -1;
    }
//blok yok etme
    public void clearTile(int x, int y) {
        int col = x / tileSize;
        int row = y / tileSize;
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            if (mapData[row][col] == 3) {
                mapData[row][col] = 0;
            }
        }
    }
//üzerinden geçilemeyen tilelar ayarlanır
    public boolean isBlockedTile(int xPixel, int yPixel) {
        int col = xPixel / tileSize;
        int row = yPixel / tileSize;
        if (col < 0 || col >= COLS || row < 0 || row >= ROWS) return true;
        int tile = mapData[row][col];
        return tile == 1 || tile == 3 || tile == 5 || tile == 6 || tile == 8;
    }

    public Point findTilePosition(int target) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (mapData[row][col] == target) {
                    return new Point(col * tileSize, row * tileSize);
                }
            }
        }
        return null;
    }

    public Rectangle getEagleRect() {
        return new Rectangle(eagleX, eagleY, tileSize, tileSize);
    }

    public void destroyEagle() {
        eagleDestroyed = true;
        int row = eagleY / tileSize;
        int col = eagleX / tileSize;
        mapData[row][col] = 0;
    }

    public boolean isEagleDestroyed() {
        return eagleDestroyed;
    }
    public int getTileSize() {
        return tileSize;
    }
    public void setTile(int col, int row, int value) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            mapData[row][col] = value;
        }
    }
    public int[][] getMapData() {
        return mapData;
    }

}

