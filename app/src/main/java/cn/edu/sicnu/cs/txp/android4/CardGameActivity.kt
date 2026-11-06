package cn.edu.sicnu.cs.txp.android4

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.txp.android4.model.CardMatchingGame

class CardGameActivity : AppCompatActivity() {

    private companion object {
        const val PORTRAIT_COUNT = 24
        const val LANDSCAPE_COUNT = 30
        const val PORTRAIT_GAME = "portrait_game"
        const val LANDSCAPE_GAME = "landscape_game"
        const val SESSION_BEST = "session_best"
    }

    private var portraitGame: CardMatchingGame? = null
    private var landscapeGame: CardMatchingGame? = null
    private lateinit var game: CardMatchingGame
    private lateinit var adapter: CardAdapter
    private lateinit var scoreView: TextView
    private var bestScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_game)

        portraitGame = savedInstanceState?.getSerializable(PORTRAIT_GAME, CardMatchingGame::class.java)
        landscapeGame = savedInstanceState?.getSerializable(LANDSCAPE_GAME, CardMatchingGame::class.java)
        bestScore = savedInstanceState?.getInt(SESSION_BEST) ?: 0

        val landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        game = if (landscape) {
            landscapeGame ?: CardMatchingGame(LANDSCAPE_COUNT).also { landscapeGame = it }
        } else {
            portraitGame ?: CardMatchingGame(PORTRAIT_COUNT).also { portraitGame = it }
        }
        adapter = CardAdapter(game, ::onCardTapped)
        findViewById<RecyclerView>(R.id.game_view).apply {
            layoutManager = GridLayoutManager(this@CardGameActivity, if (landscape) 10 else 4)
            adapter = this@CardGameActivity.adapter
            itemAnimator = null
        }

        scoreView = findViewById(R.id.tv_score)
        updateScore()
        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            game.reset()
            adapter.notifyItemRangeChanged(0, game.cardCount())
            updateScore()
        }
        findViewById<Button>(R.id.btn_exit).setOnClickListener { exitGame() }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = exitGame()
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PORTRAIT_GAME, portraitGame)
        outState.putSerializable(LANDSCAPE_GAME, landscapeGame)
        outState.putInt(SESSION_BEST, bestScore)
    }

    private fun onCardTapped(index: Int) {
        game.chooseCardAtIndex(index)
        adapter.notifyItemRangeChanged(0, game.cardCount())
        updateScore()
        if (game.isGameFinished()) {
            Toast.makeText(this, R.string.game_finished, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateScore() {
        bestScore = maxOf(bestScore, game.score)
        scoreView.text = game.score.toString()
    }

    private fun exitGame() {
        val data = Intent().putExtra(MainActivity.KEY_HIGH_SCORE, bestScore)
        setResult(RESULT_OK, data)
        finish()
    }
}
