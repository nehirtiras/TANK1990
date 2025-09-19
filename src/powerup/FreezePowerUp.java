//package powerup;
//
//import java.awt.Image;
//import java.util.List;
//import entity.PlayerTank;
//import entity.BasicTank;
//import entity.FastTank;
//import entity.PowerTank;
//import entity.ArmorTank;
//
//public class FreezePowerUp extends PowerUp {
//
//    private PlayerTank player;
//    private List<BasicTank> basicTanks;
//    private List<FastTank> fastTanks;
//    private List<PowerTank> powerTanks;
//    private List<ArmorTank> armorTanks;
//    private boolean collectedByPlayer;
//
//    public FreezePowerUp(int x, int y, Image image,
//                         PlayerTank player,
//                         List<BasicTank> basicTanks,
//                         List<FastTank> fastTanks,
//                         List<PowerTank> powerTanks,
//                         List<ArmorTank> armorTanks,
//                         boolean collectedByPlayer) {
//        super(x, y, image);
//        this.player = player;
//        this.basicTanks = basicTanks;
//        this.fastTanks = fastTanks;
//        this.powerTanks = powerTanks;
//        this.armorTanks = armorTanks;
//        this.collectedByPlayer = collectedByPlayer;
//    }
//
//    @Override
//    public void applyToPlayer() {
//
//        freezeEnemies();
//    }
//
//    @Override
//    public void applyToEnemy() {
//
//        freezePlayer();
//    }
//
//    private void freezeEnemies() {
//        new Thread(() -> {
//            setEnemyFrozen(true);
//            try {
//                Thread.sleep(5000); 
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            setEnemyFrozen(false);
//        }).start();
//    }
//
//    private void setEnemyFrozen(boolean frozen) {
//        for (BasicTank t : basicTanks) t.setFrozen(frozen);
//        for (FastTank t : fastTanks) t.setFrozen(frozen);
//        for (PowerTank t : powerTanks) t.setFrozen(frozen);
//        for (ArmorTank t : armorTanks) t.setFrozen(frozen);
//    }
//
//    private void freezePlayer() {
//        new Thread(() -> {
//            player.setFrozen(true);
//            try {
//                Thread.sleep(5000); 
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            player.setFrozen(false);
//        }).start();
//    }
//}
