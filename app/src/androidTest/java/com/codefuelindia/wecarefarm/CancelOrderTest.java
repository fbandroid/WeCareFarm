package com.codefuelindia.wecarefarm;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.codefuelindia.wecarefarm.model.TodayAgentDelivery;
import com.codefuelindia.wecarefarm.view.ApproveDeliveryByAgent;
import com.codefuelindia.wecarefarm.view.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CancelOrderTest {

    @Rule
    public ActivityTestRule<ApproveDeliveryByAgent> mActivityRule =
            new ActivityTestRule(ApproveDeliveryByAgent.class){

                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra("data", new TodayAgentDelivery());
                    return intent;
                }


            };


    @Test
   public void cancelOrder(){

        onView(withId(R.id.btnCancelDelivery)).perform(click());

        assertEquals("3",mActivityRule.getActivity().getStatus());

        }

    @Test
    public void ApproveOrder(){
        onView(withId(R.id.btnMakeDelivery)).perform(click());

        assertEquals("6",mActivityRule.getActivity().getStatus());
    }



}
