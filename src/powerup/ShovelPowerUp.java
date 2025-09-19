//package powerup;
//
//import java.awt.Image;
//import map.GameMap;
//
//public class ShovelPowerUp extends PowerUp {
//
//    private GameMap map;
//    private boolean collectedByPlayer;
//
//    public ShovelPowerUp(int x, int y, Image image, GameMap map, boolean collectedByPlayer) {
//        super(x, y, image);
//        this.map = map;
//        this.collectedByPlayer = collectedByPlayer;
//    }
//
//    @Override
//    public void applyToPlayer() {
//        if (map != null) {
//            map.upgradeEagleWalls(); // tuğla → çelik
//        }
//    }
//
//    @Override
//    public void applyToEnemy() {
//        if (map != null) {
//            map.removeEagleWalls(); // tüm duvarları sil
//        }
//    }
//}
