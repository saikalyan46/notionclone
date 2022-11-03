package edu.bu.androiddev.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.navigation.NavHostController
import edu.bu.androiddev.components.NodePageLink
import edu.bu.androiddev.components.NodeTextField
import edu.bu.androiddev.components.NodeToDo
import edu.bu.androiddev.constant.Constant
import edu.bu.androiddev.model.Node
import edu.bu.androiddev.singleton.Singleton
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

open class Page(var name: String?, var navController: NavHostController) {
    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun render(dataStore: DataStore<Preferences>) {
        val scaffoldState = rememberScaffoldState()
        val createPageText = remember { mutableStateOf("") }
        var nodeList:SnapshotStateList<Node>
        var tmpList = readDataAndPopulate(dataStore)
        val openDialog = remember { mutableStateOf(false)  }
        if(tmpList!=null){
            nodeList = mutableStateListOf<Node>(*tmpList.toTypedArray());
        }else{
            nodeList = mutableStateListOf()
        }
//        val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        val scope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Filled.Add,"")
                }
            },
            drawerContent = {
                OutlinedButton(
                    onClick = {
                        nodeList.add(NodeTextField())
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Unspecified),
                    border = BorderStroke(0.dp, Color.Unspecified),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Text Field")
                }
                Divider()
                OutlinedButton(
                    onClick = {
//                        nodeList.add(NodeTextField())
                        openDialog.value = true
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Unspecified),
                    border = BorderStroke(0.dp, Color.Unspecified),
                ) {
                    Text(text = "Page Link")
                }
                Divider()
                OutlinedButton(
                    onClick = {
                        nodeList.add(NodeToDo())
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
//                        openDialog.value = true
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Unspecified),
                    border = BorderStroke(0.dp, Color.Unspecified),
                ) {
                    Text(text = "ToDo Node")
                }
            },
//            floatingActionButton = {
//                FloatingActionButton(onClick = {scope.launch {
//                    drawerState.open()
//                }}) {
//                    Icon(Icons.Filled.Add,"")
//                }
//            },
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
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if(nodeList.size!=0) {
                                var tmpMap: MutableMap<String, *> =
                                    mutableMapOf("data" to mutableListOf<MutableMap<String, *>>())
                                var tmpList =
                                    tmpMap.get("data") as MutableList<MutableMap<String, *>>
                                for (i in 0..nodeList.size - 1) {
                                    tmpList.add(nodeList.get(i).generateJsonMap())
                                }
                                var tmpString = Singleton.objectMapper.writeValueAsString(tmpMap)

                                runBlocking {
                                    dataStore.edit {
                                        var key = name?.let { it1 -> preferencesKey<String>(it1) }
                                            ?: preferencesKey<String>("")
                                        it[key] = Singleton.objectMapper.writeValueAsString(tmpMap)
                                    }
                                }
                            }
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "backPage")
                        }
                        Text(
                            text = name?:""
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
                    Box(
                        modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth()
                    ){}
                }
//                BottomDrawer(drawerState = drawerState,
//                    drawerContent = {
//                        // add your UI code
//                        Button(
//                            onClick = {
//                                nodeList.add(NodeTextField())
//                                scope.launch {
//                                    drawerState.close();
//                                }
//                            },
//                            modifier = Modifier
//                                .height(40.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Text(text = "Text Field")
//                        }
//                        Divider()
//                        Button(
//                            onClick = {
////                        nodeList.add(NodeTextField())
//                            },
//                            modifier = Modifier
//                                .height(40.dp)
//                                .fillMaxWidth()
//                        ) {
//                            Text(text = "Text Field")
//                        }
//                    },
//                    gesturesEnabled = drawerState.isOpen,
//                    scrimColor = if (!drawerState.isOpen) Color.Unspecified else Color(0.4f,0.4f,0.4f,0.6f)
//                ){}
            }

//            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//                ModalDrawer(
//                    drawerState = drawerState,
//                    gesturesEnabled = false,
//                    drawerContent = {
//                        // Drawer content
//                        Button(
//                            onClick = {
//                                nodeList.add(NodeTextField())
//                                scope.launch {
//                                    drawerState.close();
//                                }
//                            },
//                            modifier = Modifier.height(40.dp).fillMaxWidth()
//                        ) {
//                            Text(text = "Text Field")
//                        }
//                        Divider()
//                        Button(
//                            onClick = {
////                        nodeList.add(NodeTextField())
//                            },
//                            modifier = Modifier.height(40.dp).fillMaxWidth()
//                        ) {
//                            Text(text = "Text Field")
//                        }
//                    }
//                ) {
//
//                }
//            }
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
                                    if(nodeList.size!=0) {
                                        var tmpMap: MutableMap<String, *> =
                                            mutableMapOf("data" to mutableListOf<MutableMap<String, *>>())
                                        var tmpList =
                                            tmpMap.get("data") as MutableList<MutableMap<String, *>>
                                        for (i in 0..nodeList.size - 1) {
                                            tmpList.add(nodeList.get(i).generateJsonMap())
                                        }
                                        var tmpString = Singleton.objectMapper.writeValueAsString(tmpMap)

                                        runBlocking {
                                            dataStore.edit {
                                                var key = name?.let { it1 -> preferencesKey<String>(it1) }
                                                    ?: preferencesKey<String>("")
                                                it[key] = Singleton.objectMapper.writeValueAsString(tmpMap)
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
    fun displayNodes(nodeList: SnapshotStateList<Node>){
        Column(modifier = Modifier.padding(top = 10.dp)) {
            nodeList.map { it.render() }
        }
    }

    @Composable
    private fun readDataAndPopulate(dataStore: DataStore<Preferences>): MutableList<Node>? {
        var tmpNodeList: Array<Node?>? = null
        runBlocking {
            dataStore.edit {
                var key = name?.let { it1 -> preferencesKey<String>(it1) }?:preferencesKey<String>("")
                var tmp = it[key]
                if (tmp != null) {
                    var tmpList = Singleton.objectMapper.readValue(tmp, MutableMap::class.java)
                        .get("data") as MutableList<MutableMap<String,*>>
                    tmpNodeList = Array(tmpList.size) { null }
                    for (i in 0..tmpList.size-1) {
                        if((tmpList.get(i).get("node_type") as String).equals("node_text_field")){
                            tmpNodeList!![i] = NodeTextField(tmpList.get(i).get("data") as String)
                        }
                        if((tmpList.get(i).get("node_type") as String).equals("node_page_link")){
                            tmpNodeList!![i] = NodePageLink(tmpList.get(i).get("data") as String, navController)
                        }
                        if((tmpList.get(i).get("node_type") as String).equals("node_to_do")){
                            tmpNodeList!![i] = NodeToDo((tmpList.get(i).get("data") as Map<String,*>).get("data") as String, (tmpList.get(i).get("data") as Map<String,*>).get("checked") as Boolean)
                        }
                    }
                }
            }
        }
        var finalMutableList:MutableList<Node> = mutableListOf();
        if(tmpNodeList==null) return null
        for(i in 0..tmpNodeList!!.size-1){
            var tmp = tmpNodeList!!.get(i)
            if(tmp!=null)
                finalMutableList.add(tmp)
        }
        if(finalMutableList.size == 0)return null
        return finalMutableList

    }
}
