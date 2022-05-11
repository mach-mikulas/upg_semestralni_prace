import java.util.TreeMap;

/**
 * Abstrakni trida udavajisi strukturu spaceobjectu
 * @author Mikulas Mach
 */
public abstract class ASpaceObject {

    /** Jmeno spaceObjectu*/
    private final String name;
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
    /** TreeMap obsahujici data grafu (cas a rychlost)*/
    private TreeMap<Long, Double> graphData = new TreeMap<>();
    /** Treemap obsahujici jesnotlive body trajektorie a cas jejich pridani*/
    public TreeMap<Long, double[]> trajectory = new TreeMap<>();

    /**
     * Konstruktor priradi hodnoty atributum
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

    public TreeMap<Long, Double> getGraphData(){
        return graphData;
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

    /**
     * Setter
     * @param r - zmeni polomer spaceObjectu
     */
    public void setr(double r) {
        this.r = r;
    }

    /**
     * Vypocita aktualni polomer spaceObjectu
     * @param weight - aktualni hmostnost spaceObjectu
     */
    public void calculateR(double weight){
        this.r = (Math.cbrt(6*weight/Math.PI))/2;
    }

    /**
     * Do TreeMapy prida jako klic aktualni cas a jako hodnotu rychlost spaceObjectu v km/h
     * Pokud je rozdil mezi prvnim a poslednim zaznamem vice  jak 30s, smaze se prvni
     * @param velocityX rychlost X spaceObjectu
     * @param velocityY rychlost Y spaceObjectu
     */
    public void addGraphData(double velocityX, double velocityY){
        double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY) * 3.6;

        long realTime = (System.currentTimeMillis() - Calculations.simulationTimeStart);

        long stopTime = 30000;

        graphData.put(realTime, velocity);

        if(graphData.lastKey() - graphData.firstKey() > stopTime){
            graphData.pollFirstEntry();
        }

    }

    /**
     * Do TreeMapy prida jako klic aktualni realny cas a jako hodnotu pole obsahujici x a y
     * Pokud rozdil mezi prvni a poslednim zaznamem v TreeMape je vetsi jak 1s, smaze se prvni
     * @param x souradnice X jednoho bodu trajektorie
     * @param y souradnice Y jednoho bodu trajektorie
     */
    public void addTrajectory(double x, double y){
        long timeToRemove = 1000;

        long realTime = System.currentTimeMillis() - Calculations.simulationTimeStart;
        double[] input = {x,y};

        trajectory.put(realTime, input);

        if(graphData.lastKey() - graphData.firstKey() > timeToRemove){
            trajectory.pollFirstEntry();
        }
    }
}


