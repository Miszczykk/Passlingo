package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.LabeledContent
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.borderThin
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.elevationSmall
import com.miszczyk.passlingo.ui.theme.Dimens.iconGiant
import com.miszczyk.passlingo.ui.theme.Dimens.iconHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceVeryLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

@Composable
fun DeckDetailsSection(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onSelectIconClicked: () -> Unit,
    icon: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LabeledContent(modifier.weight(1f), stringResource(R.string.label_deck_name)) {
            BasicTextField(
                state = state,
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = TextStyle(
                    fontFamily = vagRoundedLight,
                    fontSize = titleMedium,
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
                                borderThin,
                                MaterialTheme.colorScheme.onBackground,
                                RoundedCornerShape(cornerRadiusDefault)
                            )
                            .padding(horizontal = spaceExtraLarge, vertical = spaceVeryLarge),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(R.string.prompt_deck_name_hint),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = vagRoundedLight,
                            fontSize = titleMedium,
                            modifier = Modifier.alpha(if (state.text.isEmpty()) 1f else 0f)
                        )
                        innerTextField()
                    }
                },
            )
        }

        Spacer(modifier = Modifier.width(spaceExtraLarge))

        LabeledContent(
            modifier,
            stringResource(R.string.label_icon),
            Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { onSelectIconClicked() },
                modifier = Modifier
                    .shadow(
                        elevation = elevationSmall,
                        shape = RoundedCornerShape(cornerRadiusDefault)
                    )
                    .size(iconGiant)
                    .clip(RoundedCornerShape(cornerRadiusDefault))
                    .background(MaterialTheme.colorScheme.primary)
                    .border(
                        width = borderDefault,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(cornerRadiusDefault)
                    )
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(R.string.content_desc_deck_icon),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(iconHuge)
                )
            }
        }
    }
}