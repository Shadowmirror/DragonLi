package miao.kmirror.dragonli.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import miao.kmirror.dragonli.R;
import miao.kmirror.dragonli.entity.TextInfo;
import miao.kmirror.dragonli.lock.widget.activity.FingerLoginActivity;
import miao.kmirror.dragonli.singleActivity.SingleFingerLockActivity;
import miao.kmirror.dragonli.singleActivity.SingleImageLockActivity;
import miao.kmirror.dragonli.singleActivity.SinglePasswordLockActivity;
import miao.kmirror.dragonli.utils.ActivityUtils;
import miao.kmirror.dragonli.utils.ToastUtils;

public class SelectLockTypeFragment extends DialogFragment {

    private Button toFinger;
    private Button toPassword;
    private Button toImage;

    private TextInfo textInfo;


    public SelectLockTypeFragment(TextInfo textInfo) {
        this.textInfo = textInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.fragment_select_lock_type, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setLayout(-1, -2);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        toFinger = view.findViewById(R.id.single_lock_to_finger);
        toImage = view.findViewById(R.id.single_lock_to_image);
        toPassword = view.findViewById(R.id.single_lock_to_password);

        toFinger.setOnClickListener(v -> {
            ActivityUtils.simpleIntentWithTextInfo(getContext(), SingleFingerLockActivity.class, textInfo);
        });
        toImage.setOnClickListener(v -> {
            ActivityUtils.simpleIntentWithTextInfo(getActivity(), SingleImageLockActivity.class, textInfo);
        });
        toPassword.setOnClickListener(v -> {
            ActivityUtils.simpleIntentWithTextInfo(getActivity(), SinglePasswordLockActivity.class, textInfo);
        });
        // Fetch arguments from bundle and set title
        // Show soft keyboard automatically and request focus to field
        getDialog()
                .getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
}
