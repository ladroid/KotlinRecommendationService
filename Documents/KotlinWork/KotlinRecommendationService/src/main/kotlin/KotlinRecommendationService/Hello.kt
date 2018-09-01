package KotlinRecommendationService

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender
import org.apache.mahout.cf.taste.impl.recommender.GenericRecommendedItem
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.AveragingPreferenceInferrer
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.mapdb.DBMaker
import org.mapdb.Serializer
import java.io.File

class MuiscComposition(map: Map<String, Any?>) {
    val id: Int by map
    val item: Int by map
    val rating: Double by map
}

class Example {
    fun start() {
        var user = mutableListOf<MuiscComposition>(
                MuiscComposition(mapOf("id" to 1, "item" to 1, "rating" to 10.0)),
                MuiscComposition(mapOf("id" to 1, "item" to 2, "rating" to 1.0)),
                MuiscComposition(mapOf("id" to 1, "item" to 3, "rating" to 9.0)),
                MuiscComposition(mapOf("id" to 2, "item" to 1, "rating" to 3.0)),
                MuiscComposition(mapOf("id" to 2, "item" to 2, "rating" to 5.0)),
                MuiscComposition(mapOf("id" to 2, "item" to 3, "rating" to 1.0)),
                MuiscComposition(mapOf("id" to 2, "item" to 4, "rating" to 10.0)),
                MuiscComposition(mapOf("id" to 3, "item" to 1, "rating" to 2.0)),
                MuiscComposition(mapOf("id" to 3, "item" to 2, "rating" to 6.0)),
                MuiscComposition(mapOf("id" to 3, "item" to 3, "rating" to 2.0)),
                MuiscComposition(mapOf("id" to 3, "item" to 4, "rating" to 9.0)),
                MuiscComposition(mapOf("id" to 3, "item" to 5, "rating" to 10.0)),
                MuiscComposition(mapOf("id" to 3, "item" to 6, "rating" to 8.0)),
                MuiscComposition(mapOf("id" to 3, "item" to 7, "rating" to 9.0)),
                MuiscComposition(mapOf("id" to 4, "item" to 1, "rating" to 9.0)),
                MuiscComposition(mapOf("id" to 4, "item" to 2, "rating" to 2.0)),
                MuiscComposition(mapOf("id" to 4, "item" to 3, "rating" to 8.0)),
                MuiscComposition(mapOf("id" to 4, "item" to 4, "rating" to 3.0)),
                MuiscComposition(mapOf("id" to 4, "item" to 10, "rating" to 10.0)),
                MuiscComposition(mapOf("id" to 4, "item" to 11, "rating" to 9.0)),
                MuiscComposition(mapOf("id" to 4, "item" to 12, "rating" to 8.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 1, "rating" to 8.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 2, "rating" to 3.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 3, "rating" to 7.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 4, "rating" to 1.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 10, "rating" to 9.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 11, "rating" to 10.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 12, "rating" to 9.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 13, "rating" to 8.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 14, "rating" to 1.0)),
                MuiscComposition(mapOf("id" to 5, "item" to 15, "rating" to 1.0)),
                MuiscComposition(mapOf("id" to 6, "item" to 1, "rating" to 7.0)),
                MuiscComposition(mapOf("id" to 6, "item" to 2, "rating" to 4.0)),
                MuiscComposition(mapOf("id" to 6, "item" to 3, "rating" to 8.0)),
                MuiscComposition(mapOf("id" to 6, "item" to 4, "rating" to 1.0)),
                MuiscComposition(mapOf("id" to 6, "item" to 10, "rating" to 7.0)),
                MuiscComposition(mapOf("id" to 6, "item" to 11, "rating" to 6.0)),
                MuiscComposition(mapOf("id" to 6, "item" to 12, "rating" to 9.0)))

        val db = DBMaker.fileDB("example.csv").transactionEnable().fileMmapEnable().fileLockDisable().make()

        val map_id = db.hashMap<String, String>("example", Serializer.STRING, Serializer.STRING).createOrOpen()
        val map_item = db.hashMap<String, String>("example", Serializer.STRING, Serializer.STRING).createOrOpen()
        val map_rating = db.hashMap<String, String>("example", Serializer.STRING, Serializer.STRING).createOrOpen()

        map_id["id"] = user.map { it.id }.toString()
        map_item["item"] = user.map { it.item }.toString()
        map_rating["rating"] = user.map { it.rating }.toString()

        println("${map_id.get("id")} ${map_item.get("item")} ${map_rating.get("rating")}")
        db.commit()
        db.close()

        File("something.csv").printWriter().use { out ->
            user.forEach { out.println("${it.id},${it.item},${it.rating}") }
        }
    }

    fun read() {
        //Identify the similarity of users
        val model = FileDataModel(File("/Users/lado/Documents/KotlinWork/KotlinRecommendationService/something.csv"))
        val similarity = PearsonCorrelationSimilarity(model)
        similarity.setPreferenceInferrer(AveragingPreferenceInferrer(model))
        val neighborhood = ThresholdUserNeighborhood(0.1, similarity, model)
        val recommender = GenericUserBasedRecommender(model, neighborhood, similarity)
        val recommendations = recommender.recommend(1, 3)
        recommendations.forEach { println(it) }
    }
}

fun somth() {
    //Identify the similarity of users
    val model = FileDataModel(File("/Users/lado/Documents/KotlinWork/KotlinRecommendationService/data.csv"))
    val similarity = PearsonCorrelationSimilarity(model)
    similarity.setPreferenceInferrer(AveragingPreferenceInferrer(model))
    val neighborhood = ThresholdUserNeighborhood(0.1, similarity, model)
    val recommender = GenericUserBasedRecommender(model, neighborhood, similarity)
    val recommendations = recommender.recommend(1, 3)
    recommendations.forEach { println(it) }

    println()
    for(i in model.userIDs) {
        val itemRecommendations = recommender.recommend(i, 5)
        itemRecommendations.forEach { println("$it") }
    }

    //Item Recommendation
    println()
    val itemSimilarity = PearsonCorrelationSimilarity(model)
    val recom = GenericItemBasedRecommender(model, itemSimilarity)
    val recoms = recom.recommend(1, 3)
    recoms.forEach { println(it) }
    for(i in model.userIDs) {
        val recoms = recom.recommend(i, 3)
        recoms.forEach { println(it) }
    }
}

fun main(args: Array<String>) {
    println("Hello, World")

    Example().start()
    Example().read()
}

