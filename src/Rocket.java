/**
 * Trida Rocket
 * @author Mikulas Mach
 */
public class Rocket extends ASpaceObject{

    /**
     * Konstruktor priradi hodnoty atributum rakety
     * @param name jmeno planety
     * @param posX pozice X planety
     * @param posY pozice Y planety
     * @param velX rychlost X planety
     * @param weight hmotnost planety
     */
    public Rocket(String name, double posX, double posY, double velX, double velY, double weight){
        super(name, posX, posY, velX, velY, weight);
    }


}
