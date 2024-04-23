package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "carte")
public class Carte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String num_c;
    private Date date_exp;
    private String type_c;
    private String statut_c;

    // Constructors
    public Carte() {
    }

    public Carte(String num_c, Date date_exp, String type_c, String statut_c) {
        this.num_c = num_c;
        this.date_exp = date_exp;
        this.type_c = type_c;
        this.statut_c = statut_c;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNum_c() {
        return num_c;
    }

    public void setNum_c(String num_c) {
        this.num_c = num_c;
    }

    public Date getDate_exp() {
        return date_exp;
    }

    public void setDate_exp(Date date_exp) {
        this.date_exp = date_exp;
    }

    public String getType_c() {
        return type_c;
    }

    public void setType_c(String type_c) {
        this.type_c = type_c;
    }

    public String getStatut_c() {
        return statut_c;
    }

    public void setStatut_c(String statut_c) {
        this.statut_c = statut_c;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Carte{" +
                "id=" + id +
                ", num_c='" + num_c + '\'' +
                ", date_exp=" + date_exp +
                ", type_c='" + type_c + '\'' +
                ", statut_c='" + statut_c + '\'' +
                '}';
    }
}
