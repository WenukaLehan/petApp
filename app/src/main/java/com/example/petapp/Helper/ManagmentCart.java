package com.example.petapp.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.petapp.Domain.Foods;

import java.util.ArrayList;


public class ManagmentCart {
    private Context context;
    private CartData cartData;
    ChangeNumberItemsListener changeNumberItemsListener;

    public ManagmentCart(Context context) {
        this.context = context;
        this.cartData =new CartData(context);
    }

    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        }else{
            listpop.add(item);
        }
        cartData.putListObject("CartList",listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Foods> getListCart() {
        return cartData.getListObject("CartList");
    }

    public void clearCart() {
        ArrayList<Foods> itemList = getListCart();
        for (int i = 0; i < itemList.size(); i++) {
            itemList.remove(i);
        }
        cartData.putListObject("CartList", itemList);

    }

    public Double getTotalFee(){
        ArrayList<Foods> listItem=getListCart();
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }

    public void minusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        cartData.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public  void plusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        cartData.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public  void removeItem(ArrayList<Foods> listItem,int position, ChangeNumberItemsListener changeNumberItemsListener){
        listItem.remove(position);
        cartData.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
}
