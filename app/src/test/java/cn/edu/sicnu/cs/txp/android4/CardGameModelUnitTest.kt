package cn.edu.sicnu.cs.txp.android4

import cn.edu.sicnu.cs.txp.android4.model.Card
import cn.edu.sicnu.cs.txp.android4.model.CardMatchingGame
import cn.edu.sicnu.cs.txp.android4.model.Deck
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 翻牌游戏 Model 层单元测试。
 */
class CardGameModelUnitTest {

    @Test
    fun card_toString_returnsSuitAndRank() {
        val card = Card("♥", "A")
        assertEquals("♥A", card.toString())
    }

    @Test
    fun card_match_sameRank_returnsFour() {
        val card1 = Card("♥", "A")
        val card2 = Card("♠", "A")
        assertEquals(4, card1.match(arrayOf(card2)))
    }

    @Test
    fun card_match_sameSuit_returnsOne() {
        val card1 = Card("♥", "A")
        val card2 = Card("♥", "K")
        assertEquals(1, card1.match(arrayOf(card2)))
    }

    @Test
    fun card_match_noRelation_returnsZero() {
        val card1 = Card("♥", "A")
        val card2 = Card("♠", "K")
        assertEquals(0, card1.match(arrayOf(card2)))
    }

    @Test
    fun deck_drawsAll52Cards() {
        val deck = Deck()
        val drawn = mutableSetOf<String>()
        for (i in 1..52) {
            val card = deck.drawRandomCard()
            assertNotNull(card)
            drawn.add(card.toString())
        }
        // 52 张牌不重复
        assertEquals(52, drawn.size)
        // 抽完后为空
        assertNull(deck.drawRandomCard())
    }

    @Test
    fun game_initialScoreIsZero() {
        val game = CardMatchingGame(24)
        assertEquals(0, game.score)
    }

    @Test
    fun game_choosingCardCostsOnePoint() {
        val game = CardMatchingGame(24)
        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun game_choosingSameCardAgainUnchooses() {
        val game = CardMatchingGame(24)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(0)
        assertFalse(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun sameSuitTrioMatches() {
        val first = Card("♥", "A")
        assertEquals(1, first.match(arrayOf(Card("♥", "3"), Card("♥", "K"))))
        assertEquals(0, first.match(arrayOf(Card("♥", "3"), Card("♠", "K"))))
    }

    @Test
    fun matchRequiresThreeCardsAndLocksTheTrio() {
        val game = CardMatchingGame(24)
        val sameRank = (0 until game.cardCount()).filter {
            game.cardAtIndex(it).rankValue == game.cardAtIndex(0).rankValue
        }
        assertEquals(3, sameRank.size)
        sameRank.take(2).forEach(game::chooseCardAtIndex)
        assertEquals(-2, game.score)
        assertFalse(game.cardAtIndex(0).isMatched)
        game.chooseCardAtIndex(sameRank.last())
        assertEquals(13, game.score)
        assertTrue(sameRank.all { game.cardAtIndex(it).isMatched })
        game.chooseCardAtIndex(0)
        assertEquals(13, game.score)
    }

    @Test
    fun mismatchedTrioClosesWhenNextCardIsTapped() {
        val game = CardMatchingGame(24)
        val first = game.cardAtIndex(0)
        val second = (1 until game.cardCount()).first {
            val candidate = game.cardAtIndex(it)
            candidate.rankValue != first.rankValue && candidate.suitValue != first.suitValue
        }
        val third = (1 until game.cardCount()).first { it != second }
        val next = (1 until game.cardCount()).first { it != second && it != third }
        val trio = listOf(0, second, third)
        trio.forEach(game::chooseCardAtIndex)
        assertEquals(-5, game.score)
        assertTrue(trio.all { game.cardAtIndex(it).isChosen })
        game.chooseCardAtIndex(next)
        assertEquals(-6, game.score)
        assertTrue(trio.none { game.cardAtIndex(it).isChosen })
        assertTrue(game.cardAtIndex(next).isChosen)
    }

    @Test
    fun resetClearsScoreAndRestoresFaceDownBoard() {
        val game = CardMatchingGame(30)
        game.chooseCardAtIndex(0)
        game.reset()
        assertEquals(0, game.score)
        assertEquals(30, game.cardCount())
        assertTrue((0 until game.cardCount()).none {
            game.cardAtIndex(it).isChosen || game.cardAtIndex(it).isMatched
        })
    }
}
