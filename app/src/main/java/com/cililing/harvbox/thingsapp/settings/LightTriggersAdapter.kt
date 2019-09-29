package com.cililing.harvbox.thingsapp.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.model.LightTrigger
import com.cililing.harvbox.thingsapp.model.TriggerType
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick

class LightTriggersAdapter(
    private val presenter: SettingsContract.Presenter,
    private var triggerCollection: List<LightTrigger>,
    private val lightId: SettingsContract.LightId
) : RecyclerView.Adapter<LightTriggersAdapter.LightTriggerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightTriggerViewHolder {
        return LightTriggerViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.view_light_trigger, parent, false))
    }

    override fun getItemCount(): Int {
        return triggerCollection.count()
    }

    override fun onBindViewHolder(holder: LightTriggerViewHolder, position: Int) {
        holder.bind(triggerCollection[position])
    }

    fun updateTriggers(triggerCollection: List<LightTrigger>) {
        this.triggerCollection = triggerCollection
        notifyDataSetChanged()
    }

    inner class LightTriggerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val switch = itemView.find<Switch>(R.id.trigger_switch)
        private val removeButton = itemView.find<Button>(R.id.trigger_remove)

        @SuppressLint("SetTextI18n")
        fun bind(lightTrigger: LightTrigger) {
            switch.text = lightTrigger.hour.toString().padStart(2, '0') +
                    ":" +
                    lightTrigger.minute.toString().padStart(2, '0')
            switch.isChecked = lightTrigger.type == TriggerType.ON

            switch.onCheckedChange { _, isOn ->
                presenter.changeTriggerType(lightId, lightTrigger, isOn)
            }
            removeButton.onClick {
                presenter.removeTrigger(lightId, lightTrigger)
            }
        }
    }
}