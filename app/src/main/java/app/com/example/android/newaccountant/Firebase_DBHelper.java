package app.com.example.android.newaccountant;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Firebase_DBHelper {
    DatabaseReference db;





    public Firebase_DBHelper(DatabaseReference db) {
        this.db = db;
    }

    public void saveArticleItem(String tablename, Balance_Item item) {
        db = FirebaseDatabase.getInstance().getReference();
        db.child(tablename).push().setValue(item);

    }


    public void fetchDataByDate(String date_from, String date_to, final CallBack onCallBack) {


        db.orderByChild("date").startAt(date_from).endAt(date_to).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total_sum =0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Balance_Item item = (Balance_Item) ds.getValue(Balance_Item.class);
                    int value = item.getSum();
                    total_sum =+total_sum + value;
                }
                onCallBack.onSuccess(total_sum);
                Log.d("my6", String.valueOf(total_sum));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
  public ArrayList<Balance_Item> sortByDate(ArrayList<Balance_Item> list){
      ArrayList<Balance_Item> sortedlist = new ArrayList<>();
      for (int i=0; i<list.size(); i++){
          int start = Integer.parseInt(list.get(0).getDate());
          if (Integer.parseInt(list.get(i).getDate())>start){
              start = Integer.parseInt(list.get(i).getDate());
          }
          sortedlist.get(i);
      }
      return sortedlist;
  }
    public interface CallBack {
        void onSuccess(int sum);
        void onFail(String msg);
    }
}


