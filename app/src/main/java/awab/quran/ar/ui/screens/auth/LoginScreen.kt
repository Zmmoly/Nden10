package awab.quran.ar.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import awab.quran.ar.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val auth = FirebaseAuth.getInstance()

    fun performLogin() {
        if (!validateInputs(email, password,
                onEmailError = { emailError = it },
                onPasswordError = { passwordError = it }
            )) {
            return
        }

        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    Toast.makeText(context, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                } else {
                    Toast.makeText(
                        context,
                        "فشل تسجيل الدخول: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // صورة الخلفية
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = "خلفية تسجيل الدخول",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // شعار التطبيق - أيقونة القرآن
            Icon(
                painter = painterResource(id = R.drawable.app_background),
                contentDescription = "شعار التطبيق",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp),
                tint = Color.Unspecified
            )

            // عنوان التطبيق
            Text(
                text = "تسميع القرآن",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B5744),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "تحفظوا حفظك القرآن الكريم الذكاء الكريم،\nالذكاء الاصطناعي.",
                fontSize = 14.sp,
                color = Color(0xFF8B7355),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // بطاقة تسجيل الدخول
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F3ED).copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // حقل البريد الإلكتروني
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        label = { Text("البريد الإلكتروني", color = Color(0xFF8B7355)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = Color(0xFF8B7355)
                            )
                        },
                        isError = emailError != null,
                        supportingText = {
                            emailError?.let { Text(it, color = Color.Red) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF8B7355),
                            unfocusedBorderColor = Color(0xFFD4C5A9),
                            focusedLabelColor = Color(0xFF8B7355),
                            unfocusedLabelColor = Color(0xFF8B7355),
                            cursorColor = Color(0xFF8B7355),
                            focusedTextColor = Color(0xFF6B5744),
                            unfocusedTextColor = Color(0xFF6B5744)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // حقل كلمة المرور
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        label = { Text("كلمة المرور", color = Color(0xFF8B7355)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon",
                                tint = Color(0xFF8B7355)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) 
                                        Icons.Default.Visibility 
                                    else 
                                        Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) 
                                        "إخفاء كلمة المرور" 
                                    else 
                                        "إظهار كلمة المرور",
                                    tint = Color(0xFF8B7355)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = passwordError != null,
                        supportingText = {
                            passwordError?.let { Text(it, color = Color.Red) }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                performLogin()
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF8B7355),
                            unfocusedBorderColor = Color(0xFFD4C5A9),
                            focusedLabelColor = Color(0xFF8B7355),
                            unfocusedLabelColor = Color(0xFF8B7355),
                            cursorColor = Color(0xFF8B7355),
                            focusedTextColor = Color(0xFF6B5744),
                            unfocusedTextColor = Color(0xFF6B5744)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // نسيت كلمة المرور
                    TextButton(
                        onClick = onNavigateToForgotPassword,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "• نسيت كلمة المرور؟",
                            color = Color(0xFF8B7355),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // زر تسجيل الدخول
                    Button(
                        onClick = { performLogin() },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B5744)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "تسجيل الدخول",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // فاصل
            Text(
                text = "أو تابع التسجيل بإستخدام",
                fontSize = 14.sp,
                color = Color(0xFF8B7355),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // أزرار تسجيل الدخول بوسائل التواصل الاجتماعي
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // زر Google
                IconButton(
                    onClick = { /* Google Sign In */ },
                    modifier = Modifier
                        .size(56.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("G", fontSize = 24.sp, color = Color(0xFF6B5744))
                        }
                    }
                }

                // زر Apple
                IconButton(
                    onClick = { /* Apple Sign In */ },
                    modifier = Modifier
                        .size(56.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("", fontSize = 24.sp, color = Color(0xFF6B5744))
                        }
                    }
                }

                // زر Facebook
                IconButton(
                    onClick = { /* Facebook Sign In */ },
                    modifier = Modifier
                        .size(56.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("f", fontSize = 24.sp, color = Color(0xFF6B5744))
                        }
                    }
                }
            }
        }
    }
}

private fun validateInputs(
    email: String,
    password: String,
    onEmailError: (String?) -> Unit,
    onPasswordError: (String?) -> Unit
): Boolean {
    var isValid = true

    if (email.isBlank()) {
        onEmailError("الرجاء إدخال البريد الإلكتروني")
        isValid = false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onEmailError("البريد الإلكتروني غير صحيح")
        isValid = false
    } else {
        onEmailError(null)
    }

    if (password.isBlank()) {
        onPasswordError("الرجاء إدخال كلمة المرور")
        isValid = false
    } else if (password.length < 6) {
        onPasswordError("كلمة المرور يجب أن تكون 6 أحرف على الأقل")
        isValid = false
    } else {
        onPasswordError(null)
    }

    return isValid
}
