package com.utilites;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.suraksha.R;

/**
 * Created by Sunny on 11/10/2016.
 */

public class Utilites {


    public static void addFragmentToBackStack(AppCompatActivity context, Fragment fragment, boolean addToBackstack) {
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
    }

}
