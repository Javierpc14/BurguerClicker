package com.example.hamburguerclicker.modelo

import com.example.hamburguerclicker.R

class Partida {

    var pesoTotal: Double = 0.0

    var pesoPorClick :Double= 1.0

    var tiendas = arrayListOf(
        Tienda("Panaderia",150.0,7.5,R.drawable.panaderia),
         Tienda("Carniceria",1000.0,50.0,R.drawable.carniceria),
         Tienda("Queseria",130000.0,6500.0,R.drawable.queseria),
         Tienda("Lechuga",2300000.0,115000.0,R.drawable.lechuga),
         Tienda("Huerto",400000000.0,20000000.0,R.drawable.huerto),
         Tienda("Bacon",1000000000.0,50000000.0,R.drawable.bacon)
    )

    var logros = arrayListOf(
        Logro(R.drawable.logro1, "Alcanza los 100 mg", "Pesas los mismo que un gusano") ,
        Logro(R.drawable.logro2, "Compra 20 panaderias","Amasador profesional"),
        Logro(R.drawable.logro3, "Alcanza los 10 g", "Pesas lo mismo que un lapiz"),
        Logro(R.drawable.logro4, "Compra 20 carnicerias", "Partecarnes"),
        Logro(R.drawable.logro5, "Alcanza los 700 g", "Pesas lo mismo que una almohada"),
        Logro(R.drawable.logro6, "Compra 20 queserias", "Quesero maestro"),
        Logro(R.drawable.logro7, "Alcanza los 20 Kg", "Pesas lo mismo que un lince ibérico"),
        Logro(R.drawable.logro8, "Compra 20 lechugas", "Manos verdes"),
        Logro(R.drawable.logro9, "Alcanza los 800 Kg", "Pesas lo mismo que un twingo"),
        Logro(R.drawable.logro10, "Compra 20 huertos", "Agricultor experto"),
        Logro(R.drawable.logro11, "Alcanza las 140 T", "Pesas lo mismo que una ballena azul"),
        Logro(R.drawable.logro12, "Compra 20 beicones", "Maestro porcino"),
    )

