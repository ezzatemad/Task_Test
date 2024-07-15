package com.example.task.details

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task.R
import androidx.compose.ui.res.painterResource


class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "No Title"
            val taskDescription = intent.getStringExtra("TASK_DESCRIPTION") ?: "No Description"
            val taskDate = intent.getStringExtra("TASK_DATE") ?: "No Date"
            DetailsScreen(taskTitle, taskDescription, taskDate)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(taskTitle: String, taskDes: String, taskDate: String) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
            ) {
                TopAppBar(
                    title = {
                        Text(text = "")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            (context as? Activity)?.finish()
                        }) {
                            Icon(
                                painter = painterResource(id =R.drawable.ic_arrow_back),
                                contentDescription = "Back",
                                Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.screen_background),
                        titleContentColor = Color.Transparent
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.screen_background))
                        .padding(8.dp)
                ) {
                    TaskDetailsText(
                        text = taskDate,
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.card_date_color)
                        ),
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 8.dp,
                            end = 8.dp,
                            bottom = 15.dp
                        )
                    )
                    TaskDetailsText(
                        text = taskTitle,
                        maxLines = 3,
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = colorResource(id = R.color.white)
                        ),
                        modifier = Modifier.padding(5.dp)
                    )
                    TaskDetailsText(
                        text = taskDes,
                        maxLines = 10,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = colorResource(id = R.color.card_details_color)
                        ),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun TaskDetailsText(
    text: String,
    maxLines: Int,
    style: TextStyle,
    modifier: Modifier
) {
    Text(
        text = text,
        maxLines = maxLines,
        style = style,
        textAlign = TextAlign.Start,
        modifier = modifier
    )

}

@Composable
@Preview(showBackground = true)
fun Preview() {
    DetailsScreen("", "", "")
}