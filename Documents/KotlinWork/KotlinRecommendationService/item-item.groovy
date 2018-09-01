// Use item-item CF to score items
bind ItemScorer to ItemItemScorer
// let's use personalized mean rating as the baseline/fallback predictor.
// 2-step process:
// First, use the user-item bias to compute item scores
bind (BaselineScorer, ItemScorer) to BiasItemScorer
// Second, use user-item biases
bind BiasModel to LiveUserItemBiasModel
// and normalize ratings by baseline prior to computing similarities
bind (UserVectorNormalizer) to BiasUserVectorNormalizer
// little speed tweek
within (UserVectorNormalizer) {
    bind Biasmodel to UserItemBiasModel
}