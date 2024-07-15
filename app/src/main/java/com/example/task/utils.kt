package com.example.task

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task.home.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*


fun showDatePicker(
    homeViewModel: HomeViewModel,
    context: Context,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val selectedDate =
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(calendar.time)
            onDateSelected(selectedDate)
            homeViewModel.onDueDateChange(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskOutlinedTextField(
    label: String,
    state: MutableState<String>,
    errorState: MutableState<String>
) {
    Column {
        OutlinedTextField(
            value = state.value,
            onValueChange = { state.value = it },
            label = {
                Text(label)
            },
            isError = errorState.value.isNotEmpty(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorTextColor = Color.Red,
                focusedLabelColor = colorResource(id = R.color.white),
                unfocusedLabelColor = colorResource(id = R.color.white2),
                focusedTextColor = colorResource(id = R.color.white),
                unfocusedTextColor = colorResource(id = R.color.white),
                focusedBorderColor = colorResource(id = R.color.white),
                unfocusedBorderColor = colorResource(id = R.color.white),
                cursorColor = colorResource(id = R.color.white)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        if (errorState.value.isNotEmpty()) {
            Text(
                text = errorState.value,
                style = TextStyle(color = Color.Red, fontSize = 12.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


@Composable
fun FAB(modifier: Modifier, onButtonClick: () -> Unit) {
    FloatingActionButton(
        onClick = onButtonClick,
        shape = FloatingActionButtonDefaults.largeShape,
        containerColor = colorResource(id = R.color.fab),
        contentColor = colorResource(id = R.color.white),
        modifier = modifier
            .padding(16.dp),
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add Task")
    }
}

@Composable
fun CardTask(
    title: String,
    description: String,
    date: String,
    shadowColor: Color,
    onClick: (String, String, String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .graphicsLayer {
                shadowElevation = 0f
                shape = RoundedCornerShape(8.dp)
                clip = false
            }
            .drawBehind {
                val cornerRadius = 8.dp.toPx()
                val shadowHeight = 1.dp.toPx()
                val shadowColor = shadowColor.copy(alpha = 0.5f)

                drawIntoCanvas { canvas ->
                    val paint = androidx.compose.ui.graphics
                        .Paint()
                        .apply {
                            color = shadowColor
                        }
                    canvas.drawRoundRect(
                        left = 0f,
                        top = size.height - cornerRadius,
                        right = size.width,
                        bottom = size.height + shadowHeight,
                        radiusX = cornerRadius,
                        radiusY = cornerRadius,
                        paint = paint
                    )
                }
            }
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.card_background))
            .clickable { onClick(title, description, date) }
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                TaskText(
                    text = title,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier.padding(8.dp)
                )
                TaskTextDescription(
                    text = description,
                    maxLines = 5,
                    style = TextStyle(
                        fontSize = 17.sp,
                        color = colorResource(id = R.color.card_details_color)
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .heightIn(min = 70.dp)
                )
                TaskText(
                    text = date,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.card_details_color)
                    ),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 12.dp,
                        bottom = 1.dp
                    )
                )
            }
        }
    }
}


@Composable
fun TaskText(text: String, maxLines: Int, style: TextStyle, modifier: Modifier) {
    Text(
        text = text,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        style = style,
        textAlign = TextAlign.Start,
        modifier = modifier
    )
}

@Composable
fun TaskTextDescription(text: String, maxLines: Int, style: TextStyle, modifier: Modifier) {
    Text(
        text = text,
        minLines = 5,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        style = style,
        textAlign = TextAlign.Start,
        modifier = modifier
    )
}


val shadowColors = listOf(
    Color(0xFFD6B9B1),
    Color(0xFFE0D2B8),
    Color(0xFFD7DBC2),
    Color(0xFFA3BFC3),
)