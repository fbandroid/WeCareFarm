package com.codefuelindia.wecarefarm.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert
    long insertCart(Cart cart);

    @Query("select * from cart where cid = :id group by pid")
    List<Cart> getCartList(String id);


    @Query("select * from cart where pid = :id")
    List<Cart> getCartListByProduct(String id);


    @Delete
    int deleteCart(Cart cart);

    @Query("delete from cart")
    void deleteAllOrder();

    @Query("update cart set qty = qty + :qty where pid = :id ")
    long insertIfExist(String id, double qty);



}
