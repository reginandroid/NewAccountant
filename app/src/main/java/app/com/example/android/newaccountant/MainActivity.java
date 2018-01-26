package app.com.example.android.newaccountant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button btnSignup, btnLogin;
    EditText edittxtemail, edittxtpassword;
    private FirebaseAuth mAuth;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edittxtemail = (EditText) findViewById(R.id.editTextEmail);
        edittxtpassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        btnSignup = (Button)findViewById(R.id.buttonSignup);
        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        progressbar = (ProgressBar)findViewById(R.id.progressbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

            switch (view.getId()){
                case R.id.buttonLogin:
                    userLogin();
                    break;
                case R.id.buttonSignup:
                    Intent i = new Intent(this, SignUpActivity.class);
                    startActivity(i);
                    break;

        }
    }

    private void userLogin() {
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
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
