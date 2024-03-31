package fr.isen.Jail.androiderestaurant

import android.content.Context
import android.view.ActionProvider
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class BadgeActionProvider(private val context: Context) : ActionProvider(context) {
    private var badge: TextView? = null
    private var itemCount = 0

    override fun onCreateActionView(): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionView = inflater.inflate(R.layout.cart_menu_item, null)
        badge = actionView.findViewById(R.id.cart_quantity)
        updateItemCount()
        return actionView
    }

    fun setItemCount(count: Int) {
        this.itemCount = count
        updateItemCount()
    }

    private fun updateItemCount() {
        if (badge != null) {
            if (itemCount == 0) {
                badge?.visibility = View.GONE
            } else {
                badge?.visibility = View.VISIBLE
                badge?.text = itemCount.toString()
            }
        }
    }
}