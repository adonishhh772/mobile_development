package uk.tees.b1162802.boro.ui.settings;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.tees.b1162802.boro.Adapter.ExpandableListAdapter;
import uk.tees.b1162802.boro.MainActivity;
import uk.tees.b1162802.boro.R;
import uk.tees.b1162802.boro.databinding.FragmentSettingsBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<Map<String,Boolean>>> listHash;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "settingpref";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = binding.settingList;
        initData();
        listAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listHash,sharedPreferences);
        listView.setAdapter(listAdapter);
        return root;
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Theme Settings");
        listDataHeader.add("Privacy Settings");
        listDataHeader.add("Connectivity Manager");

        boolean isNightEnabled = sharedPreferences.getBoolean("night_mode",false);
        Log.i("TAG", "initData: "+isNightEnabled);
        List<Map<String,Boolean>> themeSetting = new ArrayList<>();
        Map<String,Boolean> nightMap = new HashMap<>();
        nightMap.put("Enable Night Mode",isNightEnabled);
        themeSetting.add(nightMap);

        boolean isFingerPrintEnabled = sharedPreferences.getBoolean("finger_print",false);
        List<Map<String,Boolean>> privacySetting = new ArrayList<>();
        Map<String,Boolean> privacyMap = new HashMap<>();
        privacyMap.put("Enable Fingerprint Authentication",isFingerPrintEnabled);
        privacySetting.add(privacyMap);

        List<Map<String,Boolean>> connectSetting = new ArrayList<>();
        Map<String,Boolean> connectMap = new HashMap<>();
        connectMap.put("Connect to a Bluetooth Device",false);
        connectSetting.add(connectMap);

        listHash.put(listDataHeader.get(0),themeSetting);
        listHash.put(listDataHeader.get(1),privacySetting);
        listHash.put(listDataHeader.get(2),connectSetting);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == -1){
            Toast.makeText(getContext(),"Bluetooth Enabled",Toast.LENGTH_LONG).show();
        }else{
            if(requestCode == 0){
                Toast.makeText(getContext(),"Bluetooth Operation Is Cancelled",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}