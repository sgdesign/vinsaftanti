package com.saeedsoft.security.CallBlocker;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

//import com.saeedsoft.security.R;
import com.saeedsoft.security.R;
import com.saeedsoft.security.stuff.AppContext;
import com.saeedsoft.security.stuff.Util;


public class AddManualFragment extends Fragment {
    private View.OnClickListener okButtonListener;
    private EditText etPhoneNumber;
    private CheckBox chBeginWith;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.add_manual_fragment, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        okButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String number = etPhoneNumber.getText().toString();
                boolean checked = chBeginWith.isChecked();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getContext(), R.string.add_manual_error_empty, Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                AppContext appContext = (AppContext) getActivity().getApplicationContext();
                boolean valid = appContext.getBlackListWrapper().checkUserInput(number, checked);
                if (!valid) {
                    Toast.makeText(getContext(), R.string.add_manual_error_invalid, Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                int count = appContext.getBlackListWrapper().addNumberToBlackList(number, null,
                        chBeginWith.isChecked());
                if (count == 1) {
                    Toast.makeText(getActivity(), String.format(getResources().getString(R.string
                            .add_manual_msn_number_added), number), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), R.string.add_manual_msn_failed, Toast.LENGTH_LONG)
                            .show();
                }

                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etPhoneNumber = (EditText) view.findViewById(R.id.addManualTfPhoneNumber);

        chBeginWith = (CheckBox) view.findViewById(R.id.addManualCbBeginWith);
        chBeginWith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPhoneNumber.setHint("123");
                } else {
                    etPhoneNumber.setHint(Util.getNumberExample(getContext()));

                }

            }
        });
        //This sets the initial hint on addManualTfPhoneNumber
        etPhoneNumber.setHint(Util.getNumberExample(getContext()));

        Button btOk = (Button) view.findViewById(R.id.addManualBtOk);
        btOk.setOnClickListener(okButtonListener);

        view.findViewById(R.id.addManualBtCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });
    }

}