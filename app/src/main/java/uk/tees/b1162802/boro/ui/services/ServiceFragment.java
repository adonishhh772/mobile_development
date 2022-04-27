package uk.tees.b1162802.boro.ui.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import uk.tees.b1162802.boro.Adapter.TabsAdapter;
import uk.tees.b1162802.boro.databinding.FragmentServicesBinding;
import uk.tees.b1162802.boro.ui.services.tabs.CompleteServices;
import uk.tees.b1162802.boro.ui.services.tabs.Upcoming;

public class ServiceFragment extends Fragment {

    private FragmentServicesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentServicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager viewPager = (ViewPager) binding.viewPager;
        TabLayout tabLayout = (TabLayout) binding.tabLayout;
        TabsAdapter adapter = new TabsAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new CompleteServices(), "Completed Services");
        adapter.addFragment(new Upcoming(), "Upcoming Services");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        binding.tabLayout
//        final TextView textView = binding.textGallery;
//        serviceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}