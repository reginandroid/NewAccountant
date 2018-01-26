package app.com.example.android.newaccountant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Charges_Fragment extends Fragment {
    LinearLayoutManager nwlinearLayoutManager;
    FirebaseAdapter mFirebaseAdapter;
    Firebase_DBHelper mFirebase_dbHelper;
    DatabaseReference db, db1;
    SharedPreferences sp;
    RecyclerView rv;
    TextView totalSum;
    ArrayList<Balance_Item> mItemArrayList;
    public Charges_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_charges, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        rv = (RecyclerView)rootView.findViewById(R.id.charges_list);
        nwlinearLayoutManager = new LinearLayoutManager(getActivity());
        nwlinearLayoutManager.setStackFromEnd(true);
        totalSum = (TextView)rootView.findViewById(R.id.tvSum);

        db= FirebaseDatabase.getInstance().getReference(Banance_Contract.TABLE_NAME_CHARGES);
        mFirebase_dbHelper = new Firebase_DBHelper(db);
        refreshChargeRV();

        mFirebaseAdapter.setOnClickListener(new FirebaseAdapter.ClickListener() {

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to Delete this item?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mFirebaseAdapter.getRef(position).removeValue();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirm");
                dialog.show();

            }
        });
        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("my", "onDestroy was made");

    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshChargeRV();
        refreshChargeTotalTextView();

        if (!getUserVisibleHint())
        {
            return;
        }

        StartActivity mainActivity = (StartActivity) getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_NewIncomeCharge df = Dialog_NewIncomeCharge.newInstance("NEW CHARGE", Banance_Contract.TABLE_NAME_CHARGES);

                df.show(getFragmentManager(), "df_charge");

            }
        });
    }
    public void  refreshChargeRV(){
        mFirebaseAdapter = new FirebaseAdapter(getContext(), Balance_Item.class, R.layout.item_list, FirebaseAdapter.MyViewHolder.class,
                db.orderByChild("date").startAt(sp.getString("date_from", "")).endAt(sp.getString("date_to", "")));
        rv.setLayoutManager(nwlinearLayoutManager);
        rv.setAdapter(mFirebaseAdapter);
    }
    public  void refreshChargeTotalTextView(){
        mFirebase_dbHelper.fetchDataByDate( sp.getString("date_from", ""),
                sp.getString("date_to", ""),new Firebase_DBHelper.CallBack() {
                    @Override
                    public void onSuccess(int total_sum) {
                        (sp.edit().putInt("total_charge", total_sum)).commit();
                        totalSum.setText(String.valueOf(total_sum));
                    }
                    @Override
                    public void onFail(String msg) {

                    }
                });
    }
}