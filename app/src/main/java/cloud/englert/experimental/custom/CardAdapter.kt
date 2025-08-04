package cloud.englert.experimental.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources

import androidx.recyclerview.widget.RecyclerView

import cloud.englert.experimental.R

class CardAdapter(private val topics: MutableList<String>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    var toggle: Boolean = false

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicText: TextView = itemView.findViewById(R.id.text_view)
        val actionIcon: ImageView = itemView.findViewById(R.id.action_icon)
        val background: View = itemView.findViewById(R.id.linear_layout_background)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.topicText.text = topics[position]
        if (toggle) holder.background.background =
            AppCompatResources.getDrawable(holder.itemView.context, R.color.secondary)
        else holder.background.background =
            AppCompatResources.getDrawable(holder.itemView.context, R.color.primaryDay)
        toggle = !toggle

        // Reset transformations to ensure a fresh state
        holder.itemView.rotation = 0f
        holder.itemView.alpha = 1f
        holder.itemView.scaleX = 1f
        holder.itemView.scaleY = 1f

        // Hide the action icon
        holder.actionIcon.visibility = View.GONE
        holder.actionIcon.alpha = 0f
    }

    override fun getItemCount(): Int = topics.size

    fun removeItem(position: Int) {
        topics.removeAt(position)
        notifyItemRemoved(position)
    }
}