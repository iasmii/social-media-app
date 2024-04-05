package com.example.lab7bun.domain.validators;

import com.example.lab7bun.domain.Utilizator;

import java.util.Objects;

public class UtilizatorValidator implements Validator<Utilizator> {
    public UtilizatorValidator() {
    }
    public static boolean nuContine(String inputString, String sequence) {
        for (char c : inputString.toCharArray()) {
            if (sequence.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if (entity == null){
            throw new ValidationException("Utilizatorul nu poate fi null!");
        }
        if(Objects.equals(entity.getPrenume(), "")){
            throw new ValidationException("Prenumele nu poate fi null!");
        }
        if(Objects.equals(entity.getNume(), "")){
            throw new ValidationException("Numele nu poate fi null!");
        }
        if(entity.getId()<0 || entity.getId()==null){
            throw new ValidationException("Id-ul nu poate fi negativ sau null!");
        }
        if (Objects.equals(entity.getParola(), "")) {
            throw new ValidationException("Lungimea minima e 5!");
        }
        /*
        if(nuContine(entity.getParola(), "!@#$%^&*(){}[];:|?/><.,")){
            throw new ValidationException("Parola trebuie sa contina cel putin un caracter special!");
        }
        if(nuContine(entity.getParola(), "0123456789")){
            throw new ValidationException("Parola trebuie sa contina cel putin o cifra!");
        }
        if(nuContine(entity.getParola(), "aqwsedrftgyhujikolpmnbvcxzAQSWDEFRGTHYJUKILOPMNBVCXZ")){
            throw new ValidationException("Parola trebuie sa contina cel putin o litera!");
        }
        */
    }
}
