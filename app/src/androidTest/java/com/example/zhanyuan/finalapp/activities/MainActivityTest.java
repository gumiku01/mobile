package com.example.zhanyuan.finalapp.activities;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.zhanyuan.finalapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.float_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Container),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.float_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Container),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.description_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                2),
                        isDisplayed()));
        editText.perform(replaceText("prova "), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.type_checkbox), withText("type"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                4),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.type_checkbox), withText("type"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                4),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.type_checkbox), withText("type"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                4),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.type_checkbox), withText("type"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                4),
                        isDisplayed()));
        button4.perform(click());

        DataInteraction constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(3);
        constraintLayout.perform(click());

        DataInteraction constraintLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(3);
        constraintLayout2.perform(click());

        DataInteraction constraintLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(1);
        constraintLayout3.perform(click());

        DataInteraction constraintLayout4 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(4);
        constraintLayout4.perform(click());

        DataInteraction constraintLayout5 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(4);
        constraintLayout5.perform(click());

        DataInteraction constraintLayout6 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(4);
        constraintLayout6.perform(click());

        DataInteraction constraintLayout7 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(4);
        constraintLayout7.perform(click());

        DataInteraction constraintLayout8 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(4);
        constraintLayout8.perform(click());

        DataInteraction constraintLayout9 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(4);
        constraintLayout9.perform(click());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.confirm_btn), withText("confirm"),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1),
                        isDisplayed()));
        button5.perform(click());

        DataInteraction constraintLayout10 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(4);
        constraintLayout10.perform(click());

        ViewInteraction button6 = onView(
                allOf(withId(R.id.confirm_btn), withText("confirm"),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1),
                        isDisplayed()));
        button6.perform(click());

        ViewInteraction button7 = onView(
                allOf(withId(R.id.confirm_btn), withText("confirm"),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1),
                        isDisplayed()));
        button7.perform(click());

        ViewInteraction button8 = onView(
                allOf(withId(R.id.confirm_btn), withText("confirm"),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1),
                        isDisplayed()));
        button8.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tb_calc_num_3), withText("3"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                2),
                        isDisplayed()));
        textView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tb_calc_num_2), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        textView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tb_calc_num_done), withText("SAVE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.keyboard),
                                        5),
                                2),
                        isDisplayed()));
        textView3.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.save), withText("SAVE"),
                        childAtPosition(
                                allOf(withId(R.id.layout),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        textView4.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.description_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                2),
                        isDisplayed()));
        editText2.perform(replaceText("Did "), closeSoftKeyboard());

        ViewInteraction button9 = onView(
                allOf(withId(R.id.type_checkbox), withText("type"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                4),
                        isDisplayed()));
        button9.perform(click());

        ViewInteraction button10 = onView(
                allOf(withId(R.id.type_checkbox), withText("type"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.constraint.ConstraintLayout")),
                                        1),
                                4),
                        isDisplayed()));
        button10.perform(click());

        DataInteraction constraintLayout11 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(3);
        constraintLayout11.perform(click());

        DataInteraction constraintLayout12 = onData(anything())
                .inAdapterView(allOf(withId(R.id.detail_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(5);
        constraintLayout12.perform(click());

        ViewInteraction button11 = onView(
                allOf(withId(R.id.confirm_btn), withText("confirm"),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1),
                        isDisplayed()));
        button11.perform(click());

        ViewInteraction button12 = onView(
                allOf(withId(R.id.confirm_btn), withText("confirm"),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1),
                        isDisplayed()));
        button12.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tb_calc_num_2), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        textView5.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.tb_calc_num_5), withText("5"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        textView6.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.tb_calc_num_9), withText("9"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                2),
                        isDisplayed()));
        textView7.perform(click());

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.tb_calc_num_done), withText("SAVE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.keyboard),
                                        5),
                                2),
                        isDisplayed()));
        textView8.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.main_bottom_bar),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3)),
                        1),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction relativeLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.main_bottom_bar),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3)),
                        2),
                        isDisplayed()));
        relativeLayout2.perform(click());

        pressBack();

        pressBack();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
