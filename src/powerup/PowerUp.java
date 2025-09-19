//package powerup;
//
//import java.awt.*;
//import java.awt.image.ImageObserver;
//
//public abstract class PowerUp {
//    protected int x, y;
//    protected Image image;
//    protected boolean active = true;
//
//    public PowerUp(int x, int y, Image image) {
//        this.x = x;
//        this.y = y;
//        this.image = image;
//    }
//
//    public abstract void applyToPlayer();
//    public abstract void applyToEnemy();
//
//    public void draw(Graphics g, ImageObserver obs) {
//        if (active && image != null) {
//            g.drawImage(image, x, y, obs);
//        }
//    }
//
//    public Rectangle getBounds() {
//        return new Rectangle(x, y, 48, 48);
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void deactivate() {
//        this.active = false;
//    }
//}
