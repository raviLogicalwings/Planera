package com.planera.mis.planera2.activities.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planera.mis.planera2.R;
import com.planera.mis.planera2.activities.adapters.POBAdapter;

public class PODFragment extends BaseFragment {

    private RecyclerView recycleViewPob;
    private View view;
    private POBAdapter pobAdapter;
    public PODFragment() {
        // Required empty public constructor
    }


    public static PODFragment newInstance(String param1, String param2) {
        PODFragment fragment = new PODFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_pod, container, false);
        initUi();
        initData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    protected void initUi() {
        super.initUi();
        recycleViewPob = view.findViewById(R.id.recycle_view_pob);
    }

    @Override
    protected void initData() {
        super.initData();
        pobAdapter = new POBAdapter(getContext());
        setAdapter(recycleViewPob,pobAdapter );
    }

    public void setAdapter(RecyclerView recyclerView, POBAdapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
