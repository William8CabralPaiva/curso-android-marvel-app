package com.example.marvelapp.framework.di.qualifier

import javax.inject.Qualifier

//criar anotação pro hilt
// criar quando tiver injeção de dependencia repetida
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl