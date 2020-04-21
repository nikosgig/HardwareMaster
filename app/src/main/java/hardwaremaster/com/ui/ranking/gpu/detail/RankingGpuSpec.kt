package hardwaremaster.com.ui.ranking.gpu.detail

import android.view.View
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import hardwaremaster.com.R
import kotlinx.android.synthetic.main.card_gpu_details.*


class RankingGpuSpec(
        private val description: String,
        val value: String,
        private var onClickListener: View.OnClickListener? = null
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            detail_description.text = description
            detail_value.text = value
            setupEditButton()
        }
    }

    private fun ViewHolder.setupEditButton() {
        if(description == "Price") {
            imageButton_edit.visibility = View.VISIBLE
            imageButton_edit.setOnClickListener(onClickListener)
        }

    }

    override fun getLayout() = R.layout.card_gpu_details

}
