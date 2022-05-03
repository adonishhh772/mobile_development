package uk.tees.b1162802.boro.ui.favourite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.tees.b1162802.boro.Adapter.NewsAdapter;
import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.data.model.News;
import uk.tees.b1162802.boro.databinding.FragmentFavouritesBinding;

public class FavouriteFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    ListView favList;
    TextView emptyFav;
    String userID;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "settingpref";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID","Boro Service Provider");
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        emptyFav = binding.noFavorite;
        favList = binding.favouriteList;
        View root = binding.getRoot();
        getAllFavourites();
        return root;
    }

    private void getAllFavourites() {
        try {
            DatabaseReference fav = db.getReference().child("Favourite");
            fav.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        favList.setVisibility(View.VISIBLE);
                        emptyFav.setVisibility(View.GONE);
                        ArrayList<News> arrayList= new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            if(ds.child("user").getValue(String.class).equalsIgnoreCase(userID.trim())){
//                                Log.i("TAG", "onDataChange: "+ds.child("favourite").getValue(String.class));
                                arrayList.add(new News(ds.child("favourite").getValue(String.class),""));
                            }

                        }
                        NewsAdapter newsAdapter = new NewsAdapter(getContext(),R.layout.news_row,arrayList);
                        favList.setAdapter(newsAdapter);
                    }else{
                        emptyFav.setVisibility(View.VISIBLE);
                        favList.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}