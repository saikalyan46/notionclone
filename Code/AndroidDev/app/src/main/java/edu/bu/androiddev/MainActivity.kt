package edu.bu.androiddev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.createDataStore
import edu.bu.androiddev.navigation.navMesh
import edu.bu.androiddev.ui.theme.AndroidDevTheme

class MainActivity() : ComponentActivity() {
    val dataStore = createDataStore(name="cheepu")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidDevTheme {
                navMesh(dataStore)
//                page.render()
//                Navigation()
            }

        }
    }
}

@Composable
fun Greeting(name: String) {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidDevTheme {
        Greeting("Android")
    }
}