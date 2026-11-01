package cn.edu.sicnu.cs.txp.android4.model

class CardMatchingGame(count: Int) {
    var score = 0
        private set

    private val cards: MutableList<Card>

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    init {
        val deck = Deck()
        cards = mutableListOf()
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    fun cardCount(): Int = cards.size

    fun cardAtIndex(index: Int): Card {
        return cards.get(index)
    }

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (!card.isMatched) {
            if (card.isChosen) {
                card.isChosen = false
            } else {
                for (otherCard in cards) {
                    if (otherCard.isChosen && !otherCard.isMatched) {
                        val matchScore = card.match(arrayOf(otherCard))
                        if (matchScore > 0) {
                            score += matchScore * MATCH_BONUS
                            otherCard.isMatched = true
                            card.isMatched = true
                        } else {
                            score -= MISMATCH_PENALTY
                            otherCard.isChosen = false
                        }
                        break
                    }
                }
                score -= COST_TO_CHOOSE
                card.isChosen = true
            }
        }
    }

    fun isGameFinished(): Boolean {
        return cards.all { it.isMatched }
    }
}
