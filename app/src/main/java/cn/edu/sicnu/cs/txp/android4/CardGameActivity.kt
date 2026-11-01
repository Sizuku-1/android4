package cn.edu.sicnu.cs.txp.android4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.txp.android4.model.CardMatchingGame

/**
 * 翻牌游戏控制器（Controller 层）。
 * 接受用户交互（点击牌、重置、退出），处理 Model，并同步 View。
 */
class CardGameActivity : AppCompatActivity() {

    companion object {
        const val CARD_COUNT = 16
    }

    private lateinit var gameView: CardGameView
    private lateinit var tvScore: TextView

    private var game: CardMatchingGame = CardMatchingGame(CARD_COUNT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_game)

        gameView = findViewById(R.id.game_view)
        tvScore = findViewById(R.id.tv_score)

        gameView.setGame(game)
        gameView.setOnCardClickListener { index ->
            onCardTapped(index)
        }
        updateScore()

        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            resetGame()
        }

        findViewById<Button>(R.id.btn_exit).setOnClickListener {
            exitGame()
        }
    }

    private fun onCardTapped(index: Int) {
        game.chooseCardAtIndex(index)
        gameView.invalidate()
        updateScore()

        if (game.isGameFinished()) {
            Toast.makeText(this, getString(R.string.game_finished), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateScore() {
        tvScore.text = game.score.toString()
    }

    private fun resetGame() {
        game = CardMatchingGame(CARD_COUNT)
        gameView.setGame(game)
        updateScore()
    }

    private fun exitGame() {
        val data = Intent().apply {
            putExtra(MainActivity.KEY_HIGH_SCORE, game.score)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() {
        exitGame()
    }
}
