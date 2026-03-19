package com.example.journeytowealth.ui.myhabit.mind

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytowealth.databinding.ItemHabitBinding
import com.example.journeytowealth.ui.myhabit.HabitItem

class TheMindHabitAdapter(
    private val items: MutableList<HabitItem>
) : RecyclerView.Adapter<TheMindHabitAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvTitle.text = item.title

        // 체크 상태 설정
        holder.binding.checkBox.setOnCheckedChangeListener(null)
        holder.binding.checkBox.isChecked = item.isChecked

        // 체크 변경
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
        }
    }
}