package com.isidroid.a18.sample.viewmodels.di

import com.isidroid.a18.sample.viewmodels.IPostsRepository
import com.isidroid.a18.sample.viewmodels.PostsRepository
import com.isidroid.a18.sample.viewmodels.PostsViewModelFactory
import com.isidroid.a18.sample.viewmodels.SamplePostsActivity
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Singleton
@Component(modules = [PostsModule::class])
interface PostsComponent {
    fun inject(activity: SamplePostsActivity)
}

@Module
class PostsModule {
    @Provides @Singleton
    fun listViewModelFactory(repository: IPostsRepository, compositeDisposable: CompositeDisposable)
            : PostsViewModelFactory = PostsViewModelFactory(repository, compositeDisposable)

    @Provides @Singleton
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides @Singleton
    fun provideRepository(): IPostsRepository = PostsRepository(provideCompositeDisposable())
}