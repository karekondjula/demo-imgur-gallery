package team2.imgurgallery.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import team2.imgurgallery.BuildConfig;
import team2.imgurgallery.R;

/**
 * Created by Daniel on 31-Oct-15.
 */
public class DialogFragmentAbout extends DialogFragment {

    public static final String TAG = "TAG_ABOUT";

    @BindView(R.id.version)
    TextView version;

    @BindView(R.id.dev_time)
    TextView devTime;

    protected Unbinder unbinder;

    public DialogFragmentAbout() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_about, null);
        unbinder = ButterKnife.bind(this, view);

        builder.setView(view);
        Dialog dialog = builder.create();

        dialog.setTitle(R.string.about_title);

        version.setText(String.format(getString(R.string.version_), BuildConfig.VERSION_NAME));
        devTime.setText(String.format(getString(R.string.build_time_), getString(R.string.dev_time)));

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}