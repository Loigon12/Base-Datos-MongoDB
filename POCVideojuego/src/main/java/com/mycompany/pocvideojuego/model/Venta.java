/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.Date;

public class Venta {
    @BsonId
    private ObjectId id;

    private int codigoVenta;
    private Date fecha;
    private String cliente;
    private double totalVenta;
    private int codigoVideojuego;

    public Venta() {}

    public Venta(int codigoVenta, Date fecha, String cliente, double totalVenta, int codigoVideojuego) {
        this.codigoVenta = codigoVenta;
        this.fecha = fecha;
        this.cliente = cliente;
        this.totalVenta = totalVenta;
        this.codigoVideojuego = codigoVideojuego;
    }

    // Getters & Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public int getCodigoVenta() { return codigoVenta; }
    public void setCodigoVenta(int codigoVenta) { this.codigoVenta = codigoVenta; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }

    public int getCodigoVideojuego() { return codigoVideojuego; }
    public void setCodigoVideojuego(int codigoVideojuego) { this.codigoVideojuego = codigoVideojuego; }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", codigoVenta=" + codigoVenta +
                ", fecha=" + fecha +
                ", cliente='" + cliente + '\'' +
                ", totalVenta=" + totalVenta +
                ", codigoVideojuego=" + codigoVideojuego +
                '}';
    }
}