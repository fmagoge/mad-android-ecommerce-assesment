package mad.app.madandroidtestsolutions.service.catalog

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import kotlinx.coroutines.flow.*
import mad.app.madandroidtestsolutions.CategoryQuery
import mad.app.madandroidtestsolutions.CategoryRootQuery
import mad.app.madandroidtestsolutions.ProductQuery
import mad.app.madandroidtestsolutions.type.FilterEqualTypeInput
import mad.app.madandroidtestsolutions.type.ProductAttributeFilterInput

class CatalogApiService(val apolloClient: ApolloClient) : ICatalogApiService {

    override suspend fun fetchRootCategory(): CategoryRootQuery.CategoryList? {
        return apolloClient.query(CategoryRootQuery())
            .toFlow()
            .map {
                it.data?.categoryList?.firstOrNull()
            }
            .single()
    }

    private fun fetchCategory(
        uuid: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<ApolloResponse<CategoryQuery.Data>> {
        return apolloClient.query(
            CategoryQuery(
                categoryUid = uuid,
                pageSize = pageSize,
                currentPage = pageNumber,
                filter = ProductAttributeFilterInput(
                    category_uid = Optional.present(FilterEqualTypeInput(eq = Optional.present(uuid)))
                )
            )
        ).toFlow()
    }

    override suspend fun getCategory(
        categoryId: String,
        pageNumber: Int,
        pageSize: Int
    ): ApolloResponse<CategoryQuery.Data> {
        return fetchCategory(categoryId, pageNumber, pageSize).single()
    }

    override suspend fun getProductsForCategory(
        categoryId: String,
        pageNumber: Int,
        pageSize: Int
    ): CategoryQuery.Products? {
        return fetchCategory(categoryId, pageNumber, pageSize)
            .map {
                it.data?.products
            }
            .single()
    }

    override suspend fun getProduct(productUid: String): ProductQuery.Product? {
        return apolloClient.query(ProductQuery(uid = productUid))
            .toFlow()
            .map {
                it?.data?.product
            }
            .firstOrNull()
    }

}