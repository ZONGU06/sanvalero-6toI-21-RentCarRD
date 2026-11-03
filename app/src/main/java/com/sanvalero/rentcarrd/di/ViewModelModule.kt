package com.sanvalero.rentcarrd.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.sanvalero.rentcarrd.ui.auth.AuthViewModel
import com.sanvalero.rentcarrd.ui.client.CatalogViewModel
import com.sanvalero.rentcarrd.ui.dashboard.DashboardViewModel
import com.sanvalero.rentcarrd.ui.operator.OperationsViewModel
import com.sanvalero.rentcarrd.ui.admin.AdminViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideAuthViewModel(): AuthViewModel {
        return AuthViewModel()
    }

    @Provides
    @Singleton
    fun provideCatalogViewModel(): CatalogViewModel {
        return CatalogViewModel()
    }

    @Provides
    @Singleton
    fun provideDashboardViewModel(): DashboardViewModel {
        return DashboardViewModel()
    }

    @Provides
    @Singleton
    fun provideOperationsViewModel(): OperationsViewModel {
        return OperationsViewModel()
    }

    @Provides
    @Singleton
    fun provideAdminViewModel(): AdminViewModel {
        return AdminViewModel()
    }

    // Factory para viewModels con parámetros
    @Provides
    @Singleton
    fun provideViewModelFactory(
        authViewModel: AuthViewModel,
        catalogViewModel: CatalogViewModel,
        dashboardViewModel: DashboardViewModel,
        operationsViewModel: OperationsViewModel,
        adminViewModel: AdminViewModel
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            @Override
            fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return authViewModel as T
                } else if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return catalogViewModel as T
                } else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return dashboardViewModel as T
                } else if (modelClass.isAssignableFrom(OperationsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return operationsViewModel as T
                } else if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return adminViewModel as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
            }
        }
    }
}