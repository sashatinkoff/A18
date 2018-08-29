package com.isidroid.a18;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

public class DetailFragment extends Fragment {

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.i("onCreate");

        boolean name = getArguments().getBoolean("NAME");
        Timber.i("name=" + name);
    }

    public static DetailFragment create() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("NAME", false);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
