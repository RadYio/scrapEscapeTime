package com.scrapperescapetime;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.net.URL;

public class Scrapper {
    private ClientGM leNavigateur;

    public Scrapper(String id, String mdp, String ville) {
        this.leNavigateur = new ClientGM(id, mdp, ville);
    }

    public static void main(String[] args) {
        

        Scrapper leScrapper = new Scrapper("lo", "lo", "lemans");
        leScrapper.leNavigateur.connecter();

        String fesse = leScrapper.leNavigateur.getNomSalle("https://gmet.escapetime-world.fr/repartition.php");
        System.out.println(fesse);

    }
}
