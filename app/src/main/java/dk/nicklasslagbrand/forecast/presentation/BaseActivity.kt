package dk.nicklasslagbrand.forecast.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    @LayoutRes
    protected open fun provideLayoutId(): Int? = null
    protected open fun applyExternalArguments(args: Bundle) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideLayoutId()?.let { layoutId ->
            setContentView(layoutId)
        }
        intent.extras?.let { applyExternalArguments(it) }
    }
}
