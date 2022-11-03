package edu.bu.androiddev.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.bu.androiddev.R
import edu.bu.androiddev.model.Node


class NodeToDo( var dataText:String = "", var checked:Boolean = false): Node {
    lateinit var text:MutableState<String>
    lateinit var focusRequester:FocusRequester
    val nodeType = "node_text_field"
    lateinit var checkedState: MutableState<Boolean>

    override fun generateJsonMap(): MutableMap<String, *> {
        var tmpMap = mutableMapOf("data" to mutableMapOf("checked" to checkedState.value, "data" to text.value), "node_type" to "node_to_do")
        return tmpMap
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun render():Unit {
        focusRequester = FocusRequester()
        text = remember { mutableStateOf(dataText) }
        checkedState = remember { mutableStateOf(checked) }
//        focusRequester = FocusRequester()
//        BasicTextField(value = text.value, onValueChange = { str: String -> text.value = str },
//            textStyle = TextStyle(color = Color.LightGray),
//            cursorBrush = Brush.verticalGradient(0f to Color.LightGray, 1f to Color.LightGray),
//            modifier = Modifier.focusRequester(focusRequester).fillMaxWidth()
//        )
        Divider()
        Row(modifier = Modifier
            .height(IntrinsicSize.Max)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .width(30.dp)) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(
//                        painter = painterResource(R.drawable.holder),
//                        contentDescription = "Contact profile picture",
////                    modifier = Modifier.align(Alignment.CenterVertically),
//                        alignment = Alignment.Center
//                    )
                    Icon(Icons.Filled.Menu,"")
                }

            }
            Checkbox(
                    // below line we are setting
                    // the state of checkbox.
                    checked = checkedState.value,
            // below line is use to add padding
            // to our checkbox.
            modifier = Modifier.padding(0.dp),
            // below line is use to add on check
            // change to our checkbox.
            onCheckedChange = { checkedState.value = it },
            )
            BasicTextField(value = text.value,
                onValueChange = { str: String -> text.value = str},
                textStyle = TextStyle(color = if(checkedState.value) Color.DarkGray else Color.LightGray, textDecoration = if(checkedState.value) TextDecoration.LineThrough else null),
                cursorBrush = Brush.verticalGradient(0f to Color.White, 1f to Color.White),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
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

            )
//            OutlinedTextField(value = text.value, onValueChange = { str:String-> text.value = str }, modifier = Modifier
//                .fillMaxWidth()
//                .focusRequester(focusRequester))

        }
        Divider()
//        TextField(value = text.value, onValueChange = { str:String-> text.value = str })
//        Text(text = text.value)
    }

}