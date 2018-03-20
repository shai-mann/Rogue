package entity.monster;

public class Zombie extends Monster {
    public Zombie(int x, int y) {
        super("Z", x, y);

        speed = 2;
        hitChance = 0.5;
        hitDamage = 1;
        name = "Zombie";
    }
}
