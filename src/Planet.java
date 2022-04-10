public class Planet implements ISpaceObject{

    private final String name;
    private double posX;
    private double posY;
    private double velX;
    private double velY;
    private double weight;
    private double r;
    private boolean clicked;
    private double aX;
    private double aY;

    public Planet(String name, double posX, double posY, double velX, double velY, double weight) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.weight = weight;
        this.clicked = false;
        this.r = (Math.cbrt(6*weight/Math.PI))/2;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPosX() {
        return posX;
    }

    @Override
    public double getPosY() {
        return posY;
    }

    @Override
    public double getVelX() {
        return velX;
    }

    @Override
    public double getVelY() {
        return velY;
    }

    @Override
    public double getaX() {
        return aX;
    }

    @Override
    public double getaY() {
        return aY;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public double getr() {
        return r;
    }

    @Override
    public boolean isClicked(){
        return clicked;
    }

    @Override
    public void setPosX(double posX) {
        this.posX = posX;
    }

    @Override
    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public void setVelX(double velX) {
        this.velX = velX;
    }

    @Override
    public void setVelY(double velY) {
        this.velY = velY;
    }

    @Override
    public void setClicked(boolean click){
        this.clicked = click;
    }

    @Override
    public void setaX(double aX) {
        this.aX = aX;
    }

    @Override
    public void setaY(double aY) {
        this.aY = aY;
    }

}
