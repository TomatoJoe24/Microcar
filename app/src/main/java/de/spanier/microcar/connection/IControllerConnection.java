package de.spanier.microcar.connection;

import de.spanier.microcar.util.LightColor;

public interface IControllerConnection {
    public boolean connect(String btName);
    public boolean disconnect();
    public int getTurnPosition();
    public void setTurnPosition(int position);
    public void start();
    public void stop();
    public void setFlashLight(LightColor color);
    public LightColor getFlashLight();
    public float getTemperature();
    public float getHumidity();

}
