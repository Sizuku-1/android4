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
        val game = CardMatchingGame(16)
        assertEquals(0, game.score)
    }

    @Test
    fun game_choosingCardCostsOnePoint() {
        val game = CardMatchingGame(16)
        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun game_choosingSameCardAgainUnchooses() {
        val game = CardMatchingGame(16)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(0)
        assertFalse(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun game_matchingCardsGiveBonus() {
        val game = CardMatchingGame(16)
        // 寻找两张 rank 相同的牌
        var first = -1
        var second = -1
        outer@ for (i in 0 until game.cardCount()) {
            for (j in i + 1 until game.cardCount()) {
                val a = game.cardAtIndex(i)
                val b = game.cardAtIndex(j)
                if (a.rankValue == b.rankValue) {
                    first = i
                    second = j
                    break@outer
                }
            }
        }
        // 16 张牌中一定存在同 rank 的牌
        assertTrue(first >= 0 && second >= 0)

        game.chooseCardAtIndex(first)
        game.chooseCardAtIndex(second)
        // 两张牌都 matched
        assertTrue(game.cardAtIndex(first).isMatched)
        assertTrue(game.cardAtIndex(second).isMatched)
        // 得分 = -1(第一次) + (4*4 - 1)(第二次) = 14
        assertEquals(14, game.score)
    }
}
