package com.chtohotovit.ui

import android.content.Intent
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
import androidx.core.widget.doAfterTextChanged
import com.chtohotovit.R
import com.chtohotovit.data.Ingredients
import com.chtohotovit.data.UserStore
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

/** Главный экран: выбор продуктов. */
class MainActivity : AppCompatActivity() {

    private val store by lazy { UserStore(this) }
    private val selected = mutableSetOf<String>()

    private lateinit var chipContainer: LinearLayout
    private lateinit var etSearch: TextInputEditText
    private lateinit var tvGreeting: TextView
    private lateinit var tvSelected: TextView
    private lateinit var tvClear: TextView
    private lateinit var btnFind: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chipContainer = findViewById(R.id.chip_container)
        etSearch = findViewById(R.id.et_search)
        tvGreeting = findViewById(R.id.tv_greeting)
        tvSelected = findViewById(R.id.tv_selected)
        tvClear = findViewById(R.id.btn_clear)
        btnFind = findViewById(R.id.btn_find)

        findViewById<ImageButton>(R.id.btn_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        etSearch.doAfterTextChanged { text ->
            buildChips(text?.toString().orEmpty())
        }

        tvClear.setOnClickListener {
            selected.clear()
            buildChips(etSearch.text?.toString().orEmpty())
            updateCounter()
        }

        btnFind.setOnClickListener { openDishes() }

        buildChips("")
        updateCounter()
    }

    override fun onResume() {
        super.onResume()
        if (!store.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        tvGreeting.text = "Привет, ${store.userName()}! 👋"
    }

    // ---------------- Список продуктов ----------------

    private fun buildChips(query: String) {
        chipContainer.removeAllViews()
        val filter = query.trim().lowercase()
        var chipsShown = 0
        var firstGroup = true

        Ingredients.groups.forEach { group ->
            val visible = group.items.filter {
                filter.isEmpty() || it.name.lowercase().contains(filter)
            }
            if (visible.isEmpty()) return@forEach

            val header = TextView(this)
            header.text = "${group.title} · ${visible.size}"
            header.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
            header.textSize = 14f
            header.setTypeface(null, Typeface.BOLD)
            val headerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            headerParams.topMargin = dp(if (firstGroup) 0 else 18)
            headerParams.bottomMargin = dp(8)
            header.layoutParams = headerParams
            chipContainer.addView(header)

            val chipGroup = ChipGroup(this)
            chipGroup.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            chipGroup.isSingleLine = false
            chipGroup.chipSpacingHorizontal = dp(8)
            chipGroup.chipSpacingVertical = dp(8)

            visible.forEach { ingredient ->
                val chip = Chip(this)
                chip.text = "${ingredient.emoji} ${ingredient.name}"
                chip.isCheckable = true
                chip.isChecked = selected.contains(ingredient.name)
                chip.isChipIconVisible = false
                chip.setTextColor(ContextCompat.getColorStateList(this, R.color.chip_text))
                chip.setChipBackgroundColorResource(R.color.chip_bg)
                chip.setChipStrokeColorResource(R.color.chip_stroke)
                chip.chipStrokeWidth = dp(1).toFloat()
                chip.setChipCornerRadius(dp(14).toFloat())
                chip.setEnsureMinTouchTargetSize(false)
                chip.textSize = 14f
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selected.add(ingredient.name)
                    else selected.remove(ingredient.name)
                    updateCounter()
                }
                chipGroup.addView(chip)
                chipsShown++
            }

            chipContainer.addView(chipGroup)
            firstGroup = false
        }

        if (chipsShown == 0) {
            val empty = TextView(this)
            empty.text = "Продукт не найден 🍽\nПопробуйте другое название"
            empty.gravity = Gravity.CENTER
            empty.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
            empty.textSize = 14f
            empty.setPadding(0, dp(40), 0, 0)
            chipContainer.addView(empty)
        }
    }

    private fun updateCounter() {
        tvSelected.text = "Выбрано: ${selected.size}"
        btnFind.text = if (selected.isEmpty()) {
            "Подобрать блюда"
        } else {
            "Подобрать блюда (${selected.size})"
        }
    }

    private fun openDishes() {
        if (selected.isEmpty()) {
            Toast.makeText(this, "Отметьте хотя бы один продукт", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, DishListActivity::class.java)
        intent.putExtra(DishListActivity.EXTRA_MODE, DishListActivity.MODE_MATCH)
        intent.putStringArrayListExtra(
            DishListActivity.EXTRA_INGREDIENTS,
            ArrayList(selected)
        )
        startActivity(intent)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
