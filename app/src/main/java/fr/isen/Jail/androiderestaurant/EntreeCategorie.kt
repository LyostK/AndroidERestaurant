package fr.isen.Jail.androiderestaurant
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.Jail.androiderestaurant.ui.theme.AndroidERestaurantTheme

import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues


class EntreeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val entrees = listOf(
                        "Croustillant de fromage",
                        "Choux rouge et manthe",
                        "Pois chiches et ouef parfait",
                        "Tartare de boeuf"
                    )
                    EntreeList(entrees, this)
                }
            }
        }
    }
}

class EntreeCompositionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val entreeName = intent.getStringExtra("entreeName")
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = entreeName ?: "No entree name provided")
                    }
                }
            }
        }
    }
}

@Composable
fun EntreeList(entrees: List<String>, context: Context) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(entrees) { entree ->
                Button(
                    onClick = {
                        val intent = Intent(context, EntreeCompositionActivity::class.java)
                        intent.putExtra("entreeName", entree)
                        context.startActivity(intent)
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(text = entree, fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.height(16.dp)) // Add vertical space between buttons
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EntreeListPreview() {
    AndroidERestaurantTheme {
        val entrees = listOf(
            "Croustillant de fromage",
            "Choux rouge et manthe",
            "Pois chiches et ouef parfait",
            "Tartare de boeuf"
        )
        val context = LocalContext.current
        EntreeList(entrees, context)
    }
}