/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.service;

import com.mycompany.pocvideojuego.model.Videojuego;
import com.mycompany.pocvideojuego.data.MongoDBConnection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mycompany.pocvideojuego.model.Publisher;
import com.mycompany.pocvideojuego.model.Venta;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ServiciosVideojuego {

    private static final String COLLECTION_NAME = "videojuegos";

    // Colección tipada como Videojuego (gracias al PojoCodecProvider)
    private static MongoCollection<Videojuego> getCollection() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        return db.getCollection(COLLECTION_NAME, Videojuego.class);
    }

    // CRUD: agregar videojuego
    public static boolean agregarVideojuego(Videojuego vj) {
         if (vj == null || vj.getCodigoSerie() <= 0) return false;
    if (buscarVideojuego(vj.getCodigoSerie()) != null) return false;

    try {
        // Paso 1: Buscar Publisher por codigoPublisher y obtener su ObjectId
        Publisher pub = ServiciosPublisher.buscarPublisher(vj.getCodigoPublisher());
        if (pub == null || pub.getId() == null) {
            System.err.println("Publisher con código " + vj.getCodigoPublisher() + " no existe o no tiene _id");
            return false;
        }
        vj.setPublisherId(pub.getId()); // Asignar ObjectId

        // ?Paso 2: Si tiene codigoVenta > 0, buscar Venta
        if (vj.getCodigoVenta() > 0) {
            Venta vta = ServiciosVenta.buscarVenta(vj.getCodigoVenta());
            if (vta == null || vta.getId() == null) {
                System.err.println("Venta con código " + vj.getCodigoVenta() + " no existe o no tiene _id");
                return false;
            }
            vj.setVentaId(vta.getId()); // Asignar ObjectId
        } else {
            vj.setVentaId(null);
        }

        // Paso 3: Guardar (codigoPublisher y codigoVenta se ignoran gracias a @BsonIgnore)
        getCollection().insertOne(vj);
        return true;

    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
    }

    // Listar todos
    public static List<Videojuego> listarVideojuegos() {
        List<Videojuego> lista = new ArrayList<>();
        try {
            for (Videojuego vj : getCollection().find()) {
                lista.add(vj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Buscar por codigoSerie (campo normal)
    public static Videojuego buscarVideojuego(int codigoSerie) {
        try {
            return getCollection().find(Filters.eq("codigoSerie", codigoSerie)).first();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Actualizar
    public static boolean actualizarVideojuego(Videojuego vj) {
        if (vj == null || vj.getCodigoSerie() <= 0) return false;

        try {
            // ?Resolver referencias
            Publisher pub = ServiciosPublisher.buscarPublisher(vj.getCodigoPublisher());
            if (pub == null) return false;
            vj.setPublisherId(pub.getId());

            if (vj.getCodigoVenta() > 0) {
                Venta vta = ServiciosVenta.buscarVenta(vj.getCodigoVenta());
                if (vta == null) return false;
                vj.setVentaId(vta.getId());
            } else {
                vj.setVentaId(null);
            }

            Bson filter = Filters.eq("codigoSerie", vj.getCodigoSerie());
            Bson update = Updates.combine(
                    Updates.set("nombre", vj.getNombre()),
                    Updates.set("estado", vj.getEstado()),
                    Updates.set("entregado", vj.isEntregado()),
                    Updates.set("precio", vj.getPrecio()),
                    Updates.set("publisherId", vj.getPublisherId()),
                    Updates.set("ventaId", vj.getVentaId())
            );

            UpdateResult res = getCollection().updateOne(filter, update);
            return res.getModifiedCount() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //Eliminar
    public static boolean eliminarVideojuego(int codigoSerie) {
        try {
            DeleteResult res = getCollection().deleteOne(Filters.eq("codigoSerie", codigoSerie));
            return res.getDeletedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ✅ Métodos de negocio
    public static List<Videojuego> listarPorPublisher(ObjectId publisherId) {
        List<Videojuego> lista = new ArrayList<>();
        try {
            for (Videojuego vj : getCollection().find(Filters.eq("publisherId", publisherId))) {
                lista.add(vj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public static List<Videojuego> listarPorPublisher(int codigoPublisher) {
        Publisher pub = ServiciosPublisher.buscarPublisher(codigoPublisher);
        if (pub == null) return new ArrayList<>();
        return listarPorPublisher(pub.getId());
    }

    public static double totalRecaudado(String estado) {
        try {
            MongoCollection<Document> coll = MongoDBConnection.getInstance().getDatabase()
                    .getCollection(COLLECTION_NAME, Document.class);

            Bson filter = (estado != null) ? Filters.eq("estado", estado) : new Document();

            Document result = coll.aggregate(List.of(
                    new Document("$match", filter),
                    new Document("$group", new Document("_id", null)
                            .append("total", new Document("$sum", "$precio")))
            )).first();

            return (result != null) ? result.getDouble("total") : 0.0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
}