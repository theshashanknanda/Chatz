package com.project.chatz.source.source.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.project.chatz.source.source.Fragments.SignInFragment;
import com.project.chatz.source.source.Fragments.SignUpFragment;

public class RegistrationAdapter extends FragmentPagerAdapter {
    public static final String titles[] = {"SignUp", "SignIn"};

    public Context context;
    public RegistrationAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = new SignUpFragment();
                break;

            case 1:
                fragment = new SignInFragment();
                break;

            default:
                fragment = new SignUpFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
