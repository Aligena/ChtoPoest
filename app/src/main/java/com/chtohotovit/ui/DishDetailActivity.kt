package com.chtohotovit.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chtohotovit.R
import com.chtohotovit.data.Dishes
import com.chtohotovit.data.UserStore

/** Карточка блюда: калорийность, продукты и пошаговый способ приготовления. */
class DishDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DISH_ID = "extra_dish_id"
        const val EXTRA_INGREDIENTS = "extra_ingredients"
        const val EXTRA_SHOW_HAVE = "extra_show_have"
    }

    private val store by lazy { UserStore(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_detail)

        val dishId = intent.getIntExtra(EXTRA_DISH_ID, -1)
        val dish = Dishes.byId(dishId)
        if (dish == null) {
            finish()
            return
        }

        val have: Set<String> =
            intent.getStringArrayListExtra(EXTRA_INGREDIENTS)?.toSet() ?: emptySet()
        val showHave = intent.getBooleanExtra(EXTRA_SHOW_HAVE, false)

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        findViewById<TextView>(R.id.tv_emoji).text = dish.emoji
        findViewById<TextView>(R.id.tv_name).text = dish.name
        findViewById<TextView>(R.id.tv_description).text = dish.description
        findViewById<TextView>(R.id.tv_time).text = "⏱ ${dish.timeMinutes} мин"
        findViewById<TextView>(R.id.tv_difficulty).text = "🎚 ${dish.difficulty}"
        findViewById<TextView>(R.id.tv_category).text = dish.category
        findViewById<TextView>(R.id.tv_kcal).text = "${dish.calories} ккал / 100 г"
        findViewById<TextView>(R.id.tv_portion).text =
            "${dish.portionGrams} г · ${dish.portionCalories} ккал"

        val tvHint = findViewById<TextView>(R.id.tv_products_hint)
        if (showHave) {
            val haveCount = dish.ingredients.count { have.contains(it) }
            tvHint.text = "Есть в наличии: $haveCount из ${dish.ingredients.size}"
        } else {
            tvHint.text = "Всего продуктов: ${dish.ingredients.size}"
        }

        val ingredientsContainer = findViewById<LinearLayout>(R.id.ingredients_container)
        ingredientsContainer.removeAllViews()
        dish.ingredients.forEach { name ->
            val haveIt = if (showHave) have.contains(name) else null
            addIngredientRow(ingredientsContainer, name, haveIt)
        }

        val stepsContainer = findViewById<LinearLayout>(R.id.steps_container)
        stepsContainer.removeAllViews()
        dish.steps.forEachIndexed { index, step ->
            addStepRow(stepsContainer, index + 1, step)
        }

        val tvTip = findViewById<TextView>(R.id.tv_tip)
        if (dish.tip.isBlank()) {
            tvTip.visibility = View.GONE
        } else {
            tvTip.visibility = View.VISIBLE
            tvTip.text = "💡 Совет: ${dish.tip}"
        }

        // Избранное
        val btnFavorite = findViewById<ImageButton>(R.id.btn_favorite)
        updateFavoriteIcon(btnFavorite, store.isFavorite(dish.id))
        btnFavorite.setOnClickListener {
            val nowFavorite = store.toggleFavorite(dish.id)
            updateFavoriteIcon(btnFavorite, nowFavorite)
            val text = if (nowFavorite) "Добавлено в избранное ⭐" else "Убрано из избранного"
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteIcon(button: ImageButton, isFavorite: Boolean) {
        button.setImageResource(
            if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )
    }

    private fun addIngredientRow(container: LinearLayout, name: String, haveIt: Boolean?) {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL
        row.setPadding(dp(8), dp(8), dp(8), dp(8))

        val mark = TextView(this)
        mark.textSize = 15f
        mark.gravity = Gravity.CENTER
        mark.layoutParams = LinearLayout.LayoutParams(dp(26), LinearLayout.LayoutParams.WRAP_CONTENT)
        when (haveIt) {
            true -> {
                mark.text = "✓"
                mark.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
            false -> {
                mark.text = "✕"
                mark.setTextColor(ContextCompat.getColor(this, R.color.brand_orange))
            }
            null -> {
                mark.text = "•"
                mark.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
            }
        }
        row.addView(mark)

        val label = TextView(this)
        label.text = name
        label.textSize = 14f
        label.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        label.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        row.addView(label)

        if (haveIt == false) {
            val needed = TextView(this)
            needed.text = "нужно купить"
            needed.textSize = 11f
            needed.setTextColor(ContextCompat.getColor(this, R.color.brand_orange_dark))
            needed.setBackgroundResource(R.drawable.bg_pill_orange)
            needed.setPadding(dp(8), dp(2), dp(8), dp(2))
            row.addView(needed)
        }

        container.addView(row)
    }

    private fun addStepRow(container: LinearLayout, number: Int, text: String) {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.setPadding(0, 0, 0, dp(12))

        val numberView = TextView(this)
        numberView.text = number.toString()
        numberView.textSize = 14f
        numberView.setTextColor(ContextCompat.getColor(this, R.color.white))
        numberView.setTypeface(null, Typeface.BOLD)
        numberView.gravity = Gravity.CENTER
        numberView.setBackgroundResource(R.drawable.bg_circle_orange)
        numberView.layoutParams = LinearLayout.LayoutParams(dp(28), dp(28))
        row.addView(numberView)

        val textView = TextView(this)
        textView.text = text
        textView.textSize = 14f
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        textView.setLineSpacing(dp(3).toFloat(), 1f)
        val params = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        params.marginStart = dp(12)
        textView.layoutParams = params
        row.addView(textView)

        container.addView(row)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
