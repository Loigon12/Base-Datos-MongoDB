package com.mycompany.pocvideojuego.service;

import com.mycompany.pocvideojuego.model.Genero;
import com.mycompany.pocvideojuego.model.Publisher;
import com.mycompany.pocvideojuego.data.MongoDBConnection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiciosPublisher {

    private static final String COLLECTION_NAME = "publishers";

    // Obtén colección tipada como Publisher (gracias al PojoCodecProvider)
    private static MongoCollection<Publisher> getCollection() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        return db.getCollection(COLLECTION_NAME, Publisher.class);
    }

    // Agregar (MongoDB generará _id: ObjectId automáticamente si id == null)
    public static boolean agregarPublisher(Publisher pb) {
        if (pb == null || pb.getCodigoPublisher() <= 0) return false;
        if (buscarPublisher(pb.getCodigoPublisher()) != null) return false;

        try {
            getCollection().insertOne(pb);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Publisher> listarPublisher() {
        List<Publisher> lista = new ArrayList<>();
        try {
            for (Publisher p : getCollection().find()) {
                lista.add(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Buscar por codigoPublisher (campo normal), NO por _id
    public static Publisher buscarPublisher(int codigoPublisher) {
        try {
            return getCollection().find(Filters.eq("codigoPublisher", codigoPublisher)).first();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean actualizarPublisher(Publisher actualizado) {
        if (actualizado == null || actualizado.getCodigoPublisher() <= 0) return false;
        try {
            Bson filter = Filters.eq("codigoPublisher", actualizado.getCodigoPublisher());
            Bson update = Updates.combine(
                    Updates.set("nombre", actualizado.getNombre()),
                    Updates.set("region", actualizado.getRegion()),
                    Updates.set("precioSuscripcion", actualizado.getPrecioSuscripcion()),
                    Updates.set("enServicio", actualizado.getEnServicio()),
                    Updates.set("generos", actualizado.getGeneros())
            );
            UpdateResult res = getCollection().updateOne(filter, update);
            return res.getModifiedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarPublisher(int codigoPublisher) {
        try {
            Bson update = Updates.set("enServicio", "no");
            UpdateResult res = getCollection().updateOne(
                    Filters.eq("codigoPublisher", codigoPublisher), update);
            return res.getModifiedCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Métodos de negocio: ahora usamos colección como Document para agregados
    private static MongoCollection<Document> getCollectionAsDocument() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        return db.getCollection(COLLECTION_NAME, Document.class);
    }

    public static boolean masDel50PorcientoSuperaSuscripcion(double limite) {
        List<Publisher> lista = listarPublisher();
        if (lista.isEmpty()) return false;
        long count = lista.stream().filter(p -> p.getPrecioSuscripcion() > limite).count();
        return (count * 100.0 / lista.size()) >= 50.0;
    }

    public static double sumatoriaPreciosSuscripcion(boolean soloActivos) {
        try {
            MongoCollection<Document> coll = getCollectionAsDocument();
            Bson filter = soloActivos ? Filters.eq("enServicio", "si") : new Document();

            List<Document> pipeline = List.of(
                    new Document("$match", filter),
                    new Document("$group", new Document("_id", null)
                            .append("total", new Document("$sum", "$precioSuscripcion")))
            );

            Document result = coll.aggregate(pipeline).first();
            return (result != null) ? result.getDouble("total") : 0.0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
}