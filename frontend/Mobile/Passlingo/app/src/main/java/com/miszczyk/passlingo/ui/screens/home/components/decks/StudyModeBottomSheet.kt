package com.miszczyk.passlingo.ui.screens.home.components.decks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.BottomSheetHeader
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.theme.Dimens.borderDefault
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraSmall
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import com.miszczyk.passlingo.ui.util.rememberSheetCloseHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyModeBottomSheet(
    sheetState: SheetState,
    deckName: String,
    onDismissRequest: () -> Unit,
    onFlashcardClicked: () -> Unit,
    onQuizClicked: () -> Unit,
    onTypingClicked: () -> Unit
){
    val closeSheet = rememberSheetCloseHandler(sheetState, onDismissRequest)

    ModalBottomSheet(
        onDismissRequest = closeSheet, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomSheetHeader(label = stringResource(id = R.string.label_select_study_mode) ){
                closeSheet()
            }

            Text(
                text = stringResource(id = R.string.prompt_choose_study_mode, deckName),
                fontSize = body,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedLight,
                modifier = Modifier.padding(horizontal = spaceExtraLarge)
            )

            Spacer(modifier = Modifier.height(height = 15.dp))

            ThemedDivider(colorLine = MaterialTheme.colorScheme.onSecondary)

            Spacer(modifier = Modifier.height(height = 15.dp))

            ModeButton(
                iconResId = R.drawable.flashcard,
                textTitle = stringResource(id = R.string.label_mode_flashcards),
                textDescription = stringResource(id = R.string.desc_mode_flashcards),
                onClick = onFlashcardClicked
            )

            ModeButton(
                iconResId = R.drawable.quiz,
                textTitle = stringResource(id = R.string.label_mode_quiz),
                textDescription = stringResource(id = R.string.desc_mode_quiz),
                onClick = onQuizClicked
            )

            ModeButton(
                iconResId = R.drawable.typing,
                textTitle = stringResource(id = R.string.label_mode_typing),
                textDescription = stringResource(id = R.string.desc_mode_typing),
                onClick = onTypingClicked
            )

            Spacer(modifier = Modifier.height(height = spaceExtraLarge))
        }
    }
}

@Composable
private fun ModeButton(
    iconResId: Int,
    textTitle: String,
    textDescription: String,
    onClick: () -> Unit
){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = spaceExtraLarge)
            .border(
                width = borderDefault,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = cornerRadiusDefault)
            ),
        shape = RoundedCornerShape(size = cornerRadiusDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        contentPadding = PaddingValues(0.dp),
        onClick = {
            onClick()
        }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            horizontalArrangement = Arrangement.Start
        ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = stringResource(id = iconResId),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(size = iconExtraHuge)
                        .background(
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(size = cornerRadiusDefault)
                        )
                        .padding(all = spaceExtraSmall)

                )

            Spacer(modifier = Modifier.width(width = spaceLarge))

            Column{
                Text(
                    text = textTitle,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = titleMedium,
                    fontFamily = vagRoundedBold,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = spaceMedium))
                Text(
                    text = textDescription,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = body,
                    fontFamily = vagRoundedLight,
                )
            }
        }
    }
}