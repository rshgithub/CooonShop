package com.example.cocoonshop.SuperClasses;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.example.cocoonshop.R;
import com.example.cocoonshop.UserInterfaces.Cart;
import com.example.cocoonshop.MainActivities.ContactUsActicity;
import com.example.cocoonshop.UserInterfaces.FavouritesActivity;
import com.example.cocoonshop.UserInterfaces.ProfileActivity;
import com.example.cocoonshop.MainActivities.Authentication.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class Constraints {

    public static final String PRODUCT_KEY = "KEY";
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public static final int RESULT_LOAD_IMAGE = 1;

    public static final String PRODUCT_CLICK = "ProductClick";

    public static final String PRODUCT_IMG = "ProductImage";
    public static final String PRODUCT_CAT = "ProductCategory";
    public static final String PRODUCT_NAME = "ProductName";
    public static final String PRODUCT_DESC = "ProductDescription";
    public static final String PRODUCT_NEW_PRICE = "ProductNewPrice";
    public static final String PRODUCT_OLD_PRICE = "ProductOldPrice";

    public static final String SEARCH_TEXT = "SearchText";

    public static final String CAT_WOMAN = "WomanCategory";
    public static final int CAT_WOMAN_VAL = 1;

    public static final String CAT_MEN = "MenCategory";
    public static final int CAT_MEN_VAL = 2;

    public static final String CAT_KIDS = "KidsCategory";
    public static final int CAT_KIDS_VAL = 3;

    public static final String ALL_CATS = "AllCategories";
    public static final int ALL_CATS_VAL = 4;

    public static final String DSICOUNT_PRODS = "DiscountedProducts";
    public static final int DISCOUNT_PRODS_VAL = 5;

    public static final String BEST_SELL_PRODS = "BestSellProducts";
    public static final int BEST_SELL_PRODS_VAL = 6;

    public static final int SPLASH_TIME_OUT = 1500;

    public static Intent navigationItemSelectedMethod(MenuItem item, int type, Context context) {

        int itemId = item.getItemId();
        switch (itemId) {

            case R.id.nav_home:
                if (type != 1) {
                    return null;
                }
                break;

            case R.id.profile:
                if (type != 2) {
                    return new Intent(context, ProfileActivity.class);
                }
                break;

            case R.id.nav_favs:
                if (type != 3) {
                    return new Intent(context, FavouritesActivity.class);
                }
                break;

            case R.id.nav_cart:
                if (type != 4) {
                    return new Intent(context, Cart.class);
                }
                break;

            case R.id.nav_contactUs:
                if (type != 5) {
                    return new Intent(context, ContactUsActicity.class);
                }
                break;

            case R.id.nav_sign_out:
                if (type != 6) {
                    mAuth.getInstance().signOut();
                    Intent intent = new Intent(context, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    return intent ;
                }
                break;
            default:
                return null;

        }
        return null;
    }

}
