package com.vjezba.a7minworkoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.a7minworkoutapp.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemExerciseStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItems = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExerciseStatusBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.apply {
            tvItems.text = model.id.toString()

            when {
                model.isSelected -> {
                    holder.apply {
                        tvItems.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.item_circular_thin_color_background
                        )
                    }
                }
                model.isCompleted -> {
                    holder.apply {
                        tvItems.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.item_circular_color_green_background
                        )
                        tvItems.setTextColor(Color.parseColor("white"))
                    }
                }
                else -> {
                    holder.apply {
                        tvItems.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.item_circular_color_gray_background
                        )
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}