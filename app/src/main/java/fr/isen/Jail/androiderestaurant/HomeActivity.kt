package fr.isen.Jail.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.Jail.androiderestaurant.ui.theme.AndroidERestaurantTheme

// Rest of your code...

class HomeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.resto_image),
                            contentDescription = "Image_Resto_Top",
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            val context = LocalContext.current
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))

                                MenuCategory("Entrée") {
                                    Toast.makeText(context, "Vous avez cliqué sur Entrée", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, EntreeActivity::class.java)
                                    context.startActivity(intent)
                                }
                                MenuCategory("Plat") {
                                    Toast.makeText(context, "Vous avez cliqué sur Plat", Toast.LENGTH_SHORT).show()
                                }
                                MenuCategory("Dessert") {
                                    Toast.makeText(context, "Vous avez cliqué sur Dessert", Toast.LENGTH_SHORT).show()
                                }
                            }

                            CategoryScreen(
                                categoryName = "Entrées",
                                dishes = listOf("Salade", "Soupe"),
                                navigateToDishDetail = ::navigateToDishDetail // Pass the navigateToDishDetail function
                            )

                        }
                        }
                    }
                }
            }
        }
    }
fun navigateToDishDetail(dishName: String) {
    // Navigate to the detail screen for the selected dish
    // You can implement your navigation logic here
}


                        /* Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Greeting("Android")
                            val context = LocalContext.current
                            Column {
                                Button(onClick = {
                                    Toast.makeText(context, "Je fonctionne", Toast.LENGTH_SHORT).show()
                                }) {
                                    Text("Entrée")
                                }
                                Button(onClick = {
                                    Toast.makeText(context, "Je fonctionne", Toast.LENGTH_SHORT).show()
                                }) {
                                    Text("Plat")
                                }
                                Button(onClick = {
                                    Toast.makeText(context, "Je fonctionne", Toast.LENGTH_SHORT).show()
                                }) {
                                    Text("Dessert")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
*/

@Composable
fun CategoryScreen(categoryName: String, dishes: List<String>, navigateToDishDetail: (String) -> Unit) {
}

@Composable
fun MenuCategory(name: String, onClick: () -> Unit) {
    Text(
        text = name,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFFff8519), // Set the text color to #ff8519
        modifier = Modifier
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick)
            .fillMaxWidth(),
        textAlign = TextAlign.Center // Center the text
    )
    Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 50.dp))
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        Greeting("Android")
    }
}

