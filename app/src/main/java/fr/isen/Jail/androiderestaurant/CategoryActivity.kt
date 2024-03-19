// CategoryActivity.kt
package fr.isen.Jail.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject
import java.io.Serializable

class CategoryActivity : ComponentActivity() {
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryName = intent.getStringExtra("categoryName") ?: ""
        title = categoryName // Set the title of the page to the category name
        queue = Volley.newRequestQueue(this)
        setContent {
            val dishes = remember { mutableStateListOf<Dish>() }
            fetchMenu(categoryName, dishes)
            CategoryList(dishes)
        }
    }

    private fun fetchMenu(category: String, dishes: MutableList<Dish>) {
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val postData = JSONObject().apply { put("id_shop", "1") }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            { response ->
                val menuResponse = Gson().fromJson(response.toString(), MenuResponse::class.java)
                Log.d("fetchMenu", "Category names in MenuResponse: ${menuResponse.data.map { it.nameFr }}") // Add logging here
                val selectedCategory = menuResponse.data.firstOrNull { it.nameFr == category }
                dishes.addAll(selectedCategory?.dishes ?: listOf())
                Log.d("VolleyResponse", "Response: $response")
            },
            { error ->
                Log.e("VolleyError", "Error: ${error.message}")
                Toast.makeText(this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }

    @Composable
    fun CategoryList(dishes: List<Dish>) {
        Log.d("CategoryList", "Displaying dishes: $dishes")
        val context = LocalContext.current
        LazyColumn {
            items(items = dishes) { dish ->
                Log.d("CategoryList", "Displaying dish: $dish")
                Text(
                    text = dish.nameFr,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                        .clickable {
                            Log.d("CategoryActivity", "Dish clicked: $dish")
                            val intent = Intent(context, DishDetailActivity::class.java)
                            intent.putExtra("dish", dish as Serializable)
                            context.startActivity(intent)
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}