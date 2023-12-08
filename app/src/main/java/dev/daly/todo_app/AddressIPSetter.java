package dev.daly.todo_app;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;

public class AddressIPSetter extends BottomSheetDialogFragment {

    public static final String TAG = "AddressIPSetter";

    private EditText addressIPEditText;
    private Button addressIPSaveButton;

    private RequestHandler requestHandler;


    public static AddressIPSetter newInstance() {
        return new AddressIPSetter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.set_up_ip, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressIPEditText =  view.findViewById(R.id.addressIPEditText);
        addressIPSaveButton = view.findViewById(R.id.addressIPSaveButton);
        requestHandler = new RequestHandler(getContext());
            if (!addressIPEditText.getText().toString().isEmpty()) {
                addressIPSaveButton.setTextColor(getResources().getColor(R.color.purple));
            } else {
                addressIPSaveButton.setTextColor(getResources().getColor(R.color.darker_gray));
            }
        addressIPEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressIPSaveButton.setEnabled(!s.toString().isEmpty());
                if (s.toString().isEmpty()) {
                    addressIPSaveButton.setTextColor(getResources().getColor(R.color.darker_gray));
                } else {
                    addressIPSaveButton.setTextColor(getResources().getColor(R.color.purple));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addressIPSaveButton.setOnClickListener(v -> {
             String addressIP = addressIPEditText.getText().toString();
                if(addressIP.isEmpty()){
                    addressIPEditText.setError("Address IP cannot be empty");
                } else {
                    requestHandler.getUsers(addressIP, getActivity());
                }
        });
    }
    @Override
    public void onDismiss(android.content.DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Activity activity = getActivity();
        if (activity instanceof dev.daly.todo_app.DialogInterface) {
            ((dev.daly.todo_app.DialogInterface) activity).handleDialogClose(dialogInterface);
        }
    }
}
