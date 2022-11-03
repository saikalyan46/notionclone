package edu.bu.androiddev.components

import android.graphics.Outline
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.bu.androiddev.model.Node
import edu.bu.androiddev.navigation.NavData

class NodePageLink(var name:String,var navController: NavController): Node {
    lateinit var text:MutableState<String>
    lateinit var focusRequester:FocusRequester

    override fun generateJsonMap(): MutableMap<String, *> {
        var tmpMap = mutableMapOf("data" to name, "node_type" to "node_page_link")
        return tmpMap
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun render():Unit {
        focusRequester = FocusRequester()
        text = remember { mutableStateOf("") }
//        focusRequester = FocusRequester()
//        BasicTextField(value = text.value, onValueChange = { str: String -> text.value = str },
//            textStyle = TextStyle(color = Color.LightGray),
//            cursorBrush = Brush.verticalGradient(0f to Color.LightGray, 1f to Color.LightGray),
//            modifier = Modifier.focusRequester(focusRequester).fillMaxWidth()
//        )
        Row(modifier = Modifier.height(IntrinsicSize.Max), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.List,"")



             OutlinedButton(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Unspecified),
                border = BorderStroke(0.dp, Color.Unspecified),
                onClick = {
                    navController.navigate(NavData.page.withArgs(name))
                }
//                    .onKeyEvent {
//                            event: KeyEvent ->
//                        // handle backspace key
//                        if (event.type == KeyEventType.KeyUp &&
//                            event.key == Key.Backspace &&
//                            text.value.isEmpty()
//                        // also any additional checks of the "list" i.e isNotEmpty()
//                        ) {
//                            // TODO remove from list
//                            return@onKeyEvent true
//                        }
//                        false
//                    }

            ){
                Text(name)
            }
//            OutlinedTextField(value = text.value, onValueChange = { str:String-> text.value = str }, modifier = Modifier
//                .fillMaxWidth()
//                .focusRequester(focusRequester))
        }

    }

//        TextField(value = text.value, onValueChange = { str:String-> text.value = str })
//        Text(text = text.value)
}
