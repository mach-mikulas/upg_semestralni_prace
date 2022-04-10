/**
 * Abstrakni trida udavajisi strukturu spaceobjectu
 * @author Mikulas Mach
 */
public abstract class ASpaceObject {

    /** Jmeno spaceObjectu*/
    private String name;
    /** Pozice X spaceObjectu*/
    private double posX;
    /** Pozice Y spaceObjectu*/
    private double posY;
    /** Rychlost X spaceObjectu*/
    private double velX;
    /** Rychlost Y spaceObjectu*/
    private double velY;
    /** Hmotnost spaceObjectu*/
    private double weight;
    /** Polomer spaceObjectu*/
    private double r;
    /** zda byl spaceObjectu aktualne zvolen*/
    private boolean clicked;
    /** zrychleni X spaceObjectu*/
    private double aX;
    /** zrychleni Y spaceObjectu*/
    private double aY;

    /**
     * Konstruktor priradi hodnoty atributum a vypocita polomer spaceObjectu
     * @param name jmeno
     * @param posX pozice X
     * @param posY pozice Y
     * @param velX rychlost X
     * @param weight hmotnost
     */
    public ASpaceObject(String name, double posX, double posY, double velX, double velY, double weight) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.weight = weight;
        this.clicked = false;
        this.r = (Math.cbrt(6*weight/Math.PI))/2;
    }

    /**
     * Getter
     * @return string name - jmeno spaceObjectu
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     * @return double posX - pozice X spaceObjectu
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Getter
     * @return double posY - pozice Y spaceObjectu
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Getter
     * @return double velX - rychlost X spaceObjectu
     */
    public double getVelX() {
        return velX;
    }

    /**
     * Getter
     * @return double velY - rychlost Y spaceObjectu
     */
    public double getVelY() {
        return velY;
    }

    /**
     * Getter
     * @return double aX - zrychleni X spaceObjectu
     */
    public double getaX() {
        return aX;
    }

    /**
     * Getter
     * @return double aY - zrychleni Y spaceObjectu
     */
    public double getaY() {
        return aY;
    }

    /**
     * Getter
     * @return double weight - hmotnost spaceObjectu
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Getter
     * @return double r - polomer spaceObjectu
     */
    public double getr() {
        return r;
    }

    /**
     * Getter
     * @return boolean clicked - zda byl dany spaceObject kliknut
     */
    public boolean isClicked(){
        return clicked;
    }

    /**
     * Setter
     * @param  posX - zmeni pozici X spaceObjectu
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Setter
     * @param  posY - zmeni pozici Y spaceObjectu
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * Setter
     * @param  velX - zmeni rychlost X spaceObjectu
     */
    public void setVelX(double velX) {
        this.velX = velX;
    }

    /**
     * Setter
     * @param  velY - zmeni rychlost Y spaceObjectu
     */
    public void setVelY(double velY) {
        this.velY = velY;
    }

    /**
     * Setter
     * @param  click - zapise zda byl spaceObject kliknut nebo ne
     */
    public void setClicked(boolean click){
        this.clicked = click;
    }

    /**
     * Setter
     * @param  aX - zmeni zrychleni X spaceObjectu
     */
    public void setaX(double aX) {
        this.aX = aX;
    }

    /**
     * Setter
     * @param  aY - zmeni zrychleni Y spaceObjectu
     */
    public void setaY(double aY) {
        this.aY = aY;
    }
}
