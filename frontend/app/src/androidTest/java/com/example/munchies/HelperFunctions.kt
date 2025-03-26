package com.example.munchies

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.hamcrest.Matcher
import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


fun waitFor(millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = isRoot()

        override fun getDescription(): String = "Wait for $millis milliseconds."

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadForAtLeast(millis)
        }
    }
}
