package yagmurdan.sonra.toprakkokusu.Model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.List;

public class CampingArea {

    private String GonderiResmi;
    private String Name;
    private String Tanitim;
    private String Location;
    private Boolean BoolUcret = false;
    private Boolean BoolTesis = false;
    private Boolean BoolTopluTasima = false;
    private Boolean BoolOtopark = false;
    private Boolean BoolCesme = false;
    private Boolean BoolTuvalet = false;
    private Boolean BoolYabaniHayvan = false;
    private Boolean BoolAtesYakilmaz = false;
    private Boolean BoolSinyalVar = false;
    private Boolean BoolOdun = false;
    private double Latitute;
    private double Longitute;
    private float Rating;


    public CampingArea() {
    }

    public CampingArea(String gonderiResmi, String name, String tanitim, String location) {
        GonderiResmi = gonderiResmi;
        Name = name;
        Location = location;
        this.Tanitim = tanitim;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public Boolean getBoolUcret() {
        return BoolUcret;
    }

    public void setBoolUcret(Boolean boolUcret) {
        this.BoolUcret = boolUcret;
    }

    public Boolean getBoolTesis() {
        return BoolTesis;
    }

    public void setBoolTesis(Boolean boolTesis) {
        this.BoolTesis = boolTesis;
    }

    public Boolean getBoolTopluTasima() {
        return BoolTopluTasima;
    }

    public void setBoolTopluTasima(Boolean boolTopluTasima) {
        this.BoolTopluTasima = boolTopluTasima;
    }

    public Boolean getBoolOtopark() {
        return BoolOtopark;
    }

    public void setBoolOtopark(Boolean boolOtopark) {
        this.BoolOtopark = boolOtopark;
    }

    public Boolean getBoolCesme() {
        return BoolCesme;
    }

    public void setBoolCesme(Boolean boolCesme) {
        this.BoolCesme = boolCesme;
    }

    public Boolean getBoolTuvalet() {
        return BoolTuvalet;
    }

    public void setBoolTuvalet(Boolean boolTuvalet) {
        this.BoolTuvalet = boolTuvalet;
    }

    public Boolean getBoolYabaniHayvan() {
        return BoolYabaniHayvan;
    }

    public void setBoolYabaniHayvan(Boolean boolYabaniHayvan) { this.BoolYabaniHayvan = boolYabaniHayvan; }

    public Boolean getBoolAtesYakilmaz() {
        return BoolAtesYakilmaz;
    }

    public void setBoolAtesYakilmaz(Boolean boolAtesYakilmaz) { this.BoolAtesYakilmaz = boolAtesYakilmaz; }

    public Boolean getBoolSinyalVar() {
        return BoolSinyalVar;
    }

    public void setBoolSinyalVar(Boolean boolSinyalVar) {
        this.BoolSinyalVar = boolSinyalVar;
    }

    public Boolean getBoolOdun() {
        return BoolOdun;
    }

    public void setBoolOdun(Boolean boolOdun) {
        this.BoolOdun = boolOdun;
    }


    public String getGonderiResmi() {
        return GonderiResmi;
    }

    public void setGonderiResmi(String gonderiResmi) {
        GonderiResmi = gonderiResmi;
    }

    public String getTanitim() { return Tanitim; }

    public void setTanitim(String tanitim) { Tanitim = tanitim; }


    public double getLatitute() {
        return Latitute;
    }

    public void setLatitute(double latitute) {
        Latitute = latitute;
    }

    public double getLongitute() {
        return Longitute;
    }

    public void setLongitute(double longitute) {
        Longitute = longitute;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }
}
