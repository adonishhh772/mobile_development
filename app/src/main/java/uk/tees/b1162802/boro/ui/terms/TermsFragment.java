package uk.tees.b1162802.boro.ui.terms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.databinding.FragmentTermsBinding;

public class TermsFragment extends Fragment {

    private FragmentTermsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTermsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTitle;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}