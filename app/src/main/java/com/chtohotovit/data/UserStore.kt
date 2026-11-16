package com.chtohotovit.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Простое локальное «хранилище аккаунтов» на SharedPreferences.
 * Никакого интернета не нужно — всё работает на устройстве.
 */
class UserStore(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("chto_prigotovit", Context.MODE_PRIVATE)

    // ---------------- Аккаунт ----------------

    val currentEmail: String?
        get() = prefs.getString(KEY_CURRENT, null)

    fun isLoggedIn(): Boolean = !currentEmail.isNullOrEmpty()

    /** Регистрация. Возвращает текст ошибки или null, если всё ок. */
    fun register(name: String, email: String, password: String): String? {
        val mail = email.trim().lowercase()
        val userName = name.trim()

        when {
            userName.length < 2 -> return "Введите имя (минимум 2 символа)"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches() -> return "Введите корректный e-mail"
            password.length < 4 -> return "Пароль должен быть не короче 4 символов"
            prefs.contains("pass_$mail") -> return "Такой аккаунт уже существует — войдите"
        }

        prefs.edit()
            .putString("pass_$mail", password)
            .putString("name_$mail", userName)
            .putInt("goal_$mail", 2000)
            .putStringSet("fav_$mail", mutableSetOf())
            .putString(KEY_CURRENT, mail)
            .apply()
        return null
    }

    /** Вход. Возвращает текст ошибки или null, если всё ок. */
    fun login(email: String, password: String): String? {
        val mail = email.trim().lowercase()
        if (mail.isEmpty()) return "Введите e-mail"
        val saved = prefs.getString("pass_$mail", null)
            ?: return "Аккаунт не найден — зарегистрируйтесь"
        if (saved != password) return "Неверный пароль"
        prefs.edit().putString(KEY_CURRENT, mail).apply()
        return null
    }

    fun logout() {
        prefs.edit().remove(KEY_CURRENT).apply()
    }

    fun deleteCurrentAccount() {
        val mail = currentEmail ?: return
        prefs.edit()
            .remove("pass_$mail")
            .remove("name_$mail")
            .remove("goal_$mail")
            .remove("fav_$mail")
            .remove(KEY_CURRENT)
            .apply()
    }

    // ---------------- Профиль ----------------

    fun userName(): String {
        val mail = currentEmail ?: return "Гость"
        return prefs.getString("name_$mail", "Гость") ?: "Гость"
    }

    fun calorieGoal(): Int {
        val mail = currentEmail ?: return 2000
        return prefs.getInt("goal_$mail", 2000)
    }

    /** Обновление профиля. Возвращает текст ошибки или null. */
    fun updateProfile(name: String, email: String, goal: Int): String? {
        val oldMail = currentEmail ?: return "Сначала войдите в аккаунт"
        val newMail = email.trim().lowercase()
        val userName = name.trim()

        when {
            userName.length < 2 -> return "Введите имя (минимум 2 символа)"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(newMail).matches() -> return "Введите корректный e-mail"
            goal !in 800..6000 -> return "Норма калорий должна быть от 800 до 6000"
            newMail != oldMail && prefs.contains("pass_$newMail") -> return "Этот e-mail уже занят"
        }

        val editor = prefs.edit()
        val password = prefs.getString("pass_$oldMail", "") ?: ""
        val favorites = prefs.getStringSet("fav_$oldMail", mutableSetOf()) ?: mutableSetOf()

        if (newMail != oldMail) {
            editor.remove("pass_$oldMail")
                .remove("name_$oldMail")
                .remove("goal_$oldMail")
                .remove("fav_$oldMail")
                .putString("pass_$newMail", password)
                .putStringSet("fav_$newMail", favorites)
        }

        editor.putString("name_$newMail", userName)
            .putInt("goal_$newMail", goal)
            .putString(KEY_CURRENT, newMail)
            .apply()
        return null
    }

    // ---------------- Избранное ----------------

    fun favoriteIds(): Set<String> {
        val mail = currentEmail ?: return emptySet()
        return prefs.getStringSet("fav_$mail", mutableSetOf()) ?: emptySet()
    }

    fun isFavorite(dishId: Int): Boolean = favoriteIds().contains(dishId.toString())

    /** Добавляет/удаляет блюдо из избранного, возвращает новое состояние. */
    fun toggleFavorite(dishId: Int): Boolean {
        val mail = currentEmail ?: return false
        val set = (prefs.getStringSet("fav_$mail", mutableSetOf()) ?: mutableSetOf()).toMutableSet()
        val key = dishId.toString()
        val nowFavorite = if (set.contains(key)) {
            set.remove(key)
            false
        } else {
            set.add(key)
            true
        }
        prefs.edit().putStringSet("fav_$mail", set).apply()
        return nowFavorite
    }

    private companion object {
        const val KEY_CURRENT = "current_email"
    }
}
