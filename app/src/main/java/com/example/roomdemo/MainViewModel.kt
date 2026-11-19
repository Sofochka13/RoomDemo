package com.example.roomdemo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application) : ViewModel() {

    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository
    val searchResults: MutableLiveData<List<Product>>

    init {
        // Создаём базу данных, получаем DAO и инициализируем репозиторий
        val productDb = ProductRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        repository = ProductRepository(productDao)

        // Сохраняем ссылки на данные из репозитория
        allProducts = repository.allProducts
        searchResults = repository.searchResults
    }

    // Метод добавления товара (вызывается из UI)
    fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }

    // Метод поиска товара (вызывается из UI)
    fun findProduct(name: String) {
        repository.findProduct(name)
    }

    // Метод удаления товара (вызывается из UI)
    fun deleteProduct(name: String) {
        repository.deleteProduct(name)
    }
}