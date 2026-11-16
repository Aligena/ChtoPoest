package com.chtohotovit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chtohotovit.R
import com.chtohotovit.data.Dishes
import com.chtohotovit.data.UserStore

/** Экран со списком появившихся блюд. */
class DishListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val EXTRA_INGREDIENTS = "extra_ingredients"
        const val MODE_MATCH = "match"
        const val MODE_FAVORITES = "favorites"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_list)

        val store = UserStore(this)
        val mode = intent.getStringExtra(EXTRA_MODE) ?: MODE_MATCH
        val have: Set<String> =
            intent.getStringArrayListExtra(EXTRA_INGREDIENTS)?.toSet() ?: emptySet()

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvSubtitle = findViewById<TextView>(R.id.tv_subtitle)
        val rvDishes = findViewById<RecyclerView>(R.id.rv_dishes)
        val tvEmpty = findViewById<TextView>(R.id.tv_empty)

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        rvDishes.layoutManager = LinearLayoutManager(this)

        val items: List<DishAdapter.DishItem> = if (mode == MODE_FAVORITES) {
            val favorites = store.favoriteIds()
            val list = Dishes.all.filter { favorites.contains(it.id.toString()) }
            tvTitle.text = "Избранное"
            tvSubtitle.text = "Сохранённых рецептов: ${list.size}"
            tvEmpty.text = "В избранном пока пусто ⭐\nОткройте блюдо и нажмите на звёздочку"
            list.map { DishAdapter.DishItem(it, -1) }
        } else {
            val scored = Dishes.all.map { dish ->
                DishAdapter.DishItem(dish, dish.ingredients.count { !have.contains(it) })
            }
            val ready = scored.count { it.missing == 0 }
            tvTitle.text = "Что можно приготовить"
            tvSubtitle.text = if (scored.isEmpty()) {
                "Выбрано продуктов: ${have.size}"
            } else {
                "Готовы прямо сейчас: $ready из ${scored.size}"
            }
            tvEmpty.text = "Пока ничего не нашлось 🍽\nДобавьте больше продуктов"
            scored.sortedWith(
                compareBy(
                    { it.missing },
                    { -it.dish.ingredients.count { ing -> have.contains(ing) } },
                    { it.dish.name }
                )
            )
        }

        rvDishes.adapter = DishAdapter(items) { dish ->
            val detail = Intent(this, DishDetailActivity::class.java)
            detail.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id)
            detail.putStringArrayListExtra(
                DishDetailActivity.EXTRA_INGREDIENTS,
                ArrayList(have)
            )
            detail.putExtra(
                DishDetailActivity.EXTRA_SHOW_HAVE,
                mode == MODE_MATCH
            )
            startActivity(detail)
        }

        tvEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        rvDishes.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        // если блюдо убрали из избранного — обновим список
        if (intent.getStringExtra(EXTRA_MODE) == MODE_FAVORITES) {
            val store = UserStore(this)
            val rv = findViewById<RecyclerView>(R.id.rv_dishes)
            val favorites = store.favoriteIds()
            val list = Dishes.all.filter { favorites.contains(it.id.toString()) }
            rv.adapter = DishAdapter(list.map { DishAdapter.DishItem(it, -1) }) { dish ->
                val detail = Intent(this@DishListActivity, DishDetailActivity::class.java)
                detail.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id)
                detail.putExtra(DishDetailActivity.EXTRA_SHOW_HAVE, false)
                startActivity(detail)
            }
            findViewById<TextView>(R.id.tv_subtitle).text =
                "Сохранённых рецептов: ${list.size}"
            findViewById<TextView>(R.id.tv_empty).visibility =
                if (list.isEmpty()) View.VISIBLE else View.GONE
            rv.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}
