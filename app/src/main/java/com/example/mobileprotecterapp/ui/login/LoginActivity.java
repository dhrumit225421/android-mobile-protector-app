package com.example.mobileprotecterapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileprotecterapp.MainActivity;
import com.example.mobileprotecterapp.MapActivity;
import com.example.mobileprotecterapp.R;
import com.example.mobileprotecterapp.WelcomeActivity;
import com.example.mobileprotecterapp.model.ResponseLogin;
import com.example.mobileprotecterapp.model.User;
import com.example.mobileprotecterapp.ui.login.LoginViewModel;
import com.example.mobileprotecterapp.ui.login.LoginViewModelFactory;
import com.example.mobileprotecterapp.databinding.ActivityLoginBinding;
import com.example.mobileprotecterapp.utils.AppUtil;
import com.example.mobileprotecterapp.utils.LibFile;
import com.google.android.material.textfield.TextInputEditText;
import com.shreejipackaging.data.ResponseManager;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final TextInputEditText usernameEditText = binding.EtUsername;
        final TextInputEditText passwordEditText = binding.EtPassword;
        final Button loginButton = binding.login;
        final TextView lblNew = binding.lblNew;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        /*loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                //Complete and destroy login activity once successful
                finish();
            }
        });*/

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    User user = new User();
                    user.setUserName(usernameEditText.getText().toString());
                    user.setPassword(passwordEditText.getText().toString());
                    userLogin(user);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                User user = new User();
                user.setUserName(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                userLogin(user);
            }
        });

        lblNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void userLogin(User userName) {
        loginViewModel.isLoadingDisplay(true);
        loginViewModel.userLogin(userName).observe(this, new Observer<ResponseManager<ResponseLogin>>() {
            @Override
            public void onChanged(ResponseManager<ResponseLogin> responseLoginResponseManager) {
                loginViewModel.isLoadingDisplay(false);

                if (responseLoginResponseManager instanceof ResponseManager.Success) {
                    if (((ResponseManager.Success<ResponseLogin>) responseLoginResponseManager).getData().getToken() != null) {
                        LibFile.Companion.getInstance(getBaseContext())
                            .setString(LibFile.KEY_TOKEN, ((ResponseManager.Success<ResponseLogin>) responseLoginResponseManager).getData().getToken());

                        /*LibFile.Companion.getInstance(getBaseContext())
                            .setString(LibFile.KEY_LOGIN_USER, Gson().toJson(it.data.user))*/

                        //Goto to main Activity
                        setResult(Activity.RESULT_OK);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        //Complete and destroy login activity once successful
                        finish();
                    }

                } else if (responseLoginResponseManager instanceof ResponseManager.Unauthenticated) {

                    if (!((ResponseManager.Unauthenticated) responseLoginResponseManager).getMessage().isEmpty()) {
                        AppUtil.Companion.displayInfoDialog(
                                LoginActivity.this,
                        ((ResponseManager.Unauthenticated) responseLoginResponseManager).getMessage()
                        );
                    }

                } else {
                    AppUtil.Companion.displayInfoDialog(LoginActivity.this, ((ResponseManager.Error) responseLoginResponseManager).getMessage())
                }
            }
        });
    }
}