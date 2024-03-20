// DishDetailActivity.kt
package fr.isen.Jail.androiderestaurant

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import fr.isen.Jail.androiderestaurant.Dish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter

data class CartItem(val dishName: String, val quantity: Int, val price: Float)

class DishDetailActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra("dish") as Dish
        setContent {
            val snackbarHostState = SnackbarHostState()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    val pagerState = rememberPagerState()
                    HorizontalPager(count = dish.images.size, state = pagerState, modifier = Modifier.height(200.dp).fillMaxWidth()) { page ->
                        Image(
                            painter = rememberImagePainter(
                                data = dish.images[page],
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
                    Text(text = "Name: ${dish.nameFr}")
                    var quantity by remember { mutableStateOf(1) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { if (quantity > 1) quantity-- }) {
                            Text(text = "-")
                        }

                        Text(text = "Quantity: $quantity")

                        Button(onClick = { if (quantity < 10) quantity++ }) {
                            Text(text = "+")
                        }
                    }
                    val price = dish.prices.firstOrNull()?.price ?: 0f
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            // Handle click
                            val cartItem = CartItem(dish.nameFr, quantity, price.toFloat() * quantity)
                            val cartItems = mutableListOf<CartItem>()
                            cartItems.add(cartItem)
                            val gson = Gson()
                            val cartItemsJson = gson.toJson(cartItems)
                            applicationContext.openFileOutput("cart.json", Context.MODE_PRIVATE).use {
                                OutputStreamWriter(it).use { writer ->
                                    writer.write(cartItemsJson)
                                }
                            }
                            snackbarHostState.currentSnackbarData?.dismiss()
                            CoroutineScope(Dispatchers.Main).launch {
                                snackbarHostState.showSnackbar("Item added to cart")
                            }
                        }) {
                            Text(text = "Price: ${(price.toFloat() * quantity).toString()} €")
                        }
                    }
                    // display other properties...
                }
                SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}