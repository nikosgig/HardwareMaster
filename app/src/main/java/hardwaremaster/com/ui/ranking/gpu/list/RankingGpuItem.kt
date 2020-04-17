package hardwaremaster.com.ui.ranking.gpu.list

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import hardwaremaster.com.R
import hardwaremaster.com.data.Gpu
import hardwaremaster.com.internal.glide.GlideApp
import kotlinx.android.synthetic.main.card_gpu.*
import kotlinx.android.synthetic.main.card_gpu.view.*


class RankingGpuItem(
        val gpuEntry: Gpu
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            model.text = gpuEntry.model
            vRamType.text = gpuEntry.graphicsRamType.toString()
            updateDate()
            updateVRamSize()
            updatePrice()
            updateConditionImage()

        }
    }

    override fun getLayout() = R.layout.card_gpu

    private fun ViewHolder.updateConditionImage() {
        if(gpuEntry.model!!.contains("Radeon")) {
            GlideApp.with(this.containerView)
                    .load(R.drawable.ic_amd)
                    .into(containerView.image)
        } else {
            GlideApp.with(this.containerView)
                    .load(R.drawable.ic_nvidia)
                    .into(containerView.image)
        }

    }

    //todo fix concat
    private fun ViewHolder.updatePrice() {
        price.text = gpuEntry.price.toString() + " €"
    }

    //todo fix concat
    private fun ViewHolder.updateVRamSize() {
        vRamSize.text = gpuEntry.graphicsRamSize.toString() + " GB"
    }

    private fun ViewHolder.updateDate() {
        val releaseDateString = gpuEntry.releaseDate.toString()
        date.text = releaseDateString.substring(releaseDateString.lastIndexOf(" ")+1)
    }
}
