package com.planera.mis.planera2.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.VisitsAdapter;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private RecyclerView visitList;
    private View mainView;
    private VisitsAdapter visitsAdapter;

    public HomeFragment() {
    }



    @Override
    protected void initUi() {
        super.initUi();
        visitList = mainView.findViewById(R.id.visitList);


    }

    @Override
    protected void initData() {
        super.initData();
        visitsAdapter = new VisitsAdapter(getContext());
        setAdapter(visitList, visitsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       mainView = inflater.inflate(R.layout.fragment_home, container, false);
       initUi();
       initData();
        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setAdapter(RecyclerView recyclerView, VisitsAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_check_in:
//                Intent intent = new Intent(getContext(), ScheduleTimeActivity.class);
//                getActivity().startActivity(intent);
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
