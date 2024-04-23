package tn.esprit.bankify.services;

import tn.esprit.bankify.entities.User;


public interface IService <T>{
    public void Ajouter(T t );

    public void Modifier(T t);

    void Modifier(User u);

    public void Supprimer(T t);


    void Supprimer(User u);
}
