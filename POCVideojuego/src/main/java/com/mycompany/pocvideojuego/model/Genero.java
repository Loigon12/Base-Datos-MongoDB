/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.model;

public class Genero {
    private int idGenero;
    private String nombre;
    private int calificacion;

    // Relación lógica (solo para GUI/mostrar FK)
    private int codigoPublisher; // FK hacia Publisher (no usada en DB, solo para mostrar)

    // Constructor vacío
    public Genero() {}

    public Genero(int idGenero, String nombre, int calificacion, int codigoPublisher) {
        this.idGenero = idGenero;
        this.nombre = nombre;
        this.calificacion = calificacion;
        this.codigoPublisher = codigoPublisher;
    }

    // Getters & Setters
    public int getIdGenero() { return idGenero; }
    public void setIdGenero(int idGenero) { this.idGenero = idGenero; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    public int getCodigoPublisher() { return codigoPublisher; }
    public void setCodigoPublisher(int codigoPublisher) { this.codigoPublisher = codigoPublisher; }

    @Override
    public String toString() {
        return "Genero{" +
                "idGenero=" + idGenero +
                ", nombre='" + nombre + '\'' +
                ", calificacion=" + calificacion +
                ", codigoPublisher=" + codigoPublisher +
                '}';
    }
}