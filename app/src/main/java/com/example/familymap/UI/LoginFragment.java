package com.example.familymap.UI;

import android.os.Bundle;
import Request.LoginReq;
import Request.RegisterReq;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.familymap.R;
import com.example.familymap.Tasks.LoginTask;
import com.example.familymap.Tasks.RegisterTask;

public class LoginFragment extends Fragment implements LoginTask.LoginContext, RegisterTask.RegisterContext {
    private TextWatcher mWatcher;
    private LoginReq myLoginRequest;
    private RegisterReq myRegisterRequest;
    private LoginListener loginListener;
    private EditText serverHostText;
    private EditText serverPortText;
    private EditText usernameText;
    private EditText passwordText;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailText;
    private Button maleButton;
    private Button femaleButton;
    private Button registerButton;
    private Button loginButton;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLoginRequest = new LoginReq(null,null);
        myRegisterRequest = new RegisterReq(null,null,null,null,null,null);
    }

    private void validLogin(){
        String s1 = serverHostText.getText().toString();
        String s2 = serverPortText.getText().toString();
        String s3 = usernameText.getText().toString();
        String s4 = passwordText.getText().toString();

        if(s1.equals("")|| s2.equals("")|| s3.equals("") || s4.equals(""))
            loginButton.setEnabled(false);
        else
            loginButton.setEnabled(true);
    }

    private void validRegister(){
        String s1 = serverHostText.getText().toString();
        String s2 = serverPortText.getText().toString();
        String s3 = usernameText.getText().toString();
        String s4 = passwordText.getText().toString();
        String s5 = firstNameText.getText().toString();
        String s6 = lastNameText.getText().toString();
        String s7 = emailText.getText().toString();

        if(s1.equals("")|| s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("") || s6.equals("") || s7.equals(""))
            registerButton.setEnabled(false);
        else
            registerButton.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myRegisterRequest.setGender("m");
        View v = inflater.inflate(R.layout.login_register, container, false);
        mWatcher = new Enabler();

        serverPortText = v.findViewById(R.id.serverPortEditText);
        serverPortText.addTextChangedListener(mWatcher);

        serverHostText = v.findViewById(R.id.serverHostEditText);
        serverHostText.addTextChangedListener(mWatcher);

        usernameText = v.findViewById(R.id.userNameEditText);
        usernameText.addTextChangedListener(mWatcher);

        passwordText = v.findViewById(R.id.passwordEditText);
        passwordText.addTextChangedListener(mWatcher);

        firstNameText = v.findViewById(R.id.firstNameEditText);
        firstNameText.addTextChangedListener(mWatcher);

        lastNameText = v.findViewById(R.id.lastNameEditText);
        lastNameText.addTextChangedListener(mWatcher);

        emailText = v.findViewById(R.id.emailEditText);
        emailText.addTextChangedListener(mWatcher);

        maleButton = v.findViewById(R.id.maleRadioButton);
        maleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                myRegisterRequest.setGender("m");
                validLogin();
                validRegister();
            }
        });
        femaleButton = v.findViewById(R.id.femaleRadioButton);
        femaleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                myRegisterRequest.setGender("f");
                validLogin();
                validRegister();
            }
        });

        loginButton = v.findViewById(R.id.signInbutton);
        registerButton = v.findViewById(R.id.registerButton);
        validLogin();
        validRegister();

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                myLoginRequest.setUsername(usernameText.getText().toString());
                myLoginRequest.setPassword(passwordText.getText().toString());

                LoginTask loginTask = new LoginTask(serverHostText.getText().toString(), serverPortText.getText().toString(),
                        LoginFragment.this);
                loginTask.execute(myLoginRequest);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                myRegisterRequest.setUsername(usernameText.getText().toString());
                myRegisterRequest.setEmail(emailText.getText().toString());
                myRegisterRequest.setFirstName(firstNameText.getText().toString());
                myRegisterRequest.setLastName(lastNameText.getText().toString());
                myRegisterRequest.setPassword(passwordText.getText().toString());

                RegisterTask regTask = new RegisterTask(serverHostText.getText().toString(), serverPortText.getText().toString(),
                        LoginFragment.this);

                regTask.execute(myRegisterRequest);
            }
        });
        return v;
    }

    @Override
    public void onExecuteComplete(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        loginListener.loginComplete();
    }

    public void setLoginListener(LoginListener logListen) {
        loginListener = logListen;
    }

    public interface LoginListener {
        void loginComplete();
    }

    private class Enabler implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validLogin();
            validRegister();
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

}