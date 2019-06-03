package gotenna.lakshmi.testmapbox.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import gotenna.lakshmi.testmapbox.R;
import gotenna.lakshmi.testmapbox.adapters.UserAdapter;
import gotenna.lakshmi.testmapbox.modal.Users;
import gotenna.lakshmi.testmapbox.viewmodal.ListActivityViewModal;


public class ListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public UserAdapter userAdapter;
    public ArrayList arrayList;
    public ListActivityViewModal listActivityViewModal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        arrayList=new ArrayList<Users>();
        recyclerView=findViewById(R.id.recycler_view);
        Log.v("inside ","list activity");

        listActivityViewModal= ViewModelProviders.of(this).get(ListActivityViewModal.class);
        listActivityViewModal.init(this);
        listActivityViewModal.getUsers().observe(this, new Observer<ArrayList<Users>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Users> users) {
                userAdapter.notifyDataSetChanged();
                Log.v("inside ","listactivity datachange");
            }
        });
        userAdapter=new UserAdapter(this,listActivityViewModal.getUsers().getValue());
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}
