package com.codefuelindia.wecarefarm.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Cart.class},version = 1)
public abstract class CartDatabase extends RoomDatabase {

    private static CartDatabase INSTANCE;

    public abstract CartDAO cartDAO();

  public   static CartDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CartDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CartDatabase.class, "cart_db")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
