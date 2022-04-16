package uk.tees.b1162802.boro.ui.services.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import uk.tees.b1162802.boro.R;

public class CompleteServices extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.completed_services, container, false);
    }
}
