package app.com.example.android.newaccountant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

/**
 * Created by Ковтун on 19.12.2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edittxtemail, edittxtpassword;
    Button signUp, login;
    ProgressBar progressbar;
    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edittxtemail = (EditText) findViewById(R.id.editTextEmail);
        edittxtpassword = (EditText) findViewById(R.id.editTextPassword);
        signUp = (Button)findViewById(R.id.buttonSignup);
        login=(Button)findViewById(R.id.buttonLogin);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogin:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.buttonSignup:
                sihnUpUser();
                break;
                
        }
    }

    private void sihnUpUser() {
        String email = edittxtemail.getText().toString().trim();
        String password = edittxtpassword.getText().toString().trim();

        if (email.isEmpty()){
            edittxtemail.setError("Email ir required");
            edittxtemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edittxtemail.setError("Please enter a valid email adress");
            edittxtemail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            edittxtpassword.setError("Email ir required");
            edittxtpassword.requestFocus();
            return;
        }
        if (password.length()<6){
            edittxtpassword.setError("Minimum length of password is 6");
            edittxtpassword.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       progressbar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                       } else{
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "The user already registered",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }}
                });
    }
}
