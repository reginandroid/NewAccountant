package app.com.example.android.newaccountant;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Dialog_NewIncomeCharge extends DialogFragment implements View.OnClickListener {
    final String LOG_TAG = "myLogs";
    Button select_date, btn_save, btn_cancel;
    private int mYear, mMonth, mDay;
    EditText txtDate, income_name, income_summ;
    public static final String TITLE = "title";
    public static final String TABLE_NAME = "table_name";
    String title;
    String table_name;

    FirebaseAuth mFirebaseAuth;
    DatabaseReference db;
    private Firebase_DBHelper mFirebase_dbHelper;


    public static Dialog_NewIncomeCharge newInstance(String title, String tablename) {
        Dialog_NewIncomeCharge f = new Dialog_NewIncomeCharge();
        f.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        Bundle b = new Bundle();
        b.putString(TITLE, title);
        b.putString(TABLE_NAME, tablename);
        f.setArguments(b);
        return f;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString(TITLE);
            Log.d("my", title);
            getDialog().setTitle(title);
            mFirebaseAuth = FirebaseAuth.getInstance();
        }

        View v = inflater.inflate(R.layout.income_dialog, null);
         select_date = (Button) v.findViewById(R.id.select_date);
        select_date.setOnClickListener(this);
        txtDate = (EditText)v.findViewById(R.id.in_date) ;
        txtDate.setOnClickListener(this);
        btn_save = (Button)v.findViewById(R.id.btnSave);
        btn_cancel = (Button)v.findViewById(R.id.btnCancel);
        income_name = (EditText)v.findViewById(R.id.income_name);
        income_summ = (EditText)v.findViewById(R.id.income_summ);
        v.findViewById(R.id.btnSave).setOnClickListener(this);
        v.findViewById(R.id.btnCancel).setOnClickListener(this);

        return v;
    }

  public void onClick(View v) {

        if (v == select_date) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @TargetApi(24)
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String day =null, month=null;
                            int current_month = monthOfYear+1;
                            if(current_month < 10){

                                month= "0" + current_month;
                            } else
                                month = String.valueOf(current_month);
                            if(dayOfMonth < 10){

                                day= "0" + dayOfMonth ;
                            }
                            else day = String.valueOf(dayOfMonth);

                            txtDate.setText(year + "-" + month + "-" + day );
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            (sharedPreferences.edit().putString("item_date",year + "-" + month + "-" + day )).commit();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
        if (v == btn_save) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String date = sharedPref.getString("item_date", "");
            String item_name = income_name.getText().toString();
            if (item_name.isEmpty()){
                income_name.setError("Name ir required");
                income_name.requestFocus();
                return;
            }
            String input_sum = income_summ.getText().toString().trim();
            int item_sum=0;
            if( input_sum==null || input_sum.isEmpty() || input_sum.length() == 0 ||  input_sum.equals("")){
                income_summ.setError("Sum ir required");
                income_summ.requestFocus();
            }
            else {
                item_sum = Integer.parseInt(input_sum);
            }

            Balance_Item item = new Balance_Item(date, item_name, item_sum);
            FirebaseUser user = mFirebaseAuth.getCurrentUser();

            Bundle bundle = this.getArguments();
            if (bundle != null) {
               table_name = bundle.getString(TABLE_NAME);
            }
            db  = FirebaseDatabase.getInstance().getReference();
            mFirebase_dbHelper = new Firebase_DBHelper(db);
           mFirebase_dbHelper.saveArticleItem(table_name, item);


            Log.d("my",item.getDate() + " "+ item.getItem_name() + " "+ item.getSum());

            dismiss();
        }
            if (v == btn_cancel) {
                getDialog().dismiss();
            }

    }
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
        Log.d("my", "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }

}
