package sk.styk.martin.apkanalyzer.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> Fragment.provideViewModel(crossinline block: () -> VM): VM {
    return ViewModelProvider(
        this,
        object : ViewModelProvider.Factory {
            override fun <A : ViewModel> create(modelClass: Class<A>): A {
                @Suppress("UNCHECKED_CAST")
                return block() as A
            }
        },
    )[VM::class.java]
}

inline fun <reified VM : ViewModel> AppCompatActivity.provideViewModel(crossinline block: () -> VM): VM {
    return ViewModelProvider(
        this,
        object : ViewModelProvider.Factory {
            override fun <A : ViewModel> create(modelClass: Class<A>): A {
                @Suppress("UNCHECKED_CAST")
                return block() as A
            }
        },
    )[VM::class.java]
}

inline fun <reified VM : ViewModel> Fragment.provideViewModel(viewModelFactory: ViewModelProvider.Factory? = null): VM {
    viewModelFactory?.let {
        return ViewModelProvider(this, it)[VM::class.java]
    }
    return ViewModelProvider(this)[VM::class.java]
}

inline fun <reified VM : ViewModel> Fragment.provideViewModelOfParentFragment(viewModelFactory: ViewModelProvider.Factory? = null): VM {
    viewModelFactory?.let {
        return ViewModelProvider(this.requireParentFragment(), it)[VM::class.java]
    }
    return ViewModelProvider(this.requireParentFragment())[VM::class.java]
}

inline fun <reified VM : ViewModel> Fragment.provideViewModelOfParentFragment(crossinline block: () -> VM): VM {
    return ViewModelProvider(
        this.requireParentFragment(),
        object : ViewModelProvider.Factory {
            override fun <A : ViewModel> create(modelClass: Class<A>): A {
                @Suppress("UNCHECKED_CAST")
                return block() as A
            }
        },
    )[VM::class.java]
}

inline fun <reified VM : ViewModel> AppCompatActivity.provideViewModel(viewModelFactory: ViewModelProvider.Factory? = null): VM {
    viewModelFactory?.let {
        return ViewModelProvider(this, it)[VM::class.java]
    }
    return ViewModelProvider(this)[VM::class.java]
}
