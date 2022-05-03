package uk.tees.b1162802.boro.ui.privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import uk.tees.b1162802.boro.databinding.PrivacyPolicyBinding;

public class PrivacyFragment extends Fragment {

    private PrivacyPolicyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = PrivacyPolicyBinding.inflate(inflater, container, false);
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
