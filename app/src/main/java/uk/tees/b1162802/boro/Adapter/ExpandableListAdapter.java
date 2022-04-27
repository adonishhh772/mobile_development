package uk.tees.b1162802.boro.Adapter;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.tees.b1162802.boro.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private static final int BLUETOOTH_REQ_CODE = 1;
    boolean checked;
    private List<String> listDataHeader;
    private HashMap<String, List<Map<String, Boolean>>> listHashMap;
    private SharedPreferences sharedPreferences;
    private BluetoothAdapter bluetoothAdapter;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<Map<String, Boolean>>> listHashMap, SharedPreferences preferences) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.sharedPreferences = preferences;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.setting_list_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.settingLabelHeader);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Map<String, Boolean> childMap = (Map) getChild(groupPosition, childPosition);
        boolean isSettingChecked = false;
        String childText = "";
        for (Object key : childMap.keySet()) {
            childText = key.toString();
            isSettingChecked = childMap.get(key.toString());
        }
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.setting_list_item, null);
        }
        SwitchMaterial textListChild = (SwitchMaterial) convertView.findViewById(R.id.enableDisableButton);
        textListChild.setText(childText);
        textListChild.setChecked(isSettingChecked);
        textListChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                checked = isChecked;
                // true if the switch is in the On position
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (buttonView.getText() == "Enable Night Mode") {
                    if (isChecked) {
                        editor.putBoolean("night_mode", true);
                        editor.apply();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                            buttonView.setChecked(isChecked);
//                            Log.i("TAG", "onCheckedChanged: "+isChecked);
                    } else {
                        editor.putBoolean("night_mode", false);
                        editor.apply();
//                            buttonView.setChecked(isChecked);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    }
                }

                if (buttonView.getText() == "Enable Fingerprint Authentication") {
                    if (isChecked) {
                        editor.putBoolean("finger_print", true);
                        editor.apply();
                    } else {
                        editor.putBoolean("finger_print", false);
                        editor.apply();
                    }

                }

                if (buttonView.getText() == "Connect to a Bluetooth Device") {
                    boolean bluetoothResult = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED;
                    boolean bluetoothAdminResult = ContextCompat.checkSelfPermission(context,Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;

                    if(!bluetoothResult && !bluetoothAdminResult){
                        ActivityCompat.requestPermissions((Activity) context,new String[] {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},100);
                    }
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter == null) {
                        Toast.makeText(context, "This device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
                    }
                    if (isChecked) {
                        if(!bluetoothAdapter.isEnabled()){
                            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                Toast.makeText(context, "Permission for bluetooth not enabled", Toast.LENGTH_LONG).show();
                                textListChild.setChecked(false);
                                return;
                            }
                            ((Activity) context).startActivityForResult(bluetoothIntent, BLUETOOTH_REQ_CODE);
                        }

                        editor.putBoolean("bluetooth",true);
                        editor.apply();
                    }else{
                        if(bluetoothAdapter.isEnabled()){
                            bluetoothAdapter.disable();
                        }
                        editor.putBoolean("bluetooth",false);
                        editor.apply();
                    }

                }

            }
        });
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}
