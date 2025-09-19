//package powerup;
//
//import java.awt.Image;
//import entity.PlayerTank;
//import entity.BasicTank;
//import entity.FastTank;
//import entity.PowerTank;
//import entity.ArmorTank;
//
//public class GunPowerUp extends PowerUp {
//
//    private PlayerTank player;
//    private BasicTank basicTank;
//    private FastTank fastTank;
//    private PowerTank powerTank;
//    private ArmorTank armorTank;
//    private boolean collectedByPlayer;
//
//    public GunPowerUp(int x, int y, Image image, PlayerTank player) {
//        super(x, y, image);
//        this.player = player;
//        this.collectedByPlayer = true;
//    }
//
//    public GunPowerUp(int x, int y, Image image,
//                      BasicTank basicTank, FastTank fastTank,
//                      PowerTank powerTank, ArmorTank armorTank) {
//        super(x, y, image);
//        this.basicTank = basicTank;
//        this.fastTank = fastTank;
//        this.powerTank = powerTank;
//        this.armorTank = armorTank;
//        this.collectedByPlayer = false;
//    }
//
//    @Override
//    public void applyToPlayer() {
//        if (player != null) {
//            player.setGunPower(true);
//        }
//    }
//
//    @Override
//    public void applyToEnemy() {
//        if (basicTank != null) basicTank.setGunPower(true);
//        if (fastTank != null) fastTank.setGunPower(true);
//        if (powerTank != null) powerTank.setGunPower(true);
//        if (armorTank != null) armorTank.setGunPower(true);
//    }
//}
