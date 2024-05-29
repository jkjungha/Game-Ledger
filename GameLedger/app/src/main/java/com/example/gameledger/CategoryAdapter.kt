package com.example.gameledger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val categories: List<String>,
    private val selectedCategory: String,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = categories.indexOf(selectedCategory)

    interface OnItemClickListener {
        fun onItemClick(category: String)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val previousSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previousSelectedPosition)
                    notifyItemChanged(selectedPosition)
                    itemClickListener.onItemClick(categories[position])
                }
            }
        }

        fun bind(category: String, isSelected: Boolean) {
            categoryTextView.text = category
            itemView.setBackgroundColor(
                if (isSelected) {
                    itemView.context.getColor(R.color.selected_category_background) // 선택된 아이템 배경색
                } else {
                    itemView.context.getColor(R.color.default_category_background) // 기본 아이템 배경색
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}