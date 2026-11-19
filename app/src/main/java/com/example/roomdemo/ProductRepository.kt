package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.Deferred

class ProductRepository(private val productDao: ProductDao) {

    // LiveData для всех товаров (обновляется автоматически при изменении БД)
    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    // MutableLiveData для результатов поиска
    val searchResults = MutableLiveData<List<Product>>()

    // Область корутин для выполнения асинхронных операций
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Метод добавления товара (в фоне, через IO-поток)
    fun insertProduct(newProduct: Product) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.insertProduct(newProduct)
        }
    }

    // Метод удаления товара (в фоне, через IO-поток)
    fun deleteProduct(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(name)
        }
    }

    // Метод поиска товара
    fun findProduct(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    // Вспомогательный метод для асинхронного поиска в IO-потоке
    private fun asyncFind(name: String): Deferred<List<Product>?> {
        return coroutineScope.async(Dispatchers.IO) {
            return@async productDao.findProduct(name)
        }
    }
}