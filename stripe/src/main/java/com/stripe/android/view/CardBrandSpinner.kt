package com.stripe.android.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import com.stripe.android.R
import com.stripe.android.databinding.CardBrandSpinnerDropdownBinding
import com.stripe.android.databinding.CardBrandSpinnerMainBinding
import com.stripe.android.model.CardBrand

internal class CardBrandSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.spinnerStyle
) : AppCompatSpinner(context, attrs, defStyleAttr) {
    private val networkAdapter = Adapter(context)
    private var defaultBackground: Drawable? = null

    init {
        adapter = networkAdapter
        dropDownWidth = resources.getDimensionPixelSize(R.dimen.card_brand_spinner_dropdown_width)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        defaultBackground = background

        setCardBrands(
            listOf(CardBrand.Unknown)
        )
    }

    @JvmSynthetic
    fun setCardBrands(networks: List<CardBrand>) {
        networkAdapter.clear()
        networkAdapter.addAll(networks)
        setSelection(0)

        // enable dropdown selector if there are multiple card brands, disable otherwise
        if (networks.size > 1) {
            isClickable = true
            isEnabled = true
            background = defaultBackground
        } else {
            isClickable = false
            isEnabled = false
            setBackgroundColor(
                ContextCompat.getColor(context, android.R.color.transparent)
            )
        }
    }

    internal class Adapter(
        context: Context
    ) : ArrayAdapter<CardBrand>(
        context,
        0
    ) {
        private val layoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val viewBinding = convertView?.let {
                CardBrandSpinnerMainBinding.bind(it)
            } ?: CardBrandSpinnerMainBinding.inflate(layoutInflater, parent, false)

            val cardBrand = requireNotNull(getItem(position))
            viewBinding.image.also {
                it.setImageResource(cardBrand.icon)
                it.contentDescription = cardBrand.displayName
            }

            return viewBinding.root
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val viewBinding = convertView?.let {
                CardBrandSpinnerDropdownBinding.bind(it)
            } ?: CardBrandSpinnerDropdownBinding.inflate(layoutInflater, parent, false)

            val cardBrand = requireNotNull(getItem(position))
            viewBinding.textView.also {
                it.text = cardBrand.displayName
                it.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    cardBrand.icon,
                    0, 0, 0
                )
            }

            return viewBinding.root
        }
    }
}
