public interface ISpaceObject {

    String getName();

    double getPosX();

    double getPosY();

    double getVelX();

    double getVelY();

    double getaX();

    double getaY();

    double getWeight();

    double getr();

    boolean isClicked();

    void setPosX(double posX);

    void setPosY(double posy);

    void setVelX(double velX);

    void setVelY(double velY);

    void setClicked(boolean click);

    void setaX(double velY);

    void setaY(double velY);
}
