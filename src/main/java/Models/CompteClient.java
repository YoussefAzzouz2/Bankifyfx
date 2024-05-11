package Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "compte_client")
public class CompteClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private String rib;
    private String mail;
    private String tel;
    private float solde;
   // private  String sexe;
    private  String type_compte;
    private  String pack_compte;
    // Constructors
    public CompteClient() {
    }

    public CompteClient(String nom, String prenom, String rib,String mail, String tel, float solde,String type_compte, String pack_compte) {
        this.nom = nom;
        this.prenom = prenom;
        this.rib = rib;
        this.mail = mail;
        this.tel = tel;
        this.solde = solde;
        this.type_compte = type_compte;
        this.pack_compte = pack_compte;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public float getSolde() {
        return solde;
    }


    public void setSolde(float solde) {
        this.solde = solde;
    }

    public String getType_compte() {
        return type_compte;
    }

    public void setType_compte(String type_compte) {
        this.type_compte = type_compte;
    }

    public String getPack_compte() {
        return pack_compte;
    }

    public void setPack_compte(String pack_compte) {
        this.pack_compte = pack_compte;
    }
    // toString method for debugging
    @Override
    public String toString() {
        return "CompteClient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", rib='" + rib + '\'' +
                ", mail='" + mail + '\'' +
                ", tel='" + tel + '\'' +
                ", solde=" + solde +
                ", type_compte" + type_compte +
                ", pack_compte" + pack_compte +
                '}';
    }
}
