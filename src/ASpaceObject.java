public abstract class ASpaceObject {
    private String name;
    private double posX;
    private double posY;
    private double velX;
    private double velY;
    private double weight;
    private double r;
    private boolean clicked;
    private double aX;
    private double aY;

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

    public String getName() {
        return name;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public double getaX() {
        return aX;
    }

    public double getaY() {
        return aY;
    }

    public double getWeight() {
        return weight;
    }

    public double getr() {
        return r;
    }

    public boolean isClicked(){
        return clicked;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void setClicked(boolean click){
        this.clicked = click;
    }

    public void setaX(double aX) {
        this.aX = aX;
    }

    public void setaY(double aY) {
        this.aY = aY;
    }
}
