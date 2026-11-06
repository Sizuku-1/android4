package cn.edu.sicnu.cs.txp.android4

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.txp.android4.model.Card
import cn.edu.sicnu.cs.txp.android4.model.CardMatchingGame

class CardAdapter(
    private val game: CardMatchingGame,
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    class CardHolder(val face: TextView) : RecyclerView.ViewHolder(face)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val face = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_card, parent, false) as TextView
        return CardHolder(face).apply {
            face.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) onCardClick(position)
            }
        }
    }

    override fun getItemCount(): Int = game.cardCount()

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val face = holder.face
        val context = face.context
        val card = game.cardAtIndex(position)
        val background = when {
            card.isMatched -> R.drawable.game_card_matched
            card.isChosen -> R.drawable.game_card_front
            else -> R.drawable.game_card_back
        }
        val color = when {
            !card.isChosen -> R.color.white
            card.suitValue in Card.validSuits.take(2) -> R.color.card_text_red
            else -> R.color.card_text_black
        }
        val description = when {
            card.isMatched -> context.getString(R.string.card_matched_desc, position + 1, card.toString())
            card.isChosen -> context.getString(R.string.card_face_desc, position + 1, card.toString())
            else -> context.getString(R.string.card_position_desc, position + 1)
        }
        face.setBackgroundResource(background)
        face.setTextColor(ContextCompat.getColor(context, color))
        face.text = if (card.isChosen) card.toString() else context.getString(R.string.card_back_symbol)
        face.contentDescription = description
        face.isEnabled = !card.isMatched
    }
}
