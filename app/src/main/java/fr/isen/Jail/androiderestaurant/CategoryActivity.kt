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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

// This is the main activity for displaying the categories of dishes.
class CategoryActivity : ComponentActivity() {
    // A queue for managing network requests.
    private lateinit var queue: RequestQueue

    // This function is called when the activity is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the category name from the intent extras.
        val categoryName = intent.getStringExtra("categoryName") ?: ""
        // Set the title of the activity to the category name.
        title = categoryName
        // Initialize the request queue.
        queue = Volley.newRequestQueue(this)
        // Set the content of the activity.
        setContent {
            // Create a mutable list of dishes.
            val dishes = remember { mutableStateListOf<Dish>() }
            // Fetch the menu for the given category.
            fetchMenu(categoryName, dishes)
            // Display the list of dishes in the category.
            CategoryList(dishes)
        }
    }

    // This function fetches the menu for a given category.
    private fun fetchMenu(category: String, dishes: MutableList<Dish>) {
        // The URL of the API endpoint.
        val url = "http://test.api.catering.bluecodegames.com/menu"
        // The data to be sent with the request.
        val postData = JSONObject().apply { put("id_shop", "1") }

        // Create a JSON object request.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, postData,
            { response ->
                // Parse the response into a MenuResponse object.
                val menuResponse = Gson().fromJson(response.toString(), MenuResponse::class.java)
                // Log the names of the categories in the response.
                Log.d("fetchMenu", "Category names in MenuResponse: ${menuResponse.data.map { it.nameFr }}")
                // Find the selected category in the response.
                val selectedCategory = menuResponse.data.firstOrNull { it.nameFr == category }
                // Add the dishes in the selected category to the list of dishes.
                dishes.addAll(selectedCategory?.dishes ?: listOf())
                // Log the response.
                Log.d("VolleyResponse", "Response: $response")
            },
            { error ->
                // Log any errors that occurred during the request.
                Log.e("VolleyError", "Error: ${error.message}")
                // Show a toast message indicating that there was an error fetching the data.
                Toast.makeText(this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show()
            }
        )
        // Add the request to the request queue.
        queue.add(jsonObjectRequest)
    }

    // This composable function displays a list of dishes in a category.
    @Composable
    fun CategoryList(dishes: List<Dish>) {
        val context = LocalContext.current
        LazyColumn {
            items(items = dishes) { dish ->
                Log.d("CategoryList", "Displaying dish: $dish")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (dish.images.isNotEmpty()) {
                        val painter = rememberImagePainter(
                            data = dish.images[0],
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_background)
                                error(R.drawable.ic_launcher_background)
                            }
                        )
                        Image(
                            painter = painter,
                            contentDescription = "Dish image",
                            modifier = Modifier.size(128.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = dish.nameFr,
                            modifier = Modifier.clickable {
                                Log.d("CategoryActivity", "Dish clicked: $dish")
                                val intent = Intent(context, DishDetailActivity::class.java)
                                intent.putExtra("dish", dish as Serializable)
                                context.startActivity(intent)
                            }
                        )
                        Text(
                            text = "${dish.prices.firstOrNull()?.price ?: ""} €"
                        )
                    }
                }
                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}