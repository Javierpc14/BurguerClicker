package com.example.hamburguerclicker;

public class Partida {

    private static String partida;
    private static int panaderia;
    private static int carniceria;
    private static int queseria;
    private static int lechuga;
    private static int huerto;
    private static int bacon;
    private static double dinero;

    public Partida(String partida, int panaderia, int carniceria, int queseria, int lechuga, int huerto, int bacon, double dinero) {
        this.partida = partida;
        this.panaderia = panaderia;
        this.carniceria = carniceria;
        this.queseria = queseria;
        this.lechuga = lechuga;
        this.huerto = huerto;
        this.bacon = bacon;
        this.dinero = dinero;
    }

    public Partida() {
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
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
