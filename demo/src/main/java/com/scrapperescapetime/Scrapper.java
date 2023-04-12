package com.scrapperescapetime;




import com.google.gson.*;

public class Scrapper {
    public ClientGM leNavigateur;

    public Scrapper(String id, String mdp, String ville) {
        this.leNavigateur = new ClientGM(id, mdp, ville);
    }

    public static void main(String[] args) {
        
        RecupererIdentifiant allan = new RecupererIdentifiant();

        Scrapper leScrapper = new Scrapper(allan.getId(), allan.getMdp(), "lemans");
        leScrapper.leNavigateur.connecter();

        JsonObject salles = new JsonObject();
        for(Salle s: Salle.values()){
            JsonObject sTemp = leScrapper.leNavigateur.getClientParSalle(s, 4);
            salles.add(s.getNom(), sTemp);
        }
        String salleFinal = new GsonBuilder().setPrettyPrinting().create().toJson(salles);
        System.out.println(salleFinal);


    }
}
