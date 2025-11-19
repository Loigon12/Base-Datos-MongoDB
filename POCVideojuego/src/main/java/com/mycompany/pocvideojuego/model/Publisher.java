/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.model;

import org.bson.types.ObjectId;
import java.util.List;

public class Publisher {
    private ObjectId id; // → será _id en MongoDB
    private int codigoPublisher; // → campo normal
    private String nombre;
    private String region;
    private double precioSuscripcion;
    private String enServicio;
    private List<Genero> generos;

    public Publisher() {} // ✅ obligatorio

    public Publisher(int codigoPublisher, String nombre, String region, double precioSuscripcion, String enServicio) {
        this.codigoPublisher = codigoPublisher;
        this.nombre = nombre;
        this.region = region;
        this.precioSuscripcion = precioSuscripcion;
        this.enServicio = enServicio;
    }

    // ✅ Getters y setters para TODOS los campos
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public int getCodigoPublisher() { return codigoPublisher; }
    public void setCodigoPublisher(int codigoPublisher) { this.codigoPublisher = codigoPublisher; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public double getPrecioSuscripcion() { return precioSuscripcion; }
    public void setPrecioSuscripcion(double precioSuscripcion) { this.precioSuscripcion = precioSuscripcion; }

    public String getEnServicio() { return enServicio; }
    public void setEnServicio(String enServicio) { this.enServicio = enServicio; }

    public List<Genero> getGeneros() { return generos; }
    public void setGeneros(List<Genero> generos) { this.generos = generos; }
}