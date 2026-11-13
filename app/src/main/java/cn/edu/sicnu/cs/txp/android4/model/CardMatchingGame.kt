package cn.edu.sicnu.cs.txp.android4.model

import java.io.Serializable

class CardMatchingGame(
    private val count: Int,
    private val matchSize: Int = 3
) : Serializable {
    private companion object {
        const val serialVersionUID = 1L
    }

    var score = 0
        private set

    private val cards = mutableListOf<Card>()

    init {
        require(matchSize in 2..3)
        require(count in matchSize..(13 * matchSize) && count % matchSize == 0)
        reset()
    }

    fun reset() {
        val deck = Deck()
        val fullDeck = generateSequence { deck.drawRandomCard() }.toList()

        // Deal one matching group of each selected rank, then shuffle the whole board.
        cards.clear()
        cards.addAll(
            fullDeck.groupBy { it.rankValue }.values.shuffled().take(count / matchSize)
                .flatMap { it.take(matchSize) }.shuffled()
        )
        score = 0
    }

    fun cardCount(): Int = cards.size

    fun cardAtIndex(index: Int): Card = cards[index]

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) return

        val open = cards.filter { it.isChosen && !it.isMatched }
        if (open.size == matchSize) {
            // Leave a failed group visible until the next card tap.
            open.forEach { it.isChosen = false }
        } else if (card.isChosen) {
            card.isChosen = false
            return
        }

        card.isChosen = true
        score -= 1
        if (open.size != matchSize - 1) return

        val matchScore = card.match(open.toTypedArray())
        if (matchScore > 0) {
            score += matchScore * 4
            card.isMatched = true
            open.forEach { it.isMatched = true }
        } else {
            score -= 2
        }
    }

    fun isGameFinished(): Boolean = cards.all { it.isMatched }
}
