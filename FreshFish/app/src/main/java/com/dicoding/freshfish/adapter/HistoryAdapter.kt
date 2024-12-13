package com.dicoding.freshfish.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.freshfish.databinding.ListHistoryBinding
import com.dicoding.freshfish.response.DataItem

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val historyList = mutableListOf<DataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historyList.size

    fun setHistoryList(history: List<DataItem>) {
        historyList.clear()
        historyList.addAll(history)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: DataItem) {
            binding.resultText.text = "Result: ${dataItem.result}"
            binding.messageText.text = dataItem.message
            binding.dateText.text = "Created At: ${dataItem.createdAt}"
        }
    }
}