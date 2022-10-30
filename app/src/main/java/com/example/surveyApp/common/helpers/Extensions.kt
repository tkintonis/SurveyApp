package com.example.surveyApp.common.helpers

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import com.example.surveyApp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Fragment.hideKeyboard(view: View) {
    (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)
}

fun showToastMessage(view: View, message: String, resources: Resources, action: (() -> Unit)? = null) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setBackgroundTint(ResourcesCompat.getColor(resources, R.color.g700, null))
        .apply {
            if(action != null)
                setActionTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                    .apply {
                        dismiss()
                        setAction(R.string.retry) { action.invoke() }
                    }
        }
        .show()
}

fun Fragment.repeatWithLifecycle(lifecycleStatus: Lifecycle.State, block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifecycleStatus) {
            block()
        }
    }
}

fun AppCompatActivity.repeatWithLifecycle(lifecycleStatus: Lifecycle.State, block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(lifecycleStatus) {
            block()
        }
    }
}

fun NavController.safeNavigate(direction: NavDirections, extras: Navigator.Extras? = null) {
    currentDestination?.getAction(direction.actionId)?.run {
        if (extras != null) {
            navigate(direction, extras)
        } else {
            navigate(direction)
        }
    }
}