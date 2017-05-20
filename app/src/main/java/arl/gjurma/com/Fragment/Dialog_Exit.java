package arl.gjurma.com.Fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import arl.gjurma.com.Interfaces.ExitAppListner;
import arl.gjurma.com.R;

/**
 * Created by KRYTON on 26-09-2016.
 */
public class Dialog_Exit extends DialogFragment {
    private String TAG = "Dialog_Exit";
    private ExitAppListner mClose;
    Button btnNo;
    Button btnYes;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View v = inflater.inflate(R.layout.dialog_exit, container, false);

        btnNo = (Button) v.findViewById(R.id.btn_no);
        btnYes = (Button) v.findViewById(R.id.btn_yes);
//        ButterKnife.bind(this, v);

        mClose = (ExitAppListner) getActivity();

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        Log.d(TAG, "onViewCreate");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClose.closeApp(0);
                dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClose.closeApp(1);
                dismiss();
            }
        });
    }

}
