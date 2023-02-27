package com.scrapperescapetime;

import java.util.ArrayList;

public class Scrapper {
    public ClientGM leNavigateur;

    public Scrapper(String id, String mdp, String ville) {
        this.leNavigateur = new ClientGM(id, mdp, ville);
    }

    public static void main(String[] args) {
        
        RecupererIdentifiant allan = new RecupererIdentifiant();

        Scrapper leScrapper = new Scrapper(allan.getId(), allan.getMdp(), "lemans");
        leScrapper.leNavigateur.connecter();

        ArrayList<String> fesse = leScrapper.leNavigateur.getNomSalle("https://gmet.escapetime-world.fr/repartition.php");
        System.out.println(fesse);


    }
}
