package com.f83.freshfridge;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class FridgesFragment extends Fragment {
    private int visibility;
    private int selectedFridgeID;

    public FridgesFragment() {
        // Required empty public constructor

    }

    public static FridgesFragment newInstance(int visibility,int selectedFridgeID) {
        Bundle args = new Bundle();
        FridgesFragment fragment = new FridgesFragment();
        args.putInt("visibility",visibility);
        args.putInt("selectedFridgeID",selectedFridgeID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null){
            visibility = bundle.getInt("visibility",0);
            selectedFridgeID = bundle.getInt("selectedFridgeID");
        }
        createFridgesListFragment();
        if (selectedFridgeID != 0){
            createProductListFragment(selectedFridgeID);
        }
        return inflater.inflate(R.layout.fragment_fridges, container, false);

    }

    public void createFridgesListFragment(){
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        FridgeListFragment fragment = FridgeListFragment.newInstance(visibility);
        transaction.replace(R.id.fragment_container,fragment,fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }
    public void createProductListFragment(int id){
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ProductListFragment fragment = ProductListFragment.newInstance(visibility,id);
        transaction.replace(R.id.fragment_container,fragment,fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();

    }

    public void setSelectedFridgeID(int id){
        if (selectedFridgeID == 0){
            selectedFridgeID = id;
            if ((getActivity()) != null){
                ((MainActivity)getActivity()).selectedFridgeID = id;
            }
        }
    }
    public void setSelectedFridgeID(){
        selectedFridgeID = 0;
        if ((getActivity()) != null){
            ((MainActivity)getActivity()).selectedFridgeID = 0;
        }
    }

}
