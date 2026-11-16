package com.chtohotovit.data

/** Ингредиент, который пользователь может отметить на главном экране. */
data class Ingredient(
    val name: String,
    val emoji: String
)

/** Группа ингредиентов (категория в списке выбора). */
data class IngredientGroup(
    val title: String,
    val items: List<Ingredient>
)

/** Полная база ингредиентов приложения. */
object Ingredients {

    val groups: List<IngredientGroup> = listOf(

        IngredientGroup(
            "Овощи и зелень",
            listOf(
                Ingredient("Картофель", "🥔"),
                Ingredient("Морковь", "🥕"),
                Ingredient("Лук репчатый", "🧅"),
                Ingredient("Чеснок", "🧄"),
                Ingredient("Помидоры", "🍅"),
                Ingredient("Огурцы", "🥒"),
                Ingredient("Капуста белокочанная", "🥬"),
                Ingredient("Капуста цветная", "🥦"),
                Ingredient("Брокколи", "🥦"),
                Ingredient("Перец болгарский", "🫑"),
                Ingredient("Баклажан", "🍆"),
                Ingredient("Кабачок", "🥒"),
                Ingredient("Свёкла", "🟣"),
                Ingredient("Тыква", "🎃"),
                Ingredient("Редис", "🔴"),
                Ingredient("Шпинат", "🌿"),
                Ingredient("Салат листовой", "🥗"),
                Ingredient("Зелёный горошек", "🟢"),
                Ingredient("Кукуруза консервированная", "🌽"),
                Ingredient("Шампиньоны", "🍄"),
                Ingredient("Сельдерей", "🌱"),
                Ingredient("Лук зелёный", "🌿"),
                Ingredient("Петрушка", "🌿"),
                Ingredient("Укроп", "🌿"),
                Ingredient("Базилик", "🍃"),
                Ingredient("Имбирь", "🫚")
            )
        ),

        IngredientGroup(
            "Фрукты и ягоды",
            listOf(
                Ingredient("Яблоко", "🍎"),
                Ingredient("Банан", "🍌"),
                Ingredient("Апельсин", "🍊"),
                Ingredient("Лимон", "🍋"),
                Ingredient("Груша", "🍐"),
                Ingredient("Клубника", "🍓"),
                Ingredient("Малина", "🫐"),
                Ingredient("Виноград", "🍇"),
                Ingredient("Киви", "🥝"),
                Ingredient("Персик", "🍑"),
                Ingredient("Черника", "🫐"),
                Ingredient("Авокадо", "🥑")
            )
        ),

        IngredientGroup(
            "Мясо и птица",
            listOf(
                Ingredient("Куриная грудка", "🍗"),
                Ingredient("Куриное филе", "🍗"),
                Ingredient("Куриные бёдра", "🍗"),
                Ingredient("Индейка", "🦃"),
                Ingredient("Говядина", "🥩"),
                Ingredient("Свинина", "🥩"),
                Ingredient("Фарш мясной", "🍖"),
                Ingredient("Бекон", "🥓"),
                Ingredient("Колбаса варёная", "🌭"),
                Ingredient("Сосиски", "🌭")
            )
        ),

        IngredientGroup(
            "Рыба и морепродукты",
            listOf(
                Ingredient("Лосось", "🐟"),
                Ingredient("Тунец", "🐟"),
                Ingredient("Треска", "🐟"),
                Ingredient("Минтай", "🐟"),
                Ingredient("Скумбрия", "🐟"),
                Ingredient("Креветки", "🍤"),
                Ingredient("Мидии", "🦪"),
                Ingredient("Тунец консервированный", "🥫")
            )
        ),

        IngredientGroup(
            "Молочное и яйца",
            listOf(
                Ingredient("Молоко", "🥛"),
                Ingredient("Сливки", "🥛"),
                Ingredient("Сметана", "🥣"),
                Ingredient("Кефир", "🥛"),
                Ingredient("Йогурт натуральный", "🥣"),
                Ingredient("Творог", "🧀"),
                Ingredient("Сыр твёрдый", "🧀"),
                Ingredient("Сыр моцарелла", "🧀"),
                Ingredient("Сыр фета", "🧀"),
                Ingredient("Пармезан", "🧀"),
                Ingredient("Сливочное масло", "🧈"),
                Ingredient("Яйца", "🥚")
            )
        ),

        IngredientGroup(
            "Крупы, макароны и мука",
            listOf(
                Ingredient("Рис", "🍚"),
                Ingredient("Гречка", "🌾"),
                Ingredient("Пшено", "🌾"),
                Ingredient("Овсяные хлопья", "🥣"),
                Ingredient("Булгур", "🌾"),
                Ingredient("Кускус", "🌾"),
                Ingredient("Макароны", "🍝"),
                Ingredient("Спагетти", "🍝"),
                Ingredient("Мука", "🌾"),
                Ingredient("Манка", "🌾"),
                Ingredient("Хлеб", "🍞"),
                Ingredient("Батон", "🥖"),
                Ingredient("Лаваш", "🌯"),
                Ingredient("Дрожжи", "🫧"),
                Ingredient("Разрыхлитель", "🫧"),
                Ingredient("Крахмал", "🥄")
            )
        ),

        IngredientGroup(
            "Бобовые и орехи",
            listOf(
                Ingredient("Фасоль", "🫘"),
                Ingredient("Чечевица", "🫘"),
                Ingredient("Горох", "🫛"),
                Ingredient("Нут", "🫘"),
                Ingredient("Грецкие орехи", "🌰"),
                Ingredient("Миндаль", "🌰"),
                Ingredient("Семечки", "🌻")
            )
        ),

        IngredientGroup(
            "Специи, соусы и прочее",
            listOf(
                Ingredient("Оливковое масло", "🫒"),
                Ingredient("Растительное масло", "🌻"),
                Ingredient("Соевый соус", "🥫"),
                Ingredient("Томатная паста", "🥫"),
                Ingredient("Мёд", "🍯"),
                Ingredient("Сахар", "🧂"),
                Ingredient("Соль", "🧂"),
                Ingredient("Перец чёрный", "⚫"),
                Ingredient("Паприка", "🌶"),
                Ingredient("Карри", "🍛"),
                Ingredient("Корица", "🪵"),
                Ingredient("Куркума", "🟡"),
                Ingredient("Орегано", "🌿"),
                Ingredient("Тмин", "🌱"),
                Ingredient("Лавровый лист", "🍃"),
                Ingredient("Мята", "🌿"),
                Ingredient("Какао", "🍫"),
                Ingredient("Шоколад", "🍫"),
                Ingredient("Ванильный сахар", "🍬"),
                Ingredient("Уксус", "🧴"),
                Ingredient("Горчица", "🌭"),
                Ingredient("Майонез", "🥫"),
                Ingredient("Бульон куриный", "🍲"),
                Ingredient("Кокосовое молоко", "🥥"),
                Ingredient("Сода", "🫧"),
                Ingredient("Вода", "💧")
            )
        )
    )

    /** Плоский список всех названий — для проверок. */
    val allNames: Set<String> =
        groups.flatMap { group -> group.items }.map { it.name }.toSet()

    /** Эмодзи ингредиента по названию. */
    fun emojiOf(name: String): String =
        groups.asSequence()
            .flatMap { it.items.asSequence() }
            .firstOrNull { it.name == name }
            ?.emoji ?: "•"

    val totalCount: Int
        get() = groups.sumOf { it.items.size }
}
