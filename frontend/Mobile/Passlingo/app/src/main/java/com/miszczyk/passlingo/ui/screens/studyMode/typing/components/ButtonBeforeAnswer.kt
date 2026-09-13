package com.miszczyk.passlingo.ui.screens.studyMode.typing.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun ButtonBeforeAnswer(buttonColor: Color, textColor: Color, textDescription: String, enabled: Boolean, onClick: () -> Unit){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        enabled = enabled,
        onClick = {
            onClick()
        }) {
        Text(
            text = textDescription,
            fontSize = titleLarge,
            color = textColor,
            fontFamily = vagRoundedBold,
            modifier = Modifier.padding(vertical = spaceDefault)
        )
    }
}