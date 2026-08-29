package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun DeckDetailsSection(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spaceExtraLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LabeledContent(modifier.weight(1f), "DECK NAME") {
            val state = rememberTextFieldState("")
            BasicTextField(
                state = state,
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false
                ),
                textStyle = TextStyle(
                    fontFamily = vagRoundedLight,
                    fontSize = 21.sp,
                    color = MaterialTheme.colorScheme.primary
                ),
                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = elevationSmall,
                                shape = RoundedCornerShape(cornerRadiusDefault)
                            )
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(cornerRadiusDefault)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onBackground,
                                RoundedCornerShape(cornerRadiusDefault)
                            )
                            .padding(horizontal = 30.dp, vertical = 25.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "e.g, Spanish Verbs A1",
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontFamily = vagRoundedLight,
                            fontSize = 21.sp,
                            modifier = Modifier.alpha(if (state.text.isEmpty()) 1f else 0f)

                        )
                        innerTextField()
                    }
                },
            )
        }

        Spacer(modifier = Modifier.width(30.dp))

        LabeledContent(modifier, "ICON", Alignment.CenterHorizontally) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .padding(spaceMediumLarge)
                    .scale(1.6f)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(cornerRadiusDefault)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.deck_animal_penguinopithecus),
                    contentDescription = stringResource(R.string.content_desc_lock), //TODO to change
                    tint = MaterialTheme.colorScheme.secondary,

                    )
            }
        }
    }
}