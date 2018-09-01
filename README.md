# KotlinRecommendationService

Hello today I decided to make a recommendation service.

For this I used [Apache Mahout](https://mahout.apache.org/)

### Apache Mahout

**Identify the similarity of users**

```kotlin
val model = FileDataModel(File("your file"))
val similarity = PearsonCorrelationSimilarity(model)
similarity.setPreferenceInferrer(AveragingPreferenceInferrer(model))
val neighborhood = ThresholdUserNeighborhood(0.1, similarity, model)
val recommender = GenericUserBasedRecommender(model, neighborhood, similarity)
val recommendations = recommender.recommend(1, 3)
recommendations.forEach { println(it) }
```

**Item Recommendation**

```kotlin
val itemSimilarity = PearsonCorrelationSimilarity(model)
val recom = GenericItemBasedRecommender(model, itemSimilarity)
val recoms = recom.recommend(1, 3)
recoms.forEach { println(it) }
for(i in model.userIDs) {
    val recoms = recom.recommend(i, 3)
    recoms.forEach { println(it) }
}
```

### Learning this all things

**1. Creating a Data Model**

```kotlin
val model = FileDataModel(File("your file"))
```

**2. Creating Similarity**

*User-based Similarity*

```kotlin
val similarity = PearsonCorrelationSimilarity(model);
val similarity = LogLikelihoodSimilarity(model);
val similarity = TanimotoCoefficientSimilarity(model); 
val similarity = EuclideanDistanceSimilarity(model); 
val similarity = GenericUserSimilarity(model); 
val similarity = SpearmanCorrelationSimilarity(model);
```

**!!The same things is with Item-based Similarity!!**

**3. Neighborhood Strategy**

```kotlin
val neighborhood = NearestNUserNeighborhood (0.1, similarity, model)
//or
val neighborhood = ThresholdUserNeighborhood(0.1, similarity, model)
```

**4. Building Recommender System**

```kotlin
val recommender = GenericUserBasedRecommender(model, neighborhood, similarity)
//or for item
val recommender = GenericItemBasedRecommender(model, similarity)
```

**5. Output**

```kotlin
val recommendations = recommender.recommend(1, 3)
recommendations.forEach { println(it) }
```

**Evaluation**

```kotlin
val evaluator = new RMSRecommenderEvaluator()
val score = evaluator.evaluate(recommender, null, model, 0.7, 1.0)
println("RMSE: " + score)
```

*In details*

```kotlin
val statsEvaluator = new GenericRecommenderIRStatsEvaluator()
val stats = statsEvaluator.evaluate(recommender, null, model, null, 10, 4, 0.7); // evaluate precision recall at 10
println("Precision: " + stats.getPrecision())
println("Recall: " + stats.getRecall())
println("F1 Score: " + stats.getF1Measure())
```

And that's all, thanks for your attention :)


















