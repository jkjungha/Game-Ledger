
package com.example.gameledger

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

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

        holder.category.text = transactionList.get(position).category
        holder.date.text = transactionList.get(position).date
        holder.title.text = transactionList.get(position).title
        holder.value.text = transactionList.get(position).value

        if (transactionList.get(position).type) {
            holder.type.setAnimation(R.raw.chest_1)
            holder.value.setTextColor(Color.parseColor("#2196F3"))
        }
        else if (!transactionList.get(position).type) {
            // 지출 카테고리별 몬스터 그래픽 변경 예정
            if(transactionList.get(position).category == "식비") {
                holder.type.setAnimation(R.raw.monster_1)
                holder.value.setTextColor(Color.parseColor("#F44336"))
            }
            else if(transactionList.get(position).category == "교통") {
                holder.type.setAnimation(R.raw.monster_5)
                holder.value.setTextColor(Color.parseColor("#F44336"))
            }
            else if(transactionList.get(position).category == "문화") {
                holder.type.setAnimation(R.raw.monster_3)
                holder.value.setTextColor(Color.parseColor("#F44336"))
            }
            else if(transactionList.get(position).category == "생활") {
                holder.type.setAnimation(R.raw.monster_4)
                holder.value.setTextColor(Color.parseColor("#F44336"))
            }
            else {
                holder.type.setAnimation(R.raw.monster_2)
                holder.value.setTextColor(Color.parseColor("#F44336"))
            }

        }

        holder.type.repeatCount = LottieDrawable.INFINITE
        holder.type.playAnimation()

        holder.edit_button.setOnClickListener {
            editClickListener.onEditClick(transaction)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type = itemView.findViewById<LottieAnimationView>(R.id.lav_plus)   // 수입, 지출 type
        val category: TextView = itemView.findViewById<TextView>(R.id.tv_category)    // 카테고리
        val date: TextView = itemView.findViewById<TextView>(R.id.tv_date)    // 날짜
        val title: TextView = itemView.findViewById<TextView>(R.id.tv_transaction)    // 거래 내역
        val value: TextView = itemView.findViewById<TextView>(R.id.tv_value)  // 금액

        val edit_button: ImageButton = itemView.findViewById(R.id.edit_button)
    }

}
