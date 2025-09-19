package entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import map.GameMap;

public class ArmorTank {

    private int x, y;
    private final int size = 48;
    private final int speed = 2;
    private int health = 4;

    private String direction = "down";
    private Image imageGreen;
    private Image imageGray;
    private final GameMap map;
    private final Random random = new Random();
    private int moveCounter = 0;
    private int fireCooldown = 0;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public ArmorTank(int x, int y, GameMap map) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.imageGreen = Toolkit.getDefaultToolkit().getImage("res/tanks/armor_tank1.png");
        this.imageGray = Toolkit.getDefaultToolkit().getImage("res/tanks/armor_tank2.png");
    }

    public void update() {
        if (moveCounter % 30 == 0) {
            direction = switch (random.nextInt(4)) {
                case 0 -> "up";
                case 1 -> "down";
                case 2 -> "left";
                case 3 -> "right";
                default -> "down";
            };
        }

        int nextX = x, nextY = y;
        switch (direction) {
            case "up" -> nextY -= speed;
            case "down" -> nextY += speed;
            case "left" -> nextX -= speed;
            case "right" -> nextX += speed;
        }

        if (canMoveTo(nextX, nextY)) {
            x = nextX;
            y = nextY;
        }

        fireCooldown++;
        if (fireCooldown >= 100) {
            fireBullet();
            fireCooldown = 0;
        }

        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (!b.isActive()) it.remove();
        }

        moveCounter++;
    }

    private boolean canMoveTo(int nextX, int nextY) {
        int left = nextX;
        int right = nextX + size - 1;
        int top = nextY;
        int bottom = nextY + size - 1;

        return !map.isBlockedTile(left, top) &&
               !map.isBlockedTile(right, top) &&
               !map.isBlockedTile(left, bottom) &&
               !map.isBlockedTile(right, bottom);
    }

    public void draw(Graphics g) {
        g.drawImage(getCurrentImage(), x, y, size, size, null);
        for (Bullet b : bullets) b.draw(g);
    }

    private Image getCurrentImage() {
        return switch (health) {
            case 4 -> imageGreen;
            default -> imageGray;
        };
    }

    public boolean hit() {
        if (health > 0) health--;
        return health == 0;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }

    private void fireBullet() {
        int bx = x + size / 2 - 8;
        int by = y + size / 2 - 8;
        bullets.add(new Bullet(bx, by, direction, map));
    }
}
