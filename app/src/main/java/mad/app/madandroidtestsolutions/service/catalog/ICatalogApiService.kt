package mad.app.madandroidtestsolutions.service.catalog
import com.apollographql.apollo3.api.ApolloResponse
import mad.app.madandroidtestsolutions.CategoryQuery
import mad.app.madandroidtestsolutions.CategoryRootQuery
import mad.app.madandroidtestsolutions.ProductQuery

interface ICatalogApiService {
    suspend fun fetchRootCategory(): CategoryRootQuery.CategoryList?

    //Please note, page numbers start at 1
    suspend fun getCategory(
        categoryId: String,
        pageNumber: Int = 1,
        pageSize: Int = 20
    ): ApolloResponse<CategoryQuery.Data>

    suspend fun getProduct(productUid: String): ProductQuery.Product?
    suspend fun getProductsForCategory(
        categoryId: String,
        pageNumber: Int,
        pageSize: Int
    ): CategoryQuery.Products?
}