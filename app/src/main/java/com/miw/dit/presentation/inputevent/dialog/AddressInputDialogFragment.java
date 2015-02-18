package com.miw.dit.presentation.inputevent.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miw.dit.R;

/**
 * Created by Dani on 14/2/15.
 */
public class AddressInputDialogFragment extends DialogFragment {

    private static final String ARG_ADDRESS = "address";

    private AddressInputDialogListener listener;

    private EditText textAddressInput;

    private String address;

    public static AddressInputDialogFragment newInstance(String address) {
        AddressInputDialogFragment fragment = new AddressInputDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            address = getArguments().getString(ARG_ADDRESS);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewRoot = inflater.inflate(R.layout.dialog_address_input, null);

        textAddressInput = (EditText) viewRoot.findViewById(R.id.text_address_input);
        if (address != null) {
            textAddressInput.setText(address);
        }

        Button buttonAccept = (Button) viewRoot.findViewById(R.id.button_accept);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddress();
                dismiss();
            }
        });
        Button buttonCancel = (Button) viewRoot.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setCancelable(false);
        builder.setView(viewRoot);
        return builder.create();
    }

    private void changeAddress() {
        String address = textAddressInput.getText().toString();
        if (listener != null)
            listener.onAddressInput(address);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (AddressInputDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddressInputDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface AddressInputDialogListener {
        public void onAddressInput(String address);
    }
}
