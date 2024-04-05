package com.example.lab7bun.domain;

import java.util.Objects;

public class Utilizator extends Entity<Long>{
    private String nume;
    private String prenume;
    private String parola;

    public Utilizator(String nume,String prenume){
        this.setId(selfid());
        this.nume=nume;
        this.prenume=prenume;
    }

    public Utilizator(String nume,String prenume,String parola){
        this.setId(selfid());
        this.nume=nume;
        this.prenume=prenume;
        this.parola=parola;
    }

    private long selfid(){
        return idCounter++;
    }

    public void setId(long id){
        this.id=id;
    }

    private static long idCounter = 315;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(nume, that.nume) && Objects.equals(prenume, that.prenume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, prenume);
    }

    @Override
    public String toString(){
        return "Utilizator: "+this.getId()+", nume: "+this.getNume()+", prenume: "+this.getPrenume();
    }
}