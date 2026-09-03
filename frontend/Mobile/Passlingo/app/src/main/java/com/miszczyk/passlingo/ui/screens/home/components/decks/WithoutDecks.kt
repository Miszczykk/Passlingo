package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun WithoutDecks(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .shadow(elevation = elevationExtraSmall, shape = RoundedCornerShape(cornerRadiusDefault))
        .background(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(cornerRadiusDefault)
        )
        .border(borderThin, color = MaterialTheme.colorScheme.onBackground, RoundedCornerShape(cornerRadiusDefault)).padding(spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.write),
                contentDescription = stringResource(R.string.action_create_flashcards),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(spaceLarge))
        Text(
            text = "No decks yet",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            fontFamily = vagRoundedBold,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Create your first deck to start earning time.",
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 15.sp,
            fontFamily = vagRoundedLight,
        )
    }
}