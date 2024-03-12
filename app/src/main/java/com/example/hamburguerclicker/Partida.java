package com.example.hamburguerclicker;

public class Partida {
    private static int panaderia;
    private static int carniceria;
    private static int queseria;
    private static int lechuga;
    private static int huerto;
    private static int bacon;
    private static double dinero;



    public Partida() {
        this.panaderia = 0;
        this.carniceria = 0;
        this.queseria = 0;
        this.lechuga = 0;
        this.huerto = 0;
        this.bacon = 0;
        this.dinero = 0.0;
    }



    public int getPanaderia() {
        return panaderia;
    }

    public void setPanaderia(int panaderia) {
        this.panaderia = panaderia;
    }

    public int getCarniceria() {
        return carniceria;
    }

    public void setCarniceria(int carniceria) {
        this.carniceria = carniceria;
    }

    public int getQueseria() {
        return queseria;
    }

    public void setQueseria(int queseria) {
        this.queseria = queseria;
    }

    public int getLechuga() {
        return lechuga;
    }

    public void setLechuga(int lechuga) {
        this.lechuga = lechuga;
    }

    public int getHuerto() {
        return huerto;
    }

    public void setHuerto(int huerto) {
        this.huerto = huerto;
    }

    public int getBacon() {
        return bacon;
    }

    public void setBacon(int bacon) {
        this.bacon = bacon;
    }

    public double getDinero() {
        return dinero;
    }

    public void setDinero(double dinero) {
        this.dinero = dinero;
    }
}
