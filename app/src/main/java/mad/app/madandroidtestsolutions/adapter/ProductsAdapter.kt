package mad.app.madandroidtestsolutions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import mad.app.madandroidtestsolutions.CategoryQuery
import mad.app.madandroidtestsolutions.R
import mad.app.madandroidtestsolutions.databinding.ItemBinding

class ProductsAdapter(private val productList: List<CategoryQuery.Item?>) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    var onEndOfListReached: (() -> Unit)? = null
    var onItemClicked: ((CategoryQuery.Item?) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        with(holder.binding){
            productBrand.text = product?.productListFragment?.brand
            productName.text = product?.productListFragment?.name
            productPrice.text = "R${product?.productListFragment?.price_range?.priceRangeFragment?.minimum_price?.final_price?.value}"
            productImg.load(R.drawable.ironman)
        }

        if (position == productList.size - 1) {
            onEndOfListReached?.invoke()
        }

        holder.binding.root.setOnClickListener {
            onItemClicked?.invoke(product)
        }
    }
}