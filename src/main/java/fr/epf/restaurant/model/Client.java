package fr.epf.restaurant.model;

public class Client {
    private long id;
    private String nom;
    private String prenom;
    private String email;
    private long telephone;

    //constructeurs
    public Client() {
    }
    public Client(long id, String nom, String prenom, String email, long telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    //getteur et setteur
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public long getTelephone() {
        return telephone;
    }
    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }
}
