package com.scrapperescapetime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class RecupererIdentifiant {
    private String id;
    private String mdp;

    public RecupererIdentifiant(){
        try {
            ObjectMapper lecteur = new ObjectMapper();
            JsonNode racine = lecteur.readTree(new File("auth.json"));
            this.id = racine.get("username").asText();
            this.mdp = racine.get("motdepasse").asText();
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public String getId() {
        return id;
    }

    public String getMdp() {
        return mdp;
    }

    public static void main(String[] args){
        RecupererIdentifiant leRecupererIdentifiant = new RecupererIdentifiant();
        System.out.println("id: " + leRecupererIdentifiant.getId());
        System.out.println("mdp: " + leRecupererIdentifiant.getMdp());
    }
}
