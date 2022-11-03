package edu.bu.androiddev.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import androidx.navigation.NavHostController
import edu.bu.androiddev.components.NodePageLink
import edu.bu.androiddev.constant.Constant
import kotlinx.coroutines.runBlocking
import edu.bu.androiddev.singleton.Singleton.Companion.objectMapper

open class MainPage(var navController: NavHostController) {
    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun render(dataStore: DataStore<Preferences>) {

//        var dataStore = LocalContext.current.createDataStore(name="cheepu")
        var tmp = readDataAndPopulate(dataStore)
        val openDialog = remember { mutableStateOf(false)  }
        val createPageText = remember { mutableStateOf("") }
        var nodeList: SnapshotStateList<NodePageLink>
        if(tmp!=null){
            nodeList = mutableStateListOf<NodePageLink>(*tmp);
        }else{
            nodeList = mutableStateListOf<NodePageLink>();
        }

        val scope = rememberCoroutineScope()
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { openDialog.value = true}) {
                    Icon(Icons.Filled.Add,"")
                }
            }
        ) {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
//                onClick = {
//                    if(nodeList.size!=0){
//                        var tmp = nodeList[nodeList.size-1]
//                        if(tmp is NodeTextField) {
//                            tmp.focusRequester.requestFocus()
//                        }
//                    }else{
//                        var temp = NodeTextField()
//                        nodeList.add(temp)
////                        temp.focusRequester.requestFocus()
//                    }
//                }
            ) {
                Column(modifier = Modifier
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Text(
                            text = "Create Page"
                        )
                        Text(text = "")
                        Text(
                            text = ""
                        )
                        Text(
                            text = "",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                    Divider(thickness = 1.dp, color = Color.DarkGray)
                    displayNodes(nodeList = nodeList)
                }
            }
            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Enter Page Name")
                    },
                    text = {
                        OutlinedTextField(value = createPageText.value, onValueChange = { str:String-> createPageText.value = str }, modifier = Modifier.fillMaxWidth())
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                                if(createPageText.value.length!=0) {
//                                    navController.navigate(NavData.page.withArgs(createPageText.value))
                                    nodeList.add(NodePageLink(name = createPageText.value, navController))
//                                    var key = preferencesKey<String>(createPageText.value)
                                    runBlocking {
                                        dataStore.edit {
                                            var tmp = it[Constant.MAIN_DATA_KEY]
                                            if(tmp==null || tmp.length==0){
                                                var tmpMap = mutableMapOf("data" to mutableListOf(createPageText.value))
                                                it[Constant.MAIN_DATA_KEY] = objectMapper.writeValueAsString(tmpMap)
                                            }else{
                                                var tmpMap = objectMapper.readValue(tmp,MutableMap::class.java)
                                                (tmpMap["data"] as MutableList<String>).add(
                                                    createPageText.value as String
                                                )
                                                it[Constant.MAIN_DATA_KEY] = objectMapper.writeValueAsString(tmpMap)
                                            }
                                        }
                                    }
                                    createPageText.value = ""

                                }
                            }) {
                            Text("Create")
                        }
                    },
                )
            }
        }

    }

    @Composable
    private fun readDataAndPopulate(dataStore: DataStore<Preferences>): Array<NodePageLink>? {
        var tmpNodeList: Array<NodePageLink>? = null
        runBlocking {
            dataStore.edit {
                var tmp = it[Constant.MAIN_DATA_KEY]
                if (tmp != null) {
                    var tmpList = objectMapper.readValue(tmp, MutableMap::class.java)
                        .get("data") as MutableList<*>
                    tmpNodeList = Array(tmpList.size) { NodePageLink("", navController) }
                    for (i in 0..tmpList.size-1) {
                        tmpNodeList!![i] = NodePageLink(name = tmpList[i] as String, navController)
                    }
                }
            }
        }
        return tmpNodeList

    }

    @Composable
    fun displayNodes(nodeList: SnapshotStateList<NodePageLink>){
        Column(modifier = Modifier.padding(top = 10.dp)) {
            nodeList.map { it.render() }
        }
    }
}
