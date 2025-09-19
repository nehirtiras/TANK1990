//package powerup;
//
//import java.awt.Image;
//import java.util.List;
//
//import entity.PlayerTank;
//import entity.BasicTank;
//import entity.FastTank;
//import entity.PowerTank;
//import entity.ArmorTank;
//
//public class GrenadePowerUp extends PowerUp {
//
//    private List<BasicTank> basicTanks;
//    private List<FastTank> fastTanks;
//    private List<PowerTank> powerTanks;
//    private List<ArmorTank> armorTanks;
//    private PlayerTank player;
//    private boolean collectedByPlayer;
//
//    public GrenadePowerUp(int x, int y, Image image,
//                          List<BasicTank> basicTanks,
//                          List<FastTank> fastTanks,
//                          List<PowerTank> powerTanks,
//                          List<ArmorTank> armorTanks,
//                          PlayerTank player,
//                          boolean collectedByPlayer) {
//        super(x, y, image);
//        this.basicTanks = basicTanks;
//        this.fastTanks = fastTanks;
//        this.powerTanks = powerTanks;
//        this.armorTanks = armorTanks;
//        this.player = player;
//        this.collectedByPlayer = collectedByPlayer;
//    }
//
//    @Override
//    public void applyToPlayer() {
//        basicTanks.forEach(t -> t.setDestroyed(true));
//        fastTanks.forEach(t -> t.setDestroyed(true));
//        powerTanks.forEach(t -> t.setDestroyed(true));
//        armorTanks.forEach(t -> t.setDestroyed(true));
//    }
//
//    @Override
//    public void applyToEnemy() {
//        player.resetPowerUps();
//        player.setDestroyed(true);
//    }
//}
