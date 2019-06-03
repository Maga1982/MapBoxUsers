package gotenna.lakshmi.testmapbox.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import gotenna.lakshmi.testmapbox.R;
import gotenna.lakshmi.testmapbox.modal.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    Context context;
    ArrayList<Users> arrayList;
    Users users;

    public UserAdapter(Context context,ArrayList<Users> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_list,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        users=arrayList.get(position);
        myViewHolder.textView1.setText(String.valueOf(users.getName()));
        myViewHolder.textView2.setText(String.valueOf(users.getDesc()));
        myViewHolder.textView3.setText(String.valueOf(users.getLatitude()));
        myViewHolder.textView4.setText(String.valueOf(users.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1,textView2,textView3,textView4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 =itemView.findViewById(R.id.name);
            textView2 =itemView.findViewById(R.id.desc);
            textView3 =itemView.findViewById(R.id.lat);
            textView4=itemView.findViewById(R.id.lon);

        }

    }
}
