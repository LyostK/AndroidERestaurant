// Modele.kt
package fr.isen.Jail.androiderestaurant

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Ingredient(
    @SerializedName("name_fr") val nameFr: String
): Serializable

data class Price(
    @SerializedName("price") val price: Double
): Serializable

data class Dish(
    @SerializedName("name_fr") val nameFr: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("ingredients") val ingredients: List<Ingredient>,
    @SerializedName("prices") val prices: List<Price>
): Serializable

data class Category(
    @SerializedName("name_fr") val nameFr: String,
    @SerializedName("items") val dishes: List<Dish>
): Serializable

data class MenuResponse(
    @SerializedName("data") val data: List<Category>
): Serializable

data class CartItem(val dish: String, var quantity: Int, var price: Float) {}