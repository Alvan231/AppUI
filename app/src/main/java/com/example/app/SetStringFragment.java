package com.example.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetStringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetStringFragment extends Fragment {

    View view;

    //up_str_tv, down_str_tv, left_str_tv, right_str_tv
    TextView up_str_tv, down_str_tv, left_str_tv, right_str_tv;

    //up_edit, down_edit, left_edit, right_edit
    EditText up_edit, down_edit, left_edit, right_edit;

    Button RefactorBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetStringFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetStringFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetStringFragment newInstance(String param1, String param2) {
        SetStringFragment fragment = new SetStringFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_set_string, container, false);

        up_str_tv = view.findViewById(R.id.up_str_tv);
        down_str_tv = view.findViewById(R.id.down_str_tv);
        left_str_tv = view.findViewById(R.id.left_str_tv);
        right_str_tv = view.findViewById(R.id.right_str_tv);

        up_edit = view.findViewById(R.id.up_edit);
        down_edit = view.findViewById(R.id.down_edit);
        left_edit = view.findViewById(R.id.left_edit);
        right_edit = view.findViewById(R.id.right_edit);

        RefactorBtn = view.findViewById(R.id.refactor_btn);

        //set string
        RefactorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!up_edit.getText().toString().isEmpty()) {
                    up_str_tv.setText(up_edit.getText().toString());
                    up_edit.setText(null);
                }
                if(!down_edit.getText().toString().isEmpty()) {
                    down_str_tv.setText(down_edit.getText().toString());
                    down_edit.setText(null);
                }
                if(!left_edit.getText().toString().isEmpty()) {
                    left_str_tv.setText(left_edit.getText().toString());
                    left_edit.setText(null);
                }
                if(!right_edit.getText().toString().isEmpty()) {
                    right_str_tv.setText(right_edit.getText().toString());
                    right_edit.setText(null);
                }
                Toast.makeText(getContext(),"Refactor",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}