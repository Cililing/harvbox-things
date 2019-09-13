package com.cililing.harvbox.thingsapp.core.mvp

import androidx.fragment.app.Fragment

abstract class BaseFragment<Presenter : BasePresenter<*>> : Fragment(), BaseView<Presenter>