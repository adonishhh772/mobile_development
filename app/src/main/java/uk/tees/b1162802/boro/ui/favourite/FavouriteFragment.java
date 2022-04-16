package uk.tees.b1162802.boro.ui.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import uk.tees.b1162802.boro.databinding.FragmentFavouritesBinding;

public class FavouriteFragment extends Fragment {

    private FragmentFavouritesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavouriteViewModel favouriteViewModel =
                new ViewModelProvider(this).get(FavouriteViewModel.class);

        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        favouriteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}