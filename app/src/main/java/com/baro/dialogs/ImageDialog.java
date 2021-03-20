package com.baro.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.baro.R;
import com.baro.constants.AppCodes;


public class ImageDialog extends AppCompatDialogFragment {

    private ImageButton gallery;
    private ImageButton roll;


    public interface OnInputListener {
        void sendInput(int choice);
    }

    public OnInputListener onInputListener;



    public ImageDialog(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_image_chooser, container, false);

        gallery = view.findViewById(R.id.btn_folder);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInputListener.sendInput(AppCodes.GALLERY_SELECTION.code);
                getDialog().dismiss();
            }
        });

        roll = view.findViewById(R.id.btn_camera);
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInputListener.sendInput(AppCodes.CAMERA_ROLL_SELECTION.code);
                getDialog().dismiss();
            }
        });
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            onInputListener = (OnInputListener) getActivity();
        } catch(ClassCastException e) {
        }
    }
}
