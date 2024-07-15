package com.example.task.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.task.CardTask
import com.example.task.FAB
import com.example.task.R
import com.example.task.TaskOutlinedTextField
import com.example.task.details.DetailsActivity
import com.example.task.shadowColors
import com.example.task.showDatePicker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //E
            val homeViewModel: HomeViewModel = hiltViewModel()
            // Fetch all tasks
            homeViewModel.getAllTasks()


            HomeComponent(homeViewModel)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComponent(homeViewModel: HomeViewModel) {
    var isGrid by remember { mutableStateOf(true) }
    var selectedButton by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
            ) {
                TopAppBar(
                    title = { Text(text = "Tasks") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.screen_background),
                        titleContentColor = Color.White
                    )
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                        .align(Alignment.BottomCenter)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.screen_background))
                    .padding(paddingValues)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            OutlinedButton(
                                text = "Name",
                                isSelected = selectedButton == "Name",
                                onButtonClick = {
                                    selectedButton = "Name"
                                    homeViewModel.toggleSortByName()
                                },
                            )
                            OutlinedButton(
                                text = "Date",
                                isSelected = selectedButton == "Date",
                                onButtonClick = {
                                    selectedButton = "Date"
                                    homeViewModel.toggleSortByDate()
                                },
                            )
                        }

                        Row {
                            if (isGrid) {
                                OutlinedButton(
                                    text = "Grid",
                                    isSelected = selectedButton == "Grid",
                                    onButtonClick = {
                                        selectedButton = "Grid"
                                        isGrid = false
                                    },
                                    icon = R.drawable.ic_grid
                                )
                            } else {
                                OutlinedButton(
                                    text = "List",
                                    isSelected = selectedButton == "List",
                                    onButtonClick = {
                                        selectedButton = "List"
                                        isGrid = true
                                    },
                                    icon = R.drawable.ic_list
                                )
                            }
                        }
                    }

                    // Display either the grid or list layout
                    if (isGrid) {
                        TaskGridList(homeViewModel)
                    } else {
                        TaskList(homeViewModel)
                    }
                }

                FAB(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onButtonClick = {
                        homeViewModel.setCurrentDate() // Set current date before showing the bottom sheet
                        showBottomSheet = true
                    }
                )

                if (showBottomSheet) {
                    AddTaskBottomSheet(
                        showBottomSheet = showBottomSheet,
                        onDismissSheet = { showBottomSheet = false },
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    )
}


@Composable
fun TaskList(homeViewModel: HomeViewModel) {
    // Collect StateFlow
    val taskList by homeViewModel.taskList.collectAsState(initial = emptyList())
    val context = LocalContext.current

    LazyColumn {
        items(taskList.size) { index ->
            val task = taskList[index]
            CardTask(
                title = task.title!!,
                description = task.description.toString(),
                date = task.dueDate.toString(),
                shadowColor = shadowColors[index % shadowColors.size],
                onClick = { title, description, date ->
                    val intent = Intent(context, DetailsActivity::class.java).apply {
                        putExtra("TASK_TITLE", title)
                        putExtra("TASK_DESCRIPTION", description)
                        putExtra("TASK_DATE", date)
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    showBottomSheet: Boolean,
    onDismissSheet: () -> Unit,
    homeViewModel: HomeViewModel
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    ModalBottomSheet(
        containerColor = colorResource(id = R.color.screen_background),
        sheetState = sheetState,
        onDismissRequest = onDismissSheet,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.screen_background))
            ) {
                BottomSheetContent(homeViewModel) {
                    onDismissSheet()
                }
            }
        }
    )
}

@Composable
fun BottomSheetContent(
    homeViewModel: HomeViewModel,
    onTaskInserted: () -> Unit
) {

    val selectedDate by homeViewModel.date.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, bottom = 30.dp)
    ) {
        Text(
            text = stringResource(R.string.add_new_task),
            style = TextStyle(
                fontSize = 15.sp,
                color = colorResource(id = R.color.white)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        TaskOutlinedTextField(label = "Title", homeViewModel.title, homeViewModel.title_error)
        TaskOutlinedTextField(
            label = "Description",
            homeViewModel.description,
            homeViewModel.desc_error
        )
        Text(
            text = stringResource(R.string.set_date),
            style = TextStyle(
                fontSize = 15.sp,
                color = colorResource(id = R.color.white)
            ),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        Text(
            text = selectedDate,
            style = TextStyle(
                fontSize = 15.sp,
                color = colorResource(id = R.color.white)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    showDatePicker(homeViewModel, context) { date ->
                        homeViewModel.onDueDateChange(date)
                    }
                }
        )
        FilledButton(
            text = stringResource(R.string.save_task),
            modifier = Modifier.padding(bottom = 25.dp),
            onButtonClick = {
                if (homeViewModel.insertTask()) {
                    onTaskInserted()
                }
            }
        )
    }
}


@Composable
fun OutlinedButton(
    text: String,
    isSelected: Boolean,
    onButtonClick: () -> Unit,
    icon: Int? = null
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onButtonClick,
        border = if (isSelected) null
        else BorderStroke(1.dp, colorResource(id = R.color.white)),
        modifier = Modifier.padding(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isSelected) Color.White else colorResource(id = R.color.white),
            containerColor = if (isSelected) colorResource(id = R.color.fab) else Color.Transparent
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            style = TextStyle(color = colorResource(id = R.color.white)),

            )
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 2.dp)
            )
        }
    }
}


@Composable
fun FilledButton(text: String, modifier: Modifier, onButtonClick: () -> Unit) {
    Button(
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.fab)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),

        ) {
        Text(
            text = text,
            style = TextStyle(color = colorResource(id = R.color.white))
        )
    }
}

@Composable
fun TaskGridList(homeViewModel: HomeViewModel) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        val taskList by homeViewModel.taskList.collectAsState(initial = emptyList())

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                items(taskList.size) { index ->
                    val task = taskList[index]
                    CardTask(
                        title = task.title!!,
                        description = task.description.toString(),
                        date = task.dueDate.toString(),
                        shadowColor = shadowColors[index % shadowColors.size],
                        onClick = { title, description, date ->
                            val intent = Intent(context, DetailsActivity::class.java).apply {
                                putExtra("TASK_TITLE", title)
                                putExtra("TASK_DESCRIPTION", description)
                                putExtra("TASK_DATE", date)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
}