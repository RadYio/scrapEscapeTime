package com.scrapperescapetime;

public enum Salle {
    OMEGA(1, "Omega"), ABYSS(2, "Abyss"), MANOIR(3, "Manoir"), PIRATES(4, "Pirates"), ARCHEON(5, "Archeon");

    private int val;
    private String name;

    Salle(int val, String nom){
        this.val = val;
    }

    public String getNom(){
        return this.name();
    }

    public int getVal(){
        return this.val;
    }
}
