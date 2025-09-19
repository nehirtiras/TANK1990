package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import map.GameMap;

public class PlayerTank {

    private int x, y;
    private final int size = 46;
    private int speed = 4;

    private String direction = "up";
    private Image upImage, downImage, leftImage, rightImage;
    private GameMap map;


    public PlayerTank(int x, int y, GameMap map) {
        this.x = x;
        this.y = y;
        this.map = map;
        upImage = Toolkit.getDefaultToolkit().getImage("res/tanks/player_tank_up.png");
        downImage = Toolkit.getDefaultToolkit().getImage("res/tanks/player_tank_down.png");
        leftImage = Toolkit.getDefaultToolkit().getImage("res/tanks/player_tank_left.png");
        rightImage = Toolkit.getDefaultToolkit().getImage("res/tanks/player_tank_right.png");

    }
    private int health = 3;

    public boolean hit() {
        health--;
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public void draw(Graphics g) {
        Image currentImage = switch (direction) {
            case "up" -> upImage;
            case "down" -> downImage;
            case "left" -> leftImage;
            case "right" -> rightImage;
            default -> upImage;
        };

        g.drawImage(currentImage, x, y, size, size, null);
    }

    public void update() {
        int nextX = x;
        int nextY = y;

        switch (direction) {
            case "up"    -> nextY -= speed;
            case "down"  -> nextY += speed;
            case "left"  -> nextX -= speed;
            case "right" -> nextX += speed;
        }


        boolean topLeft     = map.isBlockedTile(nextX, nextY);
        boolean topRight    = map.isBlockedTile(nextX + size - 1, nextY);
        boolean bottomLeft  = map.isBlockedTile(nextX, nextY + size - 1);
        boolean bottomRight = map.isBlockedTile(nextX + size - 1, nextY + size - 1);

        if (!(topLeft || topRight || bottomLeft || bottomRight)) {
            x = nextX;
            y = nextY;
        }
    }
    public void resetToTile(int tileValue) {
        Point p = map.findTilePosition(tileValue);
        if (p != null) {
            this.x = p.x;
            this.y = p.y;
            this.direction = "up";
            
        }
    }



    public void setDirection(String dir) {
        this.direction = dir;
    }

    public String getDirection() {
        return direction;
    }

    public void move() {
        update();
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void setHealth(int health) {
        this.health = health;
    }

}

