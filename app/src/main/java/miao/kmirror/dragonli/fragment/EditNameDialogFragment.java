package miao.kmirror.dragonli.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.lock.widget.activity.FingerLoginActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class EditNameDialogFragment extends DialogFragment {

    private Button toFinger;
    private Button toPassword;
    private Button toImage;

    private TextInfo textInfo;

    public EditNameDialogFragment(TextInfo textInfo) {
        this.textInfo = textInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        toFinger = view.findViewById(R.id.single_lock_to_finger);
        toImage = view.findViewById(R.id.single_lock_to_image);
        toPassword = view.findViewById(R.id.single_lock_to_password);

        toFinger.setOnClickListener(v -> {
            ActivityUtils.simpleIntentWithTextInfo(getContext(), FingerLoginActivity.class, textInfo);
        });
        toImage.setOnClickListener(v -> {
            ToastUtils.toastShort(getContext(), "You click image");
        });
        toPassword.setOnClickListener(v -> {
            ToastUtils.toastShort(getContext(), "You click password");
        });
        // Fetch arguments from bundle and set title
        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
