package uk.tees.b1162802.boro.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.tees.b1162802.boro.Adapter.ExpandableListAdapter;
import uk.tees.b1162802.boro.MainActivity;
import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.databinding.FragmentSettingsBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = binding.settingList;
        initData();
        listAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);
        return root;
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Theme Settings");
        listDataHeader.add("Privacy Settings");
        listDataHeader.add("Connectivity Manager");

        List<String> themeSetting = new ArrayList<>();
        themeSetting.add("Dark Mode");

        List<String> privacySetting = new ArrayList<>();
        privacySetting.add("Add FingerPrint");

        List<String> connectSetting = new ArrayList<>();
        connectSetting.add("Bluetooth");

        listHash.put(listDataHeader.get(0),themeSetting);
        listHash.put(listDataHeader.get(1),privacySetting);
        listHash.put(listDataHeader.get(2),connectSetting);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}