/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.service;

import com.mycompany.pocvideojuego.model.Venta;
import com.mycompany.pocvideojuego.data.MongoDBConnection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiciosVenta {

    private static final String COLLECTION_NAME = "ventas";

    // ✅ Document ← Venta (_id = ObjectId generado por MongoDB)
    private static Document toDocument(Venta v) {
        return new Document()
                .append("codigoVenta", v.getCodigoVenta())
                .append("fecha", v.getFecha() != null ? v.getFecha() : new Date())
                .append("cliente", v.getCliente())
                .append("totalVenta", v.getTotalVenta());
                // ⚠️ NO incluimos _id → MongoDB lo genera al insertar
    }

    // ✅ Venta ← Document
    private static Venta toVenta(Document doc) {
        if (doc == null) return null;
        Venta v = new Venta();
        v.setId(doc.getObjectId("_id")); // ✅ ObjectId
        v.setCodigoVenta(doc.getInteger("codigoVenta"));
        v.setFecha(doc.getDate("fecha"));
        v.setCliente(doc.getString("cliente"));
        v.setTotalVenta(doc.getDouble("totalVenta"));
        return v;
    }

    // ✅ CRUD
    public static boolean agregarVenta(Venta v) {
        if (v == null || v.getCodigoVenta() <= 0) return false;
        if (buscarVenta(v.getCodigoVenta()) != null) return false; // duplicado

        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);
            coll.insertOne(toDocument(v)); // ✅ _id generado automáticamente
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);
            for (Document doc : coll.find()) {
                lista.add(toVenta(doc));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public static Venta buscarVenta(int codigoVenta) {
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);
            Document doc = coll.find(Filters.eq("codigoVenta", codigoVenta)).first();
            return toVenta(doc);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean actualizarVenta(Venta v) {
        if (v == null || v.getCodigoVenta() <= 0) return false;
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);

            Bson filter = Filters.eq("codigoVenta", v.getCodigoVenta());
            Bson update = Updates.combine(
                    Updates.set("fecha", v.getFecha()),
                    Updates.set("cliente", v.getCliente()),
                    Updates.set("totalVenta", v.getTotalVenta())
            );

            UpdateResult res = coll.updateOne(filter, update);
            return res.getModifiedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarVenta(int codigoVenta) {
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);
            DeleteResult res = coll.deleteOne(Filters.eq("codigoVenta", codigoVenta));
            return res.getDeletedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ✅ Métodos de negocio
    public static double totalPorCliente(String cliente) {
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);

            Document result = coll.aggregate(List.of(
                    new Document("$match", new Document("cliente", cliente)),
                    new Document("$group", new Document("_id", null)
                            .append("total", new Document("$sum", "$totalVenta")))
            )).first();

            return (result != null) ? result.getDouble("total") : 0.0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public static List<Venta> ventasRecientes(int dias) {
        List<Venta> lista = new ArrayList<>();
        try {
            Date limite = new Date(System.currentTimeMillis() - (dias * 24L * 60 * 60 * 1000));
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(COLLECTION_NAME);

            for (Document doc : coll.find(Filters.gte("fecha", limite))) {
                lista.add(toVenta(doc));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}