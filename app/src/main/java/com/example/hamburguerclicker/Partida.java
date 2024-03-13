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
        panaderia = 0;
        carniceria = 0;
        queseria = 0;
        lechuga = 0;
        huerto = 0;
        bacon = 0;
        dinero = 0.0;
    }



    public int getPanaderia() {
        return panaderia;
    }

    public void setPanaderia(int panaderia) {
        Partida.panaderia = panaderia;
    }

    public int getCarniceria() {
        return carniceria;
    }

    public void setCarniceria(int carniceria) {
        Partida.carniceria = carniceria;
    }

    public int getQueseria() {
        return queseria;
    }

    public void setQueseria(int queseria) {
        Partida.queseria = queseria;
    }

    public int getLechuga() {
        return lechuga;
    }

    public void setLechuga(int lechuga) {
        Partida.lechuga = lechuga;
    }

    public int getHuerto() {
        return huerto;
    }

    public void setHuerto(int huerto) {
        Partida.huerto = huerto;
    }

    public int getBacon() {
        return bacon;
    }

    public void setBacon(int bacon) {
        Partida.bacon = bacon;
    }

    public double getDinero() {
        return dinero;
    }

    public void setDinero(double dinero) {
        Partida.dinero = dinero;
    }
}
