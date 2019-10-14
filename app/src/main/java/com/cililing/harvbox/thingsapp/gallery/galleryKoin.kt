package com.cililing.harvbox.thingsapp.gallery

import com.cililing.harvbox.thingsapp.core.FirebaseCloudDatabase
import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val galleryScope = { module: Module ->
    module.scope(named<GalleryFragment>()) {
        scoped { params ->
            GalleryPresenter(
                params[0],
                params[1]
            ) as GalleryContract.Presenter
        }
    }
}

fun GalleryContract.View.getPresenterParams(
    view: GalleryContract.View,
    firebaseCloudDatabase: FirebaseCloudDatabase
): DefinitionParameters {
    return parametersOf(view, firebaseCloudDatabase)
}