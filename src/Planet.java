/**
 * Trida Planet
 * @author Mikulas Mach
 */
public class Planet extends ASpaceObject{

    /**
     * Konstruktor priradi hodnoty atributum planety
     * @param name jmeno planety
     * @param posX pozice X planety
     * @param posY pozice Y planety
     * @param velX rychlost X planety
     * @param weight hmotnost planety
     */
    public Planet(String name, double posX, double posY, double velX, double velY, double weight) {
        super(name, posX, posY, velX, velY, weight);
    }

}
