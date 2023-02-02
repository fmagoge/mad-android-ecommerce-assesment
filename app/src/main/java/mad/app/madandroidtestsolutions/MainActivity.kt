package mad.app.madandroidtestsolutions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.channels.Channel
import mad.app.madandroidtestsolutions.adapter.ProductsAdapter
import mad.app.madandroidtestsolutions.databinding.ActivityMainBinding
import mad.app.madandroidtestsolutions.service.ApiService

class MainActivity : AppCompatActivity() {

    private val apiService = ApiService.createEcommerceClient()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val productList = mutableListOf<CategoryQuery.Item?>()
        val adapter = ProductsAdapter(productList)
        binding.recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
        binding.recyclerView.adapter = adapter

        val channel = Channel<Unit>(Channel.CONFLATED)

        // Send a first item to do the initial load else the list will stay empty forever
        channel.trySend(Unit)
        adapter.onEndOfListReached = {
            channel.trySend(Unit)
        }

        lifecycleScope.launchWhenResumed {

            //First, we grab the root level categories from the e-commerce platform
            val rootItems = apiService.catalog.fetchRootCategory()
            Log.d("CatalogExample", "These are the root level Categories we have available:\n" +
                    "${
                        rootItems?.children
                            ?.filterNotNull()
                            ?.map { category ->
                                "${category.name} (uid: ${category.uid})"
                            }
                            ?.joinToString(separator = "\n")
                    }"
            )


            //Lets grab the mens Category UUID and fetch the products for the first page
            val mensCategoryId = rootItems
                ?.children
                ?.find { it?.name?.lowercase() == "mens" }
                ?.uid

            val firstPageMensCat = mensCategoryId?.let { catId ->
                apiService.catalog.getCategory(categoryId = catId)
            }

            Log.d("CatalogExample",
                "The first page of mens category with CatId ${mensCategoryId} has these products:\n" +
                        "${
                            firstPageMensCat?.data?.products?.items
                                ?.map {
                                    "${it?.name} (uid: ${it?.productListFragment?.uid}) and SKU ${it?.productListFragment?.sku}"
                                }
                                ?.joinToString(separator = "\n")
                        }")


            //Now lets fetch the first product from the first page of the Mens Category
            val firstProductUid = firstPageMensCat?.let {
                it.data?.products?.items?.firstOrNull()?.productListFragment?.uid
            }

            for (item in channel) {

                val products = firstPageMensCat?.let {
                    it.data?.products?.items
                }


                if (products != null) {
                    productList.addAll(products)
                    adapter.notifyDataSetChanged()
                }
            }

            adapter.onEndOfListReached = null
            channel.close()

            val firstProduct = firstProductUid?.let { uid ->
                apiService.catalog?.getProduct(uid)
            }

            Log.d("CatalogExample",
                "The first product is ${firstProduct?.productFragment?.name} with SKU ${firstProduct?.productFragment?.sku}\n" +
                        "This product costs at least " +
                        "R${firstProduct?.productFragment?.productListFragment?.price_range?.priceRangeFragment?.minimum_price?.final_price?.value}"
            )
        }

        adapter.onItemClicked = { launch ->
            Toast.makeText(this, launch?.productListFragment?.name, Toast.LENGTH_SHORT).show();
            startActivity(Intent(this, ItemActivity::class.java).apply {
                putExtra("brand", launch?.productListFragment?.brand)
                putExtra("pName", launch?.productListFragment?.name)
                putExtra("pPrice", "R${launch?.productListFragment?.price_range?.priceRangeFragment?.minimum_price?.final_price?.value}")
            })
        }

    }
}
