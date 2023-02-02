package mad.app.madandroidtestsolutions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import coil.load
import mad.app.madandroidtestsolutions.databinding.ActivityItemBinding

class ItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_item)

        binding.productName.text = intent.getStringExtra("pName").toString()
        binding.productBrand.text = intent.getStringExtra("brand").toString()
        binding.productPrice.text = intent.getStringExtra("pPrice").toString()
        binding.productImg.load(R.drawable.ironman)
    }
}