/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.model;

import org.bson.types.ObjectId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

public class Videojuego {
    // _id real en MongoDB (ObjectId)
    private ObjectId id;

    // Campo de negocio (no es _id)
    private int codigoSerie;
    private String nombre;
    private String estado;
    private boolean entregado;
    private double precio;

    // Referencias a otros documentos
    private ObjectId publisherId;
    private ObjectId ventaId;
    
    // Estos campos los usas en la GUI y para resolver referencias...
    @BsonIgnore  
    private int codigoPublisher;

    @BsonIgnore 
    private int codigoVenta;
    
    
    // Constructor vacío
    
    
    public Videojuego() {}

    // Constructor con parámetros (opcional)
    public Videojuego(int codigoSerie, String nombre, String estado, boolean entregado, double precio) {
        this.codigoSerie = codigoSerie;
        this.nombre = nombre;
        this.estado = estado;
        this.entregado = entregado;
        this.precio = precio;
    }

    // Getters & Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public int getCodigoSerie() { return codigoSerie; }
    public void setCodigoSerie(int codigoSerie) { this.codigoSerie = codigoSerie; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isEntregado() { return entregado; }
    public void setEntregado(boolean entregado) { this.entregado = entregado; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public ObjectId getPublisherId() { return publisherId; }
    public void setPublisherId(ObjectId publisherId) { this.publisherId = publisherId; }

    public ObjectId getVentaId() { return ventaId; }
    public void setVentaId(ObjectId ventaId) { this.ventaId = ventaId; }
    
    public int getCodigoPublisher() { return codigoPublisher; }
    public void setCodigoPublisher(int codigoPublisher) { this.codigoPublisher = codigoPublisher; }

    public int getCodigoVenta() { return codigoVenta; }
    public void setCodigoVenta(int codigoVenta) { this.codigoVenta = codigoVenta; }
    
     @Override
    public String toString() {
        return "Videojuego{" +
                "_id=" + id +
                ", codigoSerie=" + codigoSerie +
                ", nombre='" + nombre + '\'' +
                ", estado='" + estado + '\'' +
                ", entregado=" + entregado +
                ", precio=" + precio +
                ", publisherId=" + publisherId +
                ", ventaId=" + ventaId +
                '}';
    }
}