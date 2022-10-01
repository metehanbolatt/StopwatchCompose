package com.metehanbolat.stopwatchcompose

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metehanbolat.stopwatchcompose.service.ServiceHelper
import com.metehanbolat.stopwatchcompose.service.StopwatchService
import com.metehanbolat.stopwatchcompose.service.StopwatchState
import com.metehanbolat.stopwatchcompose.util.Constants.ACTION_SERVICE_CANCEL
import com.metehanbolat.stopwatchcompose.util.Constants.ACTION_SERVICE_START
import com.metehanbolat.stopwatchcompose.util.Constants.ACTION_SERVICE_STOP

@ExperimentalAnimationApi
@Composable
fun MainScreen(stopwatchService: StopwatchService) {
    val context = LocalContext.current
    val hours by stopwatchService.hours
    val minutes by stopwatchService.minutes
    val seconds by stopwatchService.seconds
    val currentState by stopwatchService.currentState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(weight = 9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = hours, transitionSpec = { addAnimation() }) {
                Text(
                    text = hours,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (hours == stringResource(id = R.string.initial_value)) Color.White else Color.Blue
                    )
                )
            }
            AnimatedContent(targetState = minutes, transitionSpec = { addAnimation() }) {
                Text(
                    text = minutes,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (hours == stringResource(id = R.string.initial_value)) Color.White else Color.Blue
                    )
                )
            }
            AnimatedContent(targetState = seconds, transitionSpec = { addAnimation() }) {
                Text(
                    text = seconds,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (hours == stringResource(id = R.string.initial_value)) Color.White else Color.Blue
                    )
                )
            }
        }
        Row(
            modifier = Modifier.weight(weight = 1f)
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP else ACTION_SERVICE_START
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (currentState == StopwatchState.Started) Color.Red else Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (currentState == StopwatchState.Started) stringResource(id = R.string.stop)
                    else if (currentState == StopwatchState.Stopped) stringResource(id = R.string.resume)
                    else stringResource(id = R.string.start)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(0.8f),
                    onClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_CANCEL
                        )
                    },
                    enabled = seconds != stringResource(id = R.string.initial_value) && currentState != StopwatchState.Started,
                    colors = ButtonDefaults.buttonColors(disabledBackgroundColor = Color.LightGray)
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        }
    }
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec =  tween(durationMillis = duration)) { height -> height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}