package be.leocheikhboukal.pokemontcgmanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import be.leocheikhboukal.pokemontcgmanager.ui.theme.PokemonTCGManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (!isOnline(this)) {
            Toast.makeText(this, "No Internet connexion, the application will close", Toast.LENGTH_LONG).show()
            finish()
        }
        setContent {
            PokemonTCGManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    PTCGManagerApp()
                }
            }
        }
    }
}

/**
 * Check if the device is connected to the internet, Ethernet, Wifi or Cellular
 * @param context The context of the app
 * @return True if the device is connected to the internet, false otherwise
 */
fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities != null &&
            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
}