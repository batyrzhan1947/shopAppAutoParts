package com.batyrzhan.autoparts.presenters

import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.contracts.ProfileContract
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.database.UserDAO
import com.batyrzhan.common.base.BasePresenter

class ProfilePresenter(
    private val db: AppDataBase
) : BasePresenter<ProfileContract.View>(),
    ProfileContract.Presenter {
    private var executors: Executors? = null

    init {
        executors = Executors()
    }

    override fun setUserIfExist() {
        executors = if(executors != null) Executors() else return
        executors?.diskIO()?.execute {
            if (db.userDao().getCount() > 0) {
                db.userDao().getUser().apply {
                    executors?.mainThread()?.execute {
                        view?.showUserInfo(
                            this.userName,
                            this.userEmail,
                            this.userPhone,
                            this.userAdress
                        )
                    }
                }
            } else {
                executors?.mainThread()?.execute {
                    view?.showUserInfo(
                        "Пользователь", "example@mail.ru", "Your number phone", "Your address"
                    )
                }
            }
        }
    }

    override fun shutdownExecutors() {
        executors?.shutdown()
    }
}