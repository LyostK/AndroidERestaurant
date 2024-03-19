package fr.isen.Jail.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.Jail.androiderestaurant.ui.theme.AndroidERestaurantTheme

// Définition des modèles de données basés sur la structure JSON.
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Ingredient(
    @SerializedName("name_fr") val nameFr: String
): Serializable

data class Dish(
    @SerializedName("name_fr") val nameFr: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("ingredients") val ingredients: List<Ingredient>
): Serializable

data class Category(
    @SerializedName("name_fr") val nameFr: String,
    @SerializedName("items") val dishes: List<Dish>
): Serializable

data class MenuResponse(
    @SerializedName("data") val data: List<Category>
): Serializable