package com.example.gameledger

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private val transactionList: ArrayList<Transactions>,
    private val editClickListener: OnEditClickListener
) : RecyclerView.Adapter<TransactionAdapter.CustomViewHolder>()
{

    interface OnEditClickListener {
        fun onEditClick(transaction: Transactions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)

    }

    override fun onBindViewHolder(holder: TransactionAdapter.CustomViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.category.text = transaction.category
        holder.date.text = transaction.date
        holder.title.text = transaction.title
        holder.value.text = transaction.value.toString()

        if (transactionList.get(position).type) {
            holder.type.setImageResource(R.drawable.treasure_opened)
            holder.value.setTextColor(Color.parseColor("#2196F3"))
        }
        else if (!transactionList.get(position).type) {
            // 지출 카테고리별 몬스터 그래픽 변경 예정
            holder.type.setImageResource(R.drawable.treasure_closed)
            holder.value.setTextColor(Color.parseColor("#F44336"))
        }

        holder.edit_button.setOnClickListener {
            editClickListener.onEditClick(transaction)
        }

    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type: ImageView = itemView.findViewById<ImageView>(R.id.iv_plus)   // 수입, 지출 type
        val category: TextView = itemView.findViewById<TextView>(R.id.tv_category)    // 카테고리
        val date: TextView = itemView.findViewById<TextView>(R.id.tv_date)    // 날짜
        val title: TextView = itemView.findViewById<TextView>(R.id.tv_transaction)    // 거래 내역
        val value: TextView = itemView.findViewById<TextView>(R.id.tv_value)  // 금액

        val edit_button: ImageButton = itemView.findViewById(R.id.edit_button)
    }

}