    var mejoras = arrayListOf(
        //Mejoras de los click en la hamburguesa
        Mejora("Dedo índice reforzado",75.0,2.0, R.drawable.mejorapulsar1, "Otorga el doble del valor a los clicks",-1),
        Mejora("Calmante muscular",5000.0,20.0, R.drawable.mejorapulsar2, "Cuadruplica el valor a los clicks",-1,0),
        Mejora("Nudillos de gorila",650000.0,50.0, R.drawable.mejorapulsar3, "Octuplica el valor a los clicks",-1,1),
        Mejora("Dedos inalambricos",11500000.0,100.0, R.drawable.mejorapulsar4, "Multiplica 16 veces el  valor de los clicks",-1,2),
        Mejora("Manos de acero",500000000.0,200.0, R.drawable.mejorapulsar5, "Multiplica 32 veces el  valor de los clicks",-1,3),

        //Mejoras de las panaderias
        Mejora("Rodillo",40000.0,2.0, R.drawable.mejorapanaderia1, "Doble de eficacia en las panaderias",0),
        Mejora("Harina organica",80000.0,2.0, R.drawable.mejorapanaderia2, "Doble de eficacia en las panaderias",0,5),
        Mejora("Amasadora industrial",160000.0,2.0, R.drawable.mejorapanaderia3, "Doble de eficacia en las panaderias",0,6),
        Mejora("Pan de mantequilla",320000.0,2.0, R.drawable.mejorapanaderia4, "Doble de eficacia en las panaderias",0,7),
        Mejora("Hornos eléctricos",640000.0,2.0, R.drawable.mejorapanaderia5, "Doble de eficacia en las panaderias",0,8),

        //Mejoras de las carnicerias
        Mejora("Hacha de despacho",600000.0,2.0, R.drawable.mejoracarniceria1, "Doble de eficacia en las carnicerias",1),
        Mejora("El afilador",600000.0,2.0, R.drawable.mejoracarniceria2, "Doble de eficacia en las carnicerias",1,10),
        Mejora("Picadora electrica",600000.0,2.0, R.drawable.mejoracarniceria3, "Doble de eficacia en las carnicerias",1,11),
        Mejora("Carne tudanca cantabra",600000.0,2.0, R.drawable.mejoracarniceria4, "Doble de eficacia en las carnicerias",1,12),
        Mejora("Carne wagyu",600000.0,2.0, R.drawable.mejoracarniceria5, "Doble de eficacia en las carnicerias",1,13),

        //Mejoras de las queserias
        Mejora("Leche de vacas silvestres",430000.0,2.0, R.drawable.mejoraqueso1, "Doble de eficacia en las queserias",2),
        Mejora("Coagulantes avanzados",830000.0,2.0, R.drawable.mejoraqueso2, "Doble de eficacia en las queserias",2,15),
        Mejora("Cowboy canadiense",1630000.0,2.0, R.drawable.mejoraqueso3, "Doble de eficacia en las queserias",2,16),
        Mejora("Frances experto",3230000.0,2.0, R.drawable.mejoraqueso4, "Doble de eficacia en las queserias",2,17),
        Mejora("Manantial de queso",6430000.0,2.0, R.drawable.mejoraqueso5, "Doble de eficacia en las queserias",2,18),

        //Mejoras de las lechuguerias
        Mejora("Pesticidas anti veganos",4300000.0,2.0, R.drawable.mejoralechuga1, "Doble de eficacia en las lechugas",3),
        Mejora("Nutrientes naturales",8300000.0,2.0, R.drawable.mejoralechuga2, "Doble de eficacia en las lechugas",3,20),
        Mejora("Cazaratones",16300000.0,2.0, R.drawable.mejoralechuga3, "Doble de eficacia en las lechugas",3,21),
        Mejora("Fesrtilizante atomico",32300000.0,2.0, R.drawable.mejoralechuga4, "Doble de eficacia en las lechugas",3,22),
        Mejora("Lechaga dorada",64300000.0,2.0, R.drawable.mejoralechuga5, "Doble de eficacia en las lechugas",3,23),

        //Mejoras de los huertos
        Mejora("Abono",450000000.0,2.0, R.drawable.mejorahuerto1, "Doble de eficacia en los huertos",4),
        Mejora("Perro guardian",800000000.0,2.0, R.drawable.mejorahuerto2, "Doble de eficacia en los huertos",4,25),
        Mejora("Vecino cacerola",1600000000.0,2.0, R.drawable.mejorahuerto3, "Doble de eficacia en los huertos",4,26),
        Mejora("Tomaco",3200000000.0,2.0, R.drawable.mejorahuerto4, "Doble de eficacia en los huertos",4,27),
        Mejora("Flash",640000000.0,2.0, R.drawable.mejorahuerto5, "Doble de eficacia en los huertos",4,28),

        //Mejoras de las beiconerias
        Mejora("Engordadores artificiales",4000000000.0,2.0, R.drawable.mejorabacon1, "Doble de eficacia en las beiconerias",5),
        Mejora("Ahumadora de alta velocidad",8000000000.0,2.0, R.drawable.mejorabacon2, "Doble de eficacia en las beiconerias",5,30),
        Mejora("Cerdos pata negra",1600000000.0,2.0, R.drawable.mejorabacon3, "Doble de eficacia en las beiconerias",5,31),
        Mejora("T-rex porcino",3200000000.0,2.0, R.drawable.mejorabacon4, "Doble de eficacia en las beiconerias",5,32),
        Mejora("Laguna de bacon",6400000000.0,2.0, R.drawable.mejorabacon5, "Doble de eficacia en las beiconerias",5,33),
    )

}
