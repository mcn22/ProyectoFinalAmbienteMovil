package model;

import java.io.Serializable;

public class Cita implements Serializable {

    private int idCita;
    private String UserID;
    private String establecimiento;
    private String area;
    private String fecha;
    private String hora;

    public Cita() {
    }

    public Cita(int idCita, String UserID, String establecimiento, String area, String fecha, String hora) {
        this.idCita = idCita;
        this.UserID= UserID;
        this.establecimiento = establecimiento;
        this.area = area;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getUserId() {
        return UserID;
    }

    public void setUserID(String UserId) {
        this.UserID = UserId;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}//fin de la clase
