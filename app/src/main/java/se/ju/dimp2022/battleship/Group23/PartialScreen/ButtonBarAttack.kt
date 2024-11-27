package se.ju.dimp2022.battleship.Group23.PartialScreen


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import se.ju.dimp2022.battleship.Group23.MyClasses.MyGameResult
import se.ju.dimp2022.battleship.Group23.MyClasses.MyPlayerTurn
import se.ju.dimp2022.battleship.Group23.Objects.AppColours
import se.ju.dimp2022.battleship.Group23.Screens.Screens
import se.ju.dimp2022.battleship.Group23.ViewModels.ReplayViewModel
import se.ju.dimp2022.battleship.Group23.ViewModels.ShipListViewModel
import java.time.format.TextStyle


@Composable
fun AttackButtonBar(
    navController: NavHostController, gameViewModel: ShipListViewModel ) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val haveSelectedCell by gameViewModel.haveSelectedCell.collectAsState()//TODO: Create a function in view-model that returns a cell or a bool if the player have selected an cell to attack
    val aimingAt by gameViewModel.aimingAt.collectAsState()
    val myTurn = gameViewModel.myTurn.collectAsState()
    val gameResult = gameViewModel.gameResult.collectAsState()
    val showSurrenderDialog = gameViewModel.showSurrender.collectAsState()


    Box(
        modifier = Modifier,
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(         // Rectangle at the bottom
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    AppColours
                        .darkBlue4()
                        .copy(alpha = 0.7f)
                ),
            contentAlignment = Alignment.TopCenter
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(
                        AppColours
                            .darkBlue4()
                            .copy(alpha = 0.7f)
                    ),

                ){}
            // SideButtons--------------------------------------------------------------------

            //skapa så användaren kan byta mellan attack, defend och cancel game?
            if (currentRoute == Screens.GameAttack.route) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(end = 15.dp, start = 15.dp, bottom = 5.dp)
                    .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    TextButton(
                        onClick = {
                            gameViewModel.showSurrenderDialog()
                        },
                        elevation = ButtonDefaults.buttonElevation(5.dp, 0.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColours.darkBlue4().copy(alpha = 0.7f),
                            contentColor = Color.White,
                        ),
                        modifier = Modifier
                            .border(3.dp, AppColours.darkBlue3(), RoundedCornerShape(50))
                            .padding(2.dp)
                    ) {
                        //TODO: Switch Screen to defend and make them look good
                        Text("Surrender", fontSize = 14.sp)
                    }



                    if (myTurn.value == MyPlayerTurn.No){
                        MoveButtonDefend(navController, gameViewModel)

                    } else {
                        TextButton(
                            onClick = {
                                navController.navigate(Screens.GameDefend.route)
                            },
                            elevation = ButtonDefaults.buttonElevation(5.dp, 0.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColours.darkBlue4().copy(alpha = 0.7f),
                                contentColor = Color.White,
                            ),
                            modifier = Modifier
                                .border(3.dp, AppColours.darkBlue3(), RoundedCornerShape(50))
                                .padding(2.dp)
                        ) {
                            //TODO: Animate switch between Attack and defend screen
                            Text("  Defend ", fontSize = 14.sp)
                        }

                    }


                }
            }
            // SideButtons--------------------------------------------------------------------
        }
        if (myTurn.value == MyPlayerTurn.Yes && haveSelectedCell) {
            ButtonFire(gameViewModel, aimingAt)

        } else{
            Box(modifier = Modifier
                .size(100.dp)
                .offset(y = (-5).dp)
            ){

            }
        }
    }

    //popUps after everything has been printed out
    if (showSurrenderDialog.value){
        ButtonSurrender(gameViewModel)
    }
    if (gameResult.value != MyGameResult.Neutral){
        GameResultPopup(gameResult, navController ,gameViewModel)
    }
}

@Composable
fun ButtonFire(
    gameViewModel: ReplayViewModel,
    aimingAt: Pair<Int, Int>,
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            )
        )
    }
    Box(
        modifier = Modifier
            .size(100.dp)
            .offset(y = (-5).dp)
            .background(
                AppColours
                    .darkBlue4()
                    .copy(alpha = 0.7f), CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = {
                println("In fireButton are going to fire at ${aimingAt}")
                gameViewModel.fireAt(aimingAt)

            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = AppColours.red1().copy(alpha = 0.75f)),
            modifier = Modifier
                .size(90.dp)
                .scale(scale.value)
                .border(4.dp, Color.Black, CircleShape)
        ) {
            Text(
                "FIRE",
                color = Color.White,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun MoveButtonDefend(
    navController: NavHostController,
    gameViewModel: ReplayViewModel,
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1.25f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            )
        )
    }

    TextButton(
        onClick = {
            navController.navigate(Screens.GameDefend.route)
        },
        elevation = ButtonDefaults.buttonElevation(5.dp, 0.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColours.green1().copy(alpha = 0.7f),
            contentColor = Color.White.copy(0.95f),
        ),
        modifier = Modifier
            .padding(2.dp)
            .scale(scale.value)
    ) {
        //TODO: Animate switch between Attack and defend screen
        Text("Defend", fontSize = 14.sp)
    }
}