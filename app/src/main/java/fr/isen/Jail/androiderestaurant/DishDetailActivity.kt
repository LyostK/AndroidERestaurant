// DishDetailActivity.kt
package fr.isen.Jail.androiderestaurant

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text

class DishDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dishName = intent.getStringExtra("dishName")
        //Log.d("DishDetailActivity", "Dish name: $dishName")
        setContent {
            Text(text = dishName ?: "No dish name provided")
        }
    }
}