/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pocvideojuego.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBConnection {
    private static volatile MongoDBConnection instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "POCVideojuegos";

    private MongoDBConnection() {
        CodecRegistry registry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(URI))
            .codecRegistry(registry)
            .build();

        this.mongoClient = MongoClients.create(settings);
        this.database = mongoClient.getDatabase(DB_NAME);
        System.out.println("✅ Conectado a MongoDB 4.x con PojoCodecProvider");
    }

    public static MongoDBConnection getInstance() {
        if (instance == null) {
            synchronized (MongoDBConnection.class) {
                if (instance == null) {
                    instance = new MongoDBConnection();
                }
            }
        }
        return instance;
    }

    public MongoDatabase getDatabase() { return database; }
    public void close() { if (mongoClient != null) mongoClient.close(); }
}