package cloud.englert.experimental.ui.swipe

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

import cloud.englert.experimental.R
import cloud.englert.experimental.custom.CardAdapter
import cloud.englert.experimental.databinding.FragmentSwipeBinding

class SwipeFragment : Fragment() {
    private var _binding: FragmentSwipeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    private val topics = mutableListOf("Java", "Kotlin", "Android", "AI", "Cloud")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSwipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        adapter = CardAdapter(topics)
        recyclerView.layoutManager = object :
            LinearLayoutManager(requireContext(), VERTICAL,
                false) {
                // Disable vertical scrolling
                override fun canScrollVertically(): Boolean = false
        }
        recyclerView.adapter = adapter

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val itemTouchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
    ) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            when (direction) {
                ItemTouchHelper.LEFT -> Toast.makeText(requireContext(),
                    "Disliked: ${topics[position]}", Toast.LENGTH_SHORT).show()
                ItemTouchHelper.RIGHT -> Toast.makeText(requireContext(),
                    "Liked: ${topics[position]}", Toast.LENGTH_SHORT).show()
                ItemTouchHelper.UP -> Toast.makeText(requireContext(),
                    "Saved: ${topics[position]}", Toast.LENGTH_SHORT).show()
                ItemTouchHelper.DOWN -> Toast.makeText(requireContext(),
                    "Skipped: ${topics[position]}", Toast.LENGTH_SHORT).show()
            }
            // Remove the card after swiping
            adapter.removeItem(position)
        }

        /**
         * Adjust how far the card needs to move before it's considered swiped
         */
        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return 0.3f
        }

        /**
         * Increases the speed sensitivity by 50%
         */
        override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
            return defaultValue * 1.5f
        }

        /**
         * Get the animation duration for a swipe in milliseconds.
         */
        override fun getAnimationDuration(recyclerView: RecyclerView, animationType: Int,
                                          animateDx: Float, animateDy: Float): Long {
            return 300
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val cardHolder = viewHolder as CardAdapter.CardViewHolder
            val threshold = recyclerView.width * 0.3f

            // Set default visibility
            cardHolder.actionIcon.visibility = View.GONE
            cardHolder.actionIcon.alpha = 0f

            // Fade out effect
            val swipeProgress = max(
                min(1f, abs(dX) / recyclerView.width),
                min(1f, abs(dY) / recyclerView.height))
            val alpha = 1.2f - swipeProgress
            itemView.alpha = alpha

            // Slight rotation effect when swiping left/right
            itemView.rotation = dX / 50

            if (dY < 0) {
                // Slight zoom-in when swiping up
                itemView.scaleX = 1.2f + abs(dY) / recyclerView.height
                itemView.scaleY = 1.2f + abs(dY) / recyclerView.height
            } else {
                // Shrink when swiping down
                itemView.scaleX = 1.2f - abs(dY) / recyclerView.height
                itemView.scaleY = 1.2f - abs(dY) / recyclerView.height
            }

            // Determine swipe direction
            when {
                dX > threshold -> { // Swiped RIGHT (LIKE)
                    cardHolder.actionIcon.setImageResource(R.drawable.ic_like) // Use your drawable
                    cardHolder.actionIcon.visibility = View.VISIBLE
                    cardHolder.actionIcon.alpha = alpha
                }
                dX < -threshold -> { // Swiped LEFT (DISLIKE)
                    cardHolder.actionIcon.setImageResource(R.drawable.ic_dislike)
                    cardHolder.actionIcon.visibility = View.VISIBLE
                    cardHolder.actionIcon.alpha = alpha
                }
                dY < -threshold -> { // Swiped UP (SAVE)
                    cardHolder.actionIcon.setImageResource(R.drawable.ic_save)
                    cardHolder.actionIcon.visibility = View.VISIBLE
                    cardHolder.actionIcon.alpha = alpha
                }
                dY > threshold -> { // Swiped DOWN (SKIP)
                    cardHolder.actionIcon.setImageResource(R.drawable.ic_skip)
                    cardHolder.actionIcon.visibility = View.VISIBLE
                    cardHolder.actionIcon.alpha = alpha
                }
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    })
}