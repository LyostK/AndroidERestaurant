// DishDetailActivity.kt
package fr.isen.Jail.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.common.reflect.TypeToken
import com.google.gson.JsonSyntaxException


@OptIn(ExperimentalCoilApi::class)
@Composable
fun DishImage(imageUrl: String, snackbarHostState: SnackbarHostState) {
    val imageLoader = LocalImageLoader.current
    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .build()

    var result by remember { mutableStateOf<coil.request.ImageResult?>(null) }

    LaunchedEffect(imageUrl) {
        result = imageLoader.execute(request)
    }

    when (result) {
        is SuccessResult -> {
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_background)
                        error(R.drawable.ic_launcher_background)
                    }
                ),
                contentDescription = "Dish image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
        else -> {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar("Failed to load image")
            }
        }
    }
}

class DishDetailActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra("dish") as Dish
        setContent {
            val snackbarHostState = SnackbarHostState()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                val price = dish.prices.firstOrNull()?.price ?: 0f
                var quantity by remember { mutableStateOf(1) }
                Column {
                    val pagerState = rememberPagerState()
                    HorizontalPager(count = dish.images.size, state = pagerState, modifier = Modifier.height(200.dp).fillMaxWidth()) { page ->
                        DishImage(imageUrl = dish.images[page], snackbarHostState = snackbarHostState)
                    }
                    Text(text = "${dish.nameFr}", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp)) // Increase the font size for the dish name
                    Text(text = "${dish.ingredients.joinToString { it.nameFr }}", fontSize = 16.sp, modifier = Modifier.padding(16.dp)) // Set a smaller font size for the composition
                    Text(text = "Price: $price €", modifier = Modifier.padding(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { if (quantity > 1) quantity-- }, shape = CircleShape, modifier = Modifier.size(50.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(text = "$quantity", fontSize = 24.sp)
                        Button(onClick = { if (quantity < 10) quantity++ }, shape = CircleShape, modifier = Modifier.size(50.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Other composables...

                        Button(onClick = {
                            // Handle click
                            val cartItem = CartItem(dish.nameFr, quantity, price.toFloat() * quantity)
                            val gson = Gson()

                            // Check if the file exists before trying to open it
                            val file = applicationContext.getFileStreamPath("cart.json")
                            var existingCartItemsJson = ""
                            if (file != null && file.exists()) {
                                try {
                                    // Read the existing content of the file
                                    existingCartItemsJson = applicationContext.openFileInput("cart.json").bufferedReader().use { it.readText() }
                                } catch (e: Exception) {
                                    Log.e("DishDetailActivity", "Error reading cart file", e)
                                }
                            }

                            // Deserialize it into a list of CartItem
                            val cartItems: MutableList<CartItem> = if (existingCartItemsJson.isNotBlank()) {
                                try {
                                    gson.fromJson(existingCartItemsJson, object : TypeToken<List<CartItem>>() {}.type) as MutableList<CartItem>
                                } catch (e: JsonSyntaxException) {
                                    Log.e("DishDetailActivity", "Error parsing cart JSON", e)
                                    mutableListOf()
                                }
                            } else {
                                mutableListOf()
                            }

                            // Add the new item to the list
                            cartItems.add(cartItem)

                            // Serialize the updated list back into JSON
                            val cartItemsJson = gson.toJson(cartItems)

                            // Write the updated JSON back to the file
                            try {
                                applicationContext.openFileOutput("cart.json", Context.MODE_PRIVATE).use {
                                    OutputStreamWriter(it).use { writer ->
                                        writer.write(cartItemsJson)
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("DishDetailActivity", "Error writing to cart file", e)
                            }

                            snackbarHostState.currentSnackbarData?.dismiss()
                            CoroutineScope(Dispatchers.Main).launch {
                                snackbarHostState.showSnackbar("Item added to cart")
                            }
                        }, modifier = Modifier.padding(bottom = 20.dp)){
                            Box(modifier = Modifier.size(width = 300.dp, height = 25.dp), contentAlignment = Alignment.Center) {
                                val totalPrice = price.toDouble() * quantity.toFloat()
                                Text(text = "Total: ${totalPrice} €")
                            }
                        }
                    }
                }
                SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, OrderActivity::class.java)
                Log.d("DishDetailActivity", "Cart clicked")

                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}