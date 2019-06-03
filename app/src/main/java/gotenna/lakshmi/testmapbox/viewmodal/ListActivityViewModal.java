package gotenna.lakshmi.testmapbox.viewmodal;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;


import gotenna.lakshmi.testmapbox.modal.Users;
import gotenna.lakshmi.testmapbox.repository.UserLoader;

public class ListActivityViewModal extends ViewModel {
    MutableLiveData<ArrayList<Users>> mutableLiveData;
    UserLoader userLoader;


    public void init(Context context){
            if(mutableLiveData !=null)
                return;
            userLoader=UserLoader.getInstance();
        Log.v("inside ","view model init");

        mutableLiveData=userLoader.getMutableLiveData(context);


    }
    public MutableLiveData<ArrayList<Users>> getUsers() {
        return mutableLiveData;

    }


}
