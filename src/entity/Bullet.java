    package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Color;
import map.GameMap;

public class Bullet {

    private int x, y;
    private final int size = 16;
    private final int speed = 8;
    private String direction;
    private boolean active = true;
    private GameMap map;
    private Image image;
//    private Object shooter;



    public Bullet(int x, int y, String direction, GameMap map) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.map = map;
        this.image = Toolkit.getDefaultToolkit().getImage("res/bullet/bullet.png"); 
    }

    public void update() {
        switch (direction) {
            case "up" -> y -= speed;
            case "down" -> y += speed;
            case "left" -> x -= speed;
            case "right" -> x += speed;
        }

        int tile = map.getTile(x + size / 2, y + size / 2);
        if (tile == 3) {
            map.clearTile(x + size / 2, y + size / 2);
            active = false;
        } else if (tile != 0) {
            if (x < 0 || y < 0 || x >= 816 || y >= 816 || map.isBlockedTile(x, y)) {
                active = false;
            }
        }

        if (x < 0 || y < 0 || x > 816 || y > 816) {
            active = false;
        }


    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, size, size, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, size, size);
        }
    }

    public boolean isActive() {
        return active;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public void setInactive() {
        active = false;
    }
    private boolean hasGun = false;

    public void setGunPower(boolean value) {
        this.hasGun = value;
    }

    public boolean hasGunPower() {
        return hasGun;
    }

}