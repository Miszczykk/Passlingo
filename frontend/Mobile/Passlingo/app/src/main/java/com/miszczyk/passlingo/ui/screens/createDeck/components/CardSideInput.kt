package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.miszczyk.passlingo.ui.components.HorizontalDivider
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumPlus
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold

@Composable
fun CardSideInput(
    modifier: Modifier = Modifier,
    label: String,
    hintText: String,
    state: TextFieldState,
    colorLine: Color = MaterialTheme.colorScheme.onBackground
) {
    var isFocused by remember { mutableStateOf(value = false) }

    Column(horizontalAlignment = Alignment.Start, modifier = modifier) {
        Text(
            text = label.uppercase(),
            fontSize = body,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = vagRoundedBold,
        )

        Spacer(modifier = Modifier.height(height = spaceMediumPlus))

        BasicTextField(
            state = state, modifier = Modifier.onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }, lineLimits = TextFieldLineLimits.SingleLine, textStyle = TextStyle(
                fontFamily = vagRoundedBold,
                fontSize = titleMedium,
                color = MaterialTheme.colorScheme.primary
            ), decorator = { innerTextField ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = hintText,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = vagRoundedBold,
                            fontSize = titleMedium,
                            modifier = Modifier.alpha(alpha = if (state.text.isEmpty()) 1f else 0f)
                        )
                        innerTextField()
                    }

                    Spacer(modifier = Modifier.height(height = spaceExtraSmall))
                    HorizontalDivider(
                        (if (!isFocused) colorLine else MaterialTheme.colorScheme.secondary),
                        strokeWidth = 3f
                    )
                }
            }
        )
    }
}