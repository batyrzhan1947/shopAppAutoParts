package com.batyrzhan.autoparts.presenters

import android.text.Editable
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.contracts.EditUserContract
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.database.UserDAO
import com.batyrzhan.autoparts.database.model.User
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BasePresenter

class EditUserPresenter(
    private val db:AppDataBase
) : BasePresenter<EditUserContract.View>(),
    EditUserContract.Presenter {
    private var executors: Executors? = null
    init {
        executors = Executors()
    }
    override fun shutdownExecutors() {
        executors?.shutdown()
    }

    override fun setUser(name: String, email: String, phone: String, address: String) {
        if (name == "" || email == "" || phone == "" || address == "") {
            view?.setSnackBar("Заполните пустые поля!")
        } else {
            executors?.diskIO()?.execute {
                if (Utils.isValidEmail(email)) {
                    db.userDao().insertUser(User(1, name, email, phone, address))
                    executors?.mainThread()?.execute {
                        view?.setSnackBar("Сохранен!")
                        view?.transitionToFragment()
                    }
                } else {
                    executors?.mainThread()?.execute {
                        view?.setSnackBar("Неправильный Email!")
                    }
                }
            }
        }
    }
    override fun getUserIfExist() {
        executors?.diskIO()?.execute {
            if (db.userDao().getCount() > 0) {
                db.userDao().getUser().apply {
                    executors?.mainThread()?.execute {
                        view?.showUserInfo(
                            Editable.Factory.getInstance().newEditable(this.userName),
                            Editable.Factory.getInstance().newEditable(this.userEmail),
                            Editable.Factory.getInstance().newEditable(this.userPhone),
                            Editable.Factory.getInstance().newEditable(this.userAdress))
                    }
                }
            }
        }
    }
}