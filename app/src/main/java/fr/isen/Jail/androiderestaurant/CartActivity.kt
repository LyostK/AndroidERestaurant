// CartActivity.kt

package fr.isen.Jail.androiderestaurant

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.Jail.androiderestaurant.ui.theme.AndroidERestaurantTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException
import java.io.OutputStreamWriter
import fr.isen.Jail.androiderestaurant.CartItem

class OrderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val cartItems = getCartItems(this)
                    ScaffoldCart(activity = this@OrderActivity, cartItems = cartItems)
                }
            }
        }
    }

    @Composable
fun CartItemComposable(cartItem: CartItem, onItemRemoved: (CartItem) -> Unit, updateTotalPrice: () -> Unit) {
    var quantity = remember { mutableStateOf(cartItem.quantity) }
    var price = remember { mutableStateOf(String.format("%.1f", cartItem.price)) }

    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "${cartItem.dish} x ${quantity.value}, Price: ${price.value}",
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                if (quantity.value > 0) {
                    quantity.value--
                    if (quantity.value <= 0) {
                        onItemRemoved(cartItem)
                    } else {
                        val prix_unite = price.value.toFloat() / (quantity.value + 1)
                        price.value = String.format(
                            "%.1f",
                            price.value.toFloat() - prix_unite
                        )
                        cartItem.quantity = quantity.value
                        cartItem.price = price.value.toFloat()
                    }
                    updateTotalPrice()
                }
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "-",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldCart(activity: OrderActivity, cartItems: MutableList<CartItem>) {
    val stateCartItems = remember { mutableStateOf(cartItems) }
    var totalPrice = remember { mutableStateOf(calculateTotalPrice(stateCartItems.value)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            stateCartItems.value.forEach { cartItem ->
                CartItemComposable(cartItem, { removedItem ->
                    stateCartItems.value.remove(removedItem)
                    updateCartItems(activity, stateCartItems.value)
                    totalPrice.value = calculateTotalPrice(stateCartItems.value)
                }, {
                    totalPrice.value = calculateTotalPrice(stateCartItems.value)
                })
            }

            Text(
                text = "Total: ${totalPrice.value} €",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Button(
            onClick = { /* Do nothing for now */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(80.dp, 48.dp)
        ) {
            Text(text = "Order")
        }
    }
}
    private fun calculateTotalPrice(cartItems: MutableList<CartItem>): Double {
        return cartItems.sumByDouble { it.price.toDouble() }
    }

    fun updateCartItems(context: Context, cartItems: MutableList<CartItem>) {
        val gson = Gson()
        val cartItemsJson = gson.toJson(cartItems)
        context.openFileOutput("cart.json", Context.MODE_PRIVATE).use {
            OutputStreamWriter(it).use { writer ->
                writer.write(cartItemsJson)
            }
        }
    }
}
