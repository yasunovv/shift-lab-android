package com.yasunov.shiftapp.registration

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.yasunov.shiftapp.MyApplication
import com.yasunov.shiftapp.R
import java.text.DateFormat
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreen(
    onRegistrationButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: RegistrationViewModel = viewModel(
        factory = RegistrationViewModel.provideFactory(
            (LocalContext.current.applicationContext as MyApplication).appRepository,
            owner = LocalSavedStateRegistryOwner.current
        )
    )
    val uiState by viewModel.uiState.collectAsState()
    var isPickerChecked by remember {
        mutableStateOf(false)
    }



    if (isPickerChecked) {
        DatePickerDialog(
            onDismissRequest = { isPickerChecked = false },
            onDateChange = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.updateTime(
                        Date.from(
                            it.atStartOfDay(ZoneId.systemDefault()).toInstant()
                        )
                    )
                }
                isPickerChecked = false
            },
            title = { Text(text = "Когда у вас день рождения?") },
            buttonColors = ButtonDefaults.textButtonColors(contentColor = Color.Blue)
        )
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Авторизация",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Column(Modifier.width(IntrinsicSize.Min)) {
                Text(
                    text = "Имя",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.firstName,
                    onValueChange = {
                        viewModel.updateFirstName(it)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedVisibility(uiState.firstName.isNotEmpty()) {
                    Column {
                        if (!viewModel.checkNameLength(uiState.firstName)) HintMessage(
                            message = stringResource(R.string.hint_len_first_name),
                            isError = true
                        )
                        else HintMessage(
                            message = stringResource(R.string.hint_len_first_name),
                            isError = false
                        )
                        if (!viewModel.checkNameSymb(uiState.firstName)) HintMessage(
                            message = stringResource(R.string.hint_len_first_symb),
                            isError = true
                        )
                        else HintMessage(
                            message = stringResource(R.string.hint_len_first_symb),
                            isError = false
                        )
                    }

                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Фамилия",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.secondName, onValueChange = {
                        viewModel.updateSecondName(it)
                    }, singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedVisibility(uiState.secondName.isNotEmpty()) {
                    Column {
                        if (!viewModel.checkNameLength(uiState.secondName)) HintMessage(
                            message = stringResource(R.string.hint_len_second_name),
                            isError = true
                        )
                        else HintMessage(
                            message = stringResource(R.string.hint_len_second_name),
                            isError = false
                        )
                        if (!viewModel.checkNameSymb(uiState.secondName)) HintMessage(
                            message = stringResource(R.string.hint_len_second_symb),
                            isError = true
                        )
                        else HintMessage(
                            message = stringResource(R.string.hint_len_second_symb),
                            isError = false
                        )
                    }

                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Дата рождения",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { isPickerChecked = true }) {
                    Text(text = DateFormat.getDateInstance().format(uiState.selectedDate))
                }
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedVisibility(true) {
                    if (viewModel.checkAge()) HintMessage(
                        message = stringResource(R.string.hint_date_age),
                        isError = false
                    )
                    else
                        HintMessage(
                            message = stringResource(R.string.hint_date_age),
                            isError = true
                        )

                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Пароль",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.firstPassword, onValueChange = {
                        viewModel.updateFirstPassword(it)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedVisibility(uiState.firstPassword.isNotEmpty()) {
                    Column {
                        if (viewModel.checkPasswordLength()) HintMessage(
                            message = stringResource(R.string.hint_password_len),
                            isError = false
                        ) else HintMessage(
                            message = stringResource(R.string.hint_password_len),
                            isError = true
                        )
                        //                    Num check

                        if (viewModel.checkPasswordConsistDigit()) HintMessage(
                            message = stringResource(R.string.hint_password_symb_num),
                            isError = false
                        ) else HintMessage(
                            message = stringResource(R.string.hint_password_symb_num),
                            isError = true
                        )
                        //                    white space check
                        if (viewModel.checkPasswordNotConsistWhiteSpace()) HintMessage(
                            message = stringResource(R.string.hint_password_symb_white_space),
                            isError = false
                        ) else HintMessage(
                            message = stringResource(R.string.hint_password_symb_white_space),
                            isError = true
                        )
                        //                    upper casecheck
                        if (viewModel.checkPasswordConsistUpperCase()) HintMessage(
                            message = stringResource(R.string.hint_password_symb_case),
                            isError = false
                        ) else HintMessage(
                            message = stringResource(R.string.hint_password_symb_case),
                            isError = true
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Повторите пароль",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.secondPassword, onValueChange = {
                        viewModel.updateSecondPassword(it)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedVisibility(uiState.secondPassword.isNotEmpty()) {
                    if (viewModel.checkPasswordsEquality()) HintMessage(
                        message = stringResource(R.string.hint_password_symb_repeat),
                        isError = false
                    )
                    else
                        HintMessage(
                            message = stringResource(R.string.hint_password_symb_repeat),
                            isError = true
                        )

                }

            }
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    viewModel.signUpUser()
                    onRegistrationButtonClicked()
                },
                enabled = viewModel.checkSubmit()
            ) {
                Text(text = "Зарегистрироваться")
            }
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun HintMessage(message: String, isError: Boolean) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(8.dp)
                .background(
                    if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
