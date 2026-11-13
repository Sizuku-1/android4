package cn.edu.sicnu.cs.txp.android4

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.txp.android4.model.CardMatchingGame
import java.io.Serializable

class GameFragment : Fragment(R.layout.fragment_game) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val matchSize = requireArguments().getInt(MATCH_SIZE)
        val file = "game-$matchSize.ser"
        val history = ObjectStore.read(context, file, GameHistory::class.java) ?: GameHistory(matchSize)
        val landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val game = if (landscape) history.landscape else history.portrait
        val score = view.findViewById<TextView>(R.id.tv_score)
        val best = view.findViewById<TextView>(R.id.tv_best_score)

        fun showScore() {
            score.text = getString(R.string.game_score, game.score)
            best.text = getString(R.string.game_best_score, history.bestScore)
        }

        fun saveGame() {
            history.bestScore = maxOf(history.bestScore, game.score)
            showScore()
            if (!ObjectStore.write(context, file, history)) {
                Toast.makeText(context, R.string.save_failed, Toast.LENGTH_SHORT).show()
            }
        }

        val board = view.findViewById<RecyclerView>(R.id.game_view)
        val cards = CardAdapter(game) { index ->
            game.chooseCardAtIndex(index)
            board.adapter?.notifyItemRangeChanged(0, game.cardCount())
            saveGame()
            if (game.isGameFinished()) {
                Toast.makeText(context, R.string.game_finished, Toast.LENGTH_LONG).show()
            }
        }
        board.layoutManager = GridLayoutManager(context, if (landscape) 10 else 4)
        board.adapter = cards
        board.itemAnimator = null

        view.findViewById<TextView>(R.id.tv_rules).text = getString(R.string.match_rules, matchSize)
        view.findViewById<Button>(R.id.btn_reset).setOnClickListener {
            game.reset()
            board.scrollToPosition(0)
            cards.notifyItemRangeChanged(0, game.cardCount())
            saveGame()
        }
        showScore()
    }

    private class GameHistory(matchSize: Int) : Serializable {
        val portrait = CardMatchingGame(24, matchSize)
        val landscape = CardMatchingGame(if (matchSize == 2) 24 else 30, matchSize)
        var bestScore = 0

        private companion object {
            const val serialVersionUID = 1L
        }
    }

    companion object {
        private const val MATCH_SIZE = "match_size"

        fun newInstance(matchSize: Int) = GameFragment().apply {
            arguments = Bundle().apply { putInt(MATCH_SIZE, matchSize) }
        }
    }
}
