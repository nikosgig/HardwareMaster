package hardwaremaster.com.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import hardwaremaster.com.R

class CaptionValueView(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {

    init {
        inflate(context, R.layout.caption_value_view, this)

        val captionTextView: TextView = findViewById(R.id.score_title)
        val valueTextView: TextView = findViewById(R.id.score_value)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CaptionValueView)
        captionTextView.text = attributes.getString(R.styleable.CaptionValueView_captionText)
        valueTextView.text = attributes.getString(R.styleable.CaptionValueView_valueText)
        attributes.recycle()

    }
}