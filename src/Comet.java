/**
 * Trida Comet
 * @author Mikulas Mach
 */
public class Comet extends ASpaceObject{

    /**
     * Konstruktor priradi hodnoty atributum komety
     * @param name jmeno komety
     * @param posX pozice X komety
     * @param posY pozice Y komety
     * @param velX rychlost X komety
     * @param weight hmotnost komety
     */
    public Comet(String name, double posX, double posY, double velX, double velY, double weight){
        super(name, posX, posY, velX, velY, weight);
    }

}
