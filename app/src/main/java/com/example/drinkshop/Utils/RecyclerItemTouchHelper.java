package com.example.drinkshop.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkshop.Adapters.CartAdapter;
import com.example.drinkshop.Adapters.FavoriteAdapter;
import com.example.drinkshop.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback  {

    RecyclerItemTouchHelperListner listner;
    Context context;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs , RecyclerItemTouchHelperListner listner , Context context) {
        super(dragDirs, swipeDirs);
        this.listner = listner;
        this.context = context;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        if(listner != null){
            listner.onSwipe(viewHolder,direction,viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof FavoriteAdapter.MyViewHolder){
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_sweep)
                    .addSwipeRightBackgroundColor(R.color.colorPrimaryDark)
                    .create()
                    .decorate();
            super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
        }else if(viewHolder instanceof CartAdapter.MyViewHolder){
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_sweep)
                    .addSwipeRightBackgroundColor(R.color.colorPrimaryDark)
                    .create()
                    .decorate();
            super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
        }
    }

    /*@Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreground = ((FavoriteAdapter.MyViewHolder)viewHolder).linearLayout;
        getDefaultUIUtil().clearView(foreground);
    }*/

   /* @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null ) {
            View  forgroundView = ((FavoriteAdapter.MyViewHolder)viewHolder).linearLayout;
            getDefaultUIUtil().onSelected(forgroundView);
        }
    } */

   /* @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View  forgroundView = ((FavoriteAdapter.MyViewHolder)viewHolder).linearLayout;
        getDefaultUIUtil().onDrawOver(c,recyclerView,forgroundView,dX,dY,actionState,isCurrentlyActive);
    } */
}
