// CartActivity.kt

package fr.isen.Jail.androiderestaurant

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.Jail.androiderestaurant.ui.theme.AndroidERestaurantTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException

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
                    ScaffoldCart(activity =this@OrderActivity, cartItems = cartItems)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldCart(activity: OrderActivity, cartItems: List<CartItem>) {
    // ...
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // replace 16.dp with your desired padding
    ) {
        Column {
            cartItems.forEach { cartItem ->
                CartItemComposable(cartItem) // Pass the entire cart item
            }
        }
    }
    // ...
}

@Composable
fun CartItemComposable(cartItem: CartItem) {
    val dish = cartItem // Access the dish property from the CartItem
    val quantity = cartItem.quantity // Access the quantity property from the CartItem

    Text(text = "${dish} x $quantity")
}

fun getCartItemsFromActivity(context: Context): MutableList<CartItem> {
    val gson = Gson()
    val json = try {
        context.openFileInput("cart.json")?.bufferedReader().use { it?.readText() }
    } catch (e: FileNotFoundException) {
        null
    }
    if (json.isNullOrEmpty()) {
        return mutableListOf()
    }
    val type = object : TypeToken<MutableList<CartItem>>() {}.type
    return gson.fromJson(json, type)
}


/* package fr.isen.Jail.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.Jail.androiderestaurant.ui.theme.AndroidERestaurantTheme

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
                    ScaffoldCart(activity =this@OrderActivity, cartItems = cartItems)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldCart(activity: OrderActivity, cartItems: List<CartItem>) {
    // ...
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // replace 16.dp with your desired padding
    ) {
        Column {
            cartItems.forEach { cartItem ->
                CartItemComposable(cartItem) // Pass the entire cart item
            }
        }
    }
    // ...
}

@Composable
fun CartItemComposable(cartItem: CartItem) {
    val dish = cartItem.dish // Access the dish property from the CartItem
    val quantity = cartItem.quantity // Access the quantity property from the CartItem

    Text(text = "${dish.nameFr} x $quantity")
}
*/