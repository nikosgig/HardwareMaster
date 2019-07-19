package hardwaremaster.com.util;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import hardwaremaster.com.R;

public class FragmentUtils {

    public static CrystalRangeSeekbar generateRangeSeekBarView(RelativeLayout rangeSeekBarLayout,
                                                               int label, long rbMinValue,
                                                               long rbMaxValue) {
        final CrystalRangeSeekbar rangeSeekBar = rangeSeekBarLayout.findViewById(R.id.rangeSeekBar);
        final TextView title = rangeSeekBarLayout.findViewById(R.id.title);
        final TextView tvMin = rangeSeekBarLayout.findViewById(R.id.min);
        final TextView tvMax = rangeSeekBarLayout.findViewById(R.id.max);

        rangeSeekBar.setMinValue(rbMinValue);
        rangeSeekBar.setMaxValue(rbMaxValue);
        title.setText(label);
        tvMin.setText(rbMinValue + "");
        tvMax.setText(rbMaxValue + "+");

        rangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                if ((long) maxValue == rbMaxValue) {
                    tvMax.setText(maxValue + "+");
                } else {
                    tvMax.setText(String.valueOf(maxValue));
                }
            }
        });

        return rangeSeekBar;
    }
}
