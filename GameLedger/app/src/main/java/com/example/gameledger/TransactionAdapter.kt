package com.example.gameledger

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(val transactionList: ArrayList<Transactions>) : RecyclerView.Adapter<TransactionAdapter.CustomViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)

    }

    override fun onBindViewHolder(holder: TransactionAdapter.CustomViewHolder, position: Int) {
        holder.category.text = transactionList.get(position).category
        holder.date.text = transactionList.get(position).date
        holder.title.text = transactionList.get(position).title
        holder.value.text = transactionList.get(position).value

        if (transactionList.get(position).type == 1) {
            holder.type.setImageResource(R.drawable.treasure_opened)
            holder.value.setTextColor(Color.parseColor("#2196F3"))
        }
        else if (transactionList.get(position).type == 0) {
            // 지출 카테고리별 몬스터 그래픽 변경 예정
            holder.type.setImageResource(R.drawable.treasure_closed)
            holder.value.setTextColor(Color.parseColor("#F44336"))
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type = itemView.findViewById<ImageView>(R.id.iv_plus)   // 수입, 지출 type
        val category = itemView.findViewById<TextView>(R.id.tv_category)    // 카테고리
        val date = itemView.findViewById<TextView>(R.id.tv_date)    // 날짜
        val title = itemView.findViewById<TextView>(R.id.tv_transaction)    // 거래 내역
        val value = itemView.findViewById<TextView>(R.id.tv_value)  // 금액
    }

}

