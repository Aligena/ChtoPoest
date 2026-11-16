package com.chtohotovit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chtohotovit.R
import com.chtohotovit.data.Dish

/** Адаптер списка блюд. missing < 0 — нехватку продуктов не считаем. */
class DishAdapter(
    private val items: List<DishItem>,
    private val onClick: (Dish) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    data class DishItem(val dish: Dish, val missing: Int)

    inner class DishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmoji: TextView = view.findViewById(R.id.tv_emoji)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvMeta: TextView = view.findViewById(R.id.tv_meta)
        val tvIngredients: TextView = view.findViewById(R.id.tv_ingredients)
        val tvKcal: TextView = view.findViewById(R.id.tv_kcal)
        val tvBadge: TextView = view.findViewById(R.id.tv_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val item = items[position]
        val dish = item.dish
        val context = holder.itemView.context

        holder.tvEmoji.text = dish.emoji
        holder.tvName.text = dish.name
        holder.tvMeta.text = "${dish.category} · ${dish.timeMinutes} мин · ${dish.difficulty}"
        holder.tvIngredients.text = dish.ingredients.joinToString(", ")
        holder.tvKcal.text = "${dish.calories} ккал"

        when {
            item.missing < 0 -> holder.tvBadge.visibility = View.GONE
            item.missing == 0 -> {
                holder.tvBadge.visibility = View.VISIBLE
                holder.tvBadge.text = "Всё есть ✅"
                holder.tvBadge.setBackgroundResource(R.drawable.bg_pill_green)
                holder.tvBadge.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            else -> {
                holder.tvBadge.visibility = View.VISIBLE
                holder.tvBadge.text = "не хватает: ${item.missing}"
                holder.tvBadge.setBackgroundResource(R.drawable.bg_pill_orange)
                holder.tvBadge.setTextColor(
                    ContextCompat.getColor(context, R.color.brand_orange_dark)
                )
            }
        }

        holder.itemView.setOnClickListener { onClick(dish) }
    }

    override fun getItemCount(): Int = items.size
}
