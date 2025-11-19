/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.service;

import com.mycompany.pocvideojuego.model.Genero;
import com.mycompany.pocvideojuego.data.MongoDBConnection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mycompany.pocvideojuego.model.Publisher;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ServiciosGenero {

    private static final String PUBLISHERS_COLLECTION = "publishers";

    // Convierte Genero → Document (para embeber)
    private static Document toDocument(Genero g) {
        return new Document("_id", g.getIdGenero())
                .append("nombre", g.getNombre())
                .append("calificacion", g.getCalificacion());
    }

    // Convierte Document → Genero
    private static Genero toGenero(Document doc, int codigoPublisher) {
        Genero g = new Genero();
        g.setIdGenero(doc.getInteger("_id"));
        g.setNombre(doc.getString("nombre"));
        g.setCalificacion(doc.getInteger("calificacion"));
        g.setCodigoPublisher(codigoPublisher);
        return g;
    }

    // Agregar género a un publisher existente
    public static boolean agregarGenero(int codigoPublisher, Genero genero) {
        if (genero == null || codigoPublisher <= 0) return false;
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(PUBLISHERS_COLLECTION);

            Bson filter = Filters.eq("codigoPublisher", codigoPublisher); 
            Bson update = Updates.push("generos", toDocument(genero));

            UpdateResult res = coll.updateOne(filter, update);
            return res.getModifiedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Listar géneros de un publisher
    public static List<Genero> listarGenerosPorPublisher(int codigoPublisher) {
        List<Genero> lista = new ArrayList<>();
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(PUBLISHERS_COLLECTION);
            Document doc = coll.find(Filters.eq("codigoPublisher", codigoPublisher)).first();
            if (doc != null && doc.containsKey("generos")) {
                @SuppressWarnings("unchecked")
                List<Document> genList = (List<Document>) doc.get("generos");
                for (Document gDoc : genList) {
                    lista.add(toGenero(gDoc, codigoPublisher));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Buscar género por nombre dentro de un publisher
    public static Genero buscarGeneroPorNombre(int codigoPublisher, String nombre) {
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(PUBLISHERS_COLLECTION);
            Document doc = coll.find(Filters.and(
                    Filters.eq("codigoPublisher", codigoPublisher),
                    Filters.eq("generos.nombre", nombre)
            )).first();

            if (doc != null && doc.containsKey("generos")) {
                @SuppressWarnings("unchecked")
                List<Document> generos = (List<Document>) doc.get("generos");
                for (Document g : generos) {
                    if (nombre.equals(g.getString("nombre"))) {
                        return toGenero(g, codigoPublisher);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Actualizar género
    public static boolean actualizarGenero(int codigoPublisher, String nombreAnterior, Genero nuevo) {
        if (nuevo == null || nombreAnterior == null) return false;
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(PUBLISHERS_COLLECTION);

            Bson filter = Filters.and(
                    Filters.eq("codigoPublisher", codigoPublisher),
                    Filters.eq("generos.nombre", nombreAnterior)
            );

            Bson update = Updates.set("generos.$", toDocument(nuevo));

            UpdateResult res = coll.updateOne(filter, update);
            return res.getModifiedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Eliminar género
    public static boolean eliminarGenero(int codigoPublisher, String nombre) {
        try {
            MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
            MongoCollection<Document> coll = db.getCollection(PUBLISHERS_COLLECTION);

            Bson filter = Filters.eq("codigoPublisher", codigoPublisher);
            Bson update = Updates.pull("generos", new Document("nombre", nombre));

            UpdateResult res = coll.updateOne(filter, update);
            return res.getModifiedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Buscar género por ID (busca en todos los publishers)
    public static Genero buscarGeneroPorId(int idGenero) {
        List<Publisher> publishers = ServiciosPublisher.listarPublisher();
        for (Publisher pub : publishers) {
            List<Genero> generos = listarGenerosPorPublisher(pub.getCodigoPublisher());
            for (Genero g : generos) {
                if (g.getIdGenero() == idGenero) {
                    return g;
                }
            }
        }
        return null;
    }
    
   public static List<Genero> listarGenero() {
    List<Genero> lista = new ArrayList<>();
    try {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        MongoCollection<Document> coll = db.getCollection("publishers");

        for (Document doc : coll.find()) {
            if (doc.containsKey("generos")) {
                @SuppressWarnings("unchecked")
                List<Document> generosList = (List<Document>) doc.get("generos");
                // Usa "codigoPublisher", NO "_id"
                int codigoPublisher = doc.getInteger("codigoPublisher");

                for (Document gDoc : generosList) {
                    Genero g = new Genero();
                    g.setIdGenero(gDoc.getInteger("_id")); 
                    g.setNombre(gDoc.getString("nombre"));
                    g.setCalificacion(gDoc.getInteger("calificacion"));
                    g.setCodigoPublisher(codigoPublisher);
                    lista.add(g);
                }
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return lista;
}
}